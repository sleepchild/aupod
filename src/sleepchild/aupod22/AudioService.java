package sleepchild.aupod22;
import android.app.*;
import android.os.*;
import android.content.*;
import java.util.*;
import mrtubbs.postman.*;
import sleepchild.aupod22.postmanmodels.*;
import android.media.*;

public class AudioService extends Service implements SongFactory.FactoryEvents
{
    public static final int REQUEST_CODE = 914826;
    public static final String CMD_START = "sleepchild.aupod.service_cmd.start";
    
    public static final String CMD_PLAY = "sleepchild.aupod.service_cmd_PLAY";
    public static final String CMD_PAUSE = "sleepchild.aupod.service_cmd.PAUSECMD";
    public static final String CMD_PLAY_PAUSE = "sleepchild.aupod.service_cmd.PAUSEplayp";
    public static final String CMD_PLAY_NEXT = "sleepchild.aupod.service_cmd.playnextsong";
    public static final String CMD_PLAY_PREV = "sleepchild.aupod.service_cmdplayprev.song";
    
    public static final String CMD_MEDIA_BUTTON = "sleepchild.aupod.service.MEDIA_BUTTON.CMD";
    
    private Handler handle = new Handler(Looper.getMainLooper());
    private AudioPlayer player;
    AudioManager audioMgr;
    private SPrefs prefs;
    private Context ctx;
    final int nid = 826384;
    boolean receiversReg=false;
    
    // todo: create a singleton global object to hold the songs?
    private List<SongItem> songlist=null;
    
    @Override
    public IBinder onBind(Intent p1){
        return null;
    }

    @Override
    public void onCreate(){
        PostMan.getInstance().register(this);
        super.onCreate();
        ctx = getApplicationContext();
        audioMgr = (AudioManager) getSystemService(AUDIO_SERVICE);
        prefs = new SPrefs(ctx);
        player = new AudioPlayer();
        SongFactory.get().registerEventListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        String cmd = intent.getAction();
        switch(cmd){
            case CMD_START:
                PostMan.getInstance().post(new AudioServiceConnect(this));
                break;
            case CMD_PLAY:
                resume();
                break;
            case CMD_PAUSE:
                pause();
                break;
            case CMD_PLAY_NEXT:
                playNext();
                break;
            case CMD_PLAY_PREV:
                playPrev();
                break;
            case CMD_MEDIA_BUTTON:
                handleMediaButton();
                break;
        }
        return START_NOT_STICKY;// 2
    }

    public static void start(Context ctx){
        Intent i = new Intent(ctx, AudioService.class);
        i.setAction(CMD_START);
        ctx.startService(i);
    }

    @Override
    public void onDestroy()
    {
        unRegisterAudioNoisy();
        unRegisterMediaButtons();
        player.stop();
        super.onDestroy();
    }
    
    
    /////////////////
    ///
    
    public void playSong(SongItem si){
        player.play(si);
        prefs.saveLastPath(si.path);
    }
    
    public void pause(){
        player.pause();
    }
    
    public void resume(){
        player.resume();
    }
    
    public void playPause(){
        if(player.isPlaying()){
            pause();
        }else{
            resume();
        }
    }
    
    public void playPrev(){
        int idx = songlist.indexOf(getCurrentSong());
        idx--;
        if(idx<0){
            idx=0;
        }
        playSong(songlist.get(idx));
    }
    
    public void playNext(){
        int idx = songlist.indexOf( getCurrentSong() );
        idx++;
        if(idx<songlist.size()){
            playSong(songlist.get(idx));
        }
    }
    
    public boolean isPlaying(){
        return player.isPlaying();
    }
    
    public SongItem getCurrentSong(){
        return player.getCurrentSong();
    }
    
    public int getCurrentPosition(){
        return player.getCurrentPosition();
    }
    
    public void seekTo(int pos){
        player.seekTo(pos);
    }
    
    
    ////////////////
    ////
    
    //todo: how to safely/efficiently update songlist?
    public void getSongList(){
        if(songlist==null){
            SongFactory.get().getSongs(this);
        }else{
            //todo: we should update the songlist in case sth changed!!
            PostMan.getInstance().post(new SongsListReadyEvent(songlist));
        }
    }
    
    
    /////////////////////
    //// 

    @PostEvent
    public void onPlayRequest(final PlaybackRequest req){
        handle.postDelayed(new Runnable(){
            public void run(){
                switch(req.getType()){
                    case PlaybackRequest.TYPE.PLAY_SONGITEM:
                        if(req.object instanceof SongItem){
                            playSong((SongItem)req.object);
                        }
                        break;
                    case PlaybackRequest.TYPE.PAUSE:
                        pause();
                        break;
                    case PlaybackRequest.TYPE.PLAY:
                        resume();
                        break;
                }
            }
        },0);
    }
    
    @PostEvent
    public void onSongComplete(SongCompletionEvent evt){
        handle.postDelayed(new Runnable(){
            public void run(){
                playNext();
            }
        },0);  
    }
    
    @PostEvent
    public void onResumeSong(SongResumeEvent evt){
        startForeground(nid, Noteaf.get(this, player.getCurrentSong(), true) );
        registerReceivers();
    }
    
    @PostEvent
    public void onPauseSong(SongPauseEvent evt){
        startForeground(nid, Noteaf.get(this, player.getCurrentSong(), false) );
        stopForeground(false);
    }

    // todo: COMPLETE REWRITE NEDED!
    boolean f2=true;
    @Override
    public void onGetSongsResult(final List<SongItem> list){
        handle.postDelayed(new Runnable(){
            public void run(){
                songlist = list;
                //*
                if(!songlist.isEmpty() && f2){
                    f2 = false;
                    player.setSong(songlist.get(0));
                    
                    /////
                    // this is terrible. We Must implement a playing queue and do this there
                    
                    //@lp : last played song path
                    String lp = prefs.getLastPath();
                    // lp is empty bcause it was reset or was never set
                    if(lp.isEmpty()){
                        player.setSong(songlist.get(0));
                    }else{
                        SongItem ss=null;
                        for (SongItem s : songlist){
                            if(s.path.equals(lp)){
                                ss = s;
                                break;// break the loop; no need to check others
                            }
                        }
                        if(ss!=null){// song still exists in same path as last time.
                            player.setSong(ss);
                        }else{// we reach here because the song was moved/renamed/deleted
                            //set the first song int he list and clear the preference
                            player.setSong(songlist.get(0));
                            prefs.saveLastPath("");
                        } 
                    }
                }
                //*/
                // setup done; send the list
                PostMan.getInstance().post(new SongsListReadyEvent(songlist));
            }
        },0);  
    }

    @Override
    public void onGetSongsError(String message){
        //
    }
    
    
    ////////////////
    // the private life of AudioService.
    
    private void registerReceivers(){
        if(receiversReg){return;}
        receiversReg = true;
        registerAudioNoisy();
        registerMediaButtons();
    }
    
    private void registerAudioNoisy(){
        //
    }
    
    private void unRegisterAudioNoisy(){
        //
    }
    
    ComponentName mediaButtonComp;// <-- move to top of file.
    private void registerMediaButtons(){
        mediaButtonComp = new ComponentName(this, ButtonReciever.class.getName());
        audioMgr.registerMediaButtonEventReceiver(mediaButtonComp);
    }
    
    private void unRegisterMediaButtons(){
        audioMgr.unregisterMediaButtonEventReceiver(mediaButtonComp);
    }
    
    private void handleMediaButton(){
        playPause();
    }
    
}
