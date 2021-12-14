package sleepchild.aupod22;
import android.app.*;
import android.os.*;
import android.content.*;
import java.util.*;
import android.media.session.*;
import android.media.*;
import mrtubbs.postman.*;
import sleepchild.aupod22.postmanmodels.*;

public class AudioService extends Service implements SongFactory.FactoryEvents
{
    //
    public static final String CMD_START ="cmd.sleep.au.service.start";
    public static final String CMD_STOP ="sleepy.aupod.cmd.stop.playservice";
    //public static final String EVENT_SERVICE_READY = "sleep.aup.apservice.event.apready";
    public static final int requestCode = 999483;
    public static final String CMD_PLAY ="sleep.au.service.notif.play";
    public static final String CMD_PAUSE ="sleep.au.service.notif.pause.i";
    public static final String CMD_PLAY_NEXT ="sleep.au.service.notif.playNExtsong";
    public static final String CMD_PLAY_PREV ="sleep.au.service.notif.playtrack.previous";
    
    private AudioPlayer aupod;
    private Handler mHandle = new Handler(Looper.getMainLooper());
    private SongItem currentSong, iSong;
    private List<SongItem> songList;// = new ArrayList<>();
    
    private AudioManager am;
    boolean audioFocusGranted=false;
   // boolean isPlaying=false;
    
    Context ctx;

    @Override
    public void onCreate()
    {
        super.onCreate();
        PostMan.getInstance().register(this);
        ctx = getApplicationContext();
        
        aupod = new AudioPlayer(this);
        SongFactory.getInstance().getAll(this, this);
        //
        registerNoisy();
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        //registerHeadsetPlug();
        //*/
    }
    
    @PostEvent
    public void onPlaybackRequest(PlaybackRequest req){
        PlaybackRequest.PLAYBACK_TYPE type = req.getType();
        if(type==PlaybackRequest.PLAYBACK_TYPE.PLAY){
            //
        }else if(type==PlaybackRequest.PLAYBACK_TYPE.PLAY_SONGITEM){
            play((SongItem)req.object);
        }else if(type==PlaybackRequest.PLAYBACK_TYPE.PAUSE){
            //
        }else if (type==PlaybackRequest.PLAYBACK_TYPE.STOP){
            //
        }
    }

    @Override
    public IBinder onBind(Intent p1)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String cmd = intent.getAction();
        if(cmd != null){
            switch(cmd){
                case "hp":
                    String p = intent.getExtras().getString("hpath");
                    iSong = new SongItem(p);
                    play(iSong);
                    fix282();
                    break;
                case CMD_START:
                    PostMan.getInstance().post(new AudioServiceConnect(this));
                    break;
                case CMD_STOP:
                    //
                    break;
                case CMD_PLAY:
                    if(aupod!=null){
                        resume();
                    }else{
                        // start new song
                    }
                    break;
                case CMD_PAUSE:
                    if(aupod!=null){
                        pause();
                    }
                    break;
                    
                case CMD_PLAY_NEXT:
                    playnext();
                    break;
                case CMD_PLAY_PREV:
                    playPrevious();
                    break;
            }
        }
        return START_NOT_STICKY;
    }
    
    public void getSongList(){
        if(songList==null){
            SongFactory.getInstance().getAll(this, this);
        }else{
            PostMan.getInstance().post(new SongsListReadyEvent(songList));
        }
    }
    
    public static void playSong(Context ctx, String path){
        Intent s = new Intent(ctx, AudioService.class);
        s.setAction("hp");
        s.putExtra("hpath",path);
        ctx.startService(s);
    }

    public static void start(Context ctx){
        try{
        Intent s = new Intent(ctx, AudioService.class);
        s.setAction(CMD_START);
        ctx.startService(s);
        }catch(Exception e){
            Utils.log(e.getMessage());
        }
        //*/
    }
    
    public static void end(Context ctx){
        Intent s = new Intent(ctx, AudioService.class);
        s.setAction(CMD_STOP);
        ctx.startService(s);
    }
    
    private void updateNotif(Boolean playing){
        startForeground(876, Noteaf.get(this, currentSong, playing));
    }
    
    public void play(SongItem si){
        //isPlaying=true;
        currentSong = si;
        aupod.play(currentSong);
        updateNotif(true);
        //APEvents.getInstance().postSongResume(si);
        PostMan.getInstance().post(new SongResumeEvent(si));
        requestAudioFocus();
        
    }
    
    public void pause(){
        aupod.pause();
       // isPlaying=false;
        updateNotif(false);
        stopForeground(false);
        //APEvents.getInstance().postSongPause();
        PostMan.getInstance().post(new SongPauseEvent(currentSong));
        
    }
    
    public void resume(){
        aupod.resume();
       // isPlaying=true;
        updateNotif(true);
        //APEvents.getInstance().postSongResume(currentSong);
        PostMan.getInstance().post(new SongResumeEvent(currentSong));
        requestAudioFocus();
        
    }
    
    public void playPause(){
        if(aupod.isPlaying()){
            pause();
        }else{
            if(aupod.isActive){
                resume();
            }else{
                play(currentSong);
                //
            }
        }
    }
    
    public boolean isPlaying(){
        return aupod.isPlaying();
    }
    
    public void playnext(){
        int id = songList.indexOf(currentSong);
        id++;
        if(id<= songList.size()-1){
            currentSong = songList.get(id);
            play(currentSong);
        }
    }
    
    public void playPrevious(){
        int id = songList.indexOf(currentSong);
        id--;
        if(id<0){
            id=0;
        }
        currentSong = songList.get(id);
        play(currentSong);
    }
    
    public int getDuration(){
        return aupod.getDuration();
    }
    
    public int getCurrentPosition(){
        return aupod.getCurrentPosition();
    }
    
    public SongItem getCurrentSong(){
        return currentSong;
    }
    
    public void stop(){
        aupod.stop();
        stopForeground(true);
    }
    
    public void onSongPlaying(SongItem si){
        currentSong = si;
        //APEvents.getInstance().postSongResume(si);
    }
    
    @Override
    public void onSongsReady(final List<SongItem> slist){
        mHandle.postDelayed(new Runnable(){
            @Override
            public void run(){
                songList = slist;
                // todo: get this from prefs
                currentSong = songList.get(0);
                fix282();
                
                //APEvents.getInstance().postSongListReady(slist);
                PostMan.getInstance().post(new SongsListReadyEvent(slist));
                
            }
        },1);
    }

    @Override
    public void onGetSongsError(final String reason)
    {
        mHandle.postDelayed(new Runnable(){
            @Override
            public void run(){
                Utils.toast(AudioService.this, reason);
            }
        },1);
    }
    
    void registerHeadsetPlug(){
        IntentFilter hpf = new IntentFilter(AudioManager.ACTION_HEADSET_PLUG);
        registerReceiver(headsetPlugReceiver, hpf);
    }
    
    void unregisterHeadsetPlug(){
        unregisterReceiver(headsetPlugReceiver);
    }
    
    BroadcastReceiver headsetPlugReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context p1, Intent p2)
        {
            resume();
        } 
    };
    
    void registerNoisy(){
        IntentFilter nf = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(noisyReceiver, nf);
    }
    
    void unregisterNoisy(){
        unregisterReceiver(noisyReceiver);
    }
    
    BroadcastReceiver noisyReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context p1, Intent p2)
        {
            pause();
        }
    };
    
    void requestAudioFocus(){
        if(audioFocusGranted){
            return;
        }
        int result = am.requestAudioFocus(audioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            audioFocusGranted=true;
        }else{
            //
        }
    }
    
    void dropAudioFocus(){
        am.abandonAudioFocus(audioFocusListener);
        audioFocusGranted=false;
    }
    
    AudioManager.OnAudioFocusChangeListener audioFocusListener = new AudioManager.OnAudioFocusChangeListener(){
        @Override
        public void onAudioFocusChange(int state)
        {
            switch(state){
                case AudioManager.AUDIOFOCUS_GAIN:
                    if(audioFocusGranted){
                        resume();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                    //
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                    //
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    pause();
                    dropAudioFocus();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //
                    break;
                case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                    pause();
                    break;
            }
        }
    };
    
    private void fix282(){
        if(songList!=null && iSong!=null){
            for(SongItem si : songList){
                if(si.path.equalsIgnoreCase(iSong.path.replace("file://",""))){
                    currentSong = si;
                    updateNotif(aupod.isPlaying());
                    break;
                }
            }
        }
    }

    @Override
    public void onDestroy()
    {
        dropAudioFocus();
        unregisterHeadsetPlug();
        unregisterNoisy();
        //
        super.onDestroy();
    }
    
    
    
}
