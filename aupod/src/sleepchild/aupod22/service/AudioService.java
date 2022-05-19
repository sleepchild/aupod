package sleepchild.aupod22.service;
import android.app.*;
import android.os.*;
import android.content.*;
import sleepchild.aupod22.models.*;
import sleepchild.aupod22.*;
import android.media.*;
import sleepchild.aupod22.library.*;
import sleepchild.aupod22.activity.*;
import java.util.*;
import java.io.*;
import sleepchild.aupod22.utils.*;
import sleepchild.aupod22.recievers.*;
import android.media.session.*;
import android.widget.*;

public class AudioService extends Service{

    //
    private static String TAG = "AudioService";
    public static final String CMD_START = "sleepchild.aupod.service.cmd.start_service";
    public static final String CMD_END = "sleepchild.aupod.service.cmd.end.exit";
    public static final String CMD_MEDIA_BUTTON = "sleepchild.aupod.service.CMD_MEDIA_BUTTON";
    //
    public static final String CMD_PAUSE = "sleepchild.aupod.service.CMD_PAUSE";
    public static final String CMD_PLAY = "sleepchild.aupod.service.CMD_PLAY";
    public static final String CMD_PLAY_NEXT = "sleepchild.aupod.service.CMD_PLAY_NEXT";
    public static final String CMD_PLAY_PREV = "sleepchild.aupod.service.CMD_PLAY_PREV";
    //
    private final int NID = 5374;
    //
    private Context ctx;
    private MediaPlayer mPlayer;
    private AudioManager am;
    private PowerManager.WakeLock wakelock;
    private ComponentName mediaButtonComponent;
    SPrefs prefs;
    
    
    private SongItem currentSong;
    private Handler handle = new Handler();
    
    private boolean restartCurrentOnPrev = true;
    private boolean songset = false;
    boolean recieversRegistered = false;
    
    private List<SongItem> songlist = new ArrayList<>();
    
    @Override
    public IBinder onBind(Intent p1) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        ctx = getApplicationContext();
        prefs = new SPrefs(ctx);
        restartCurrentOnPrev = prefs.getRestartSongOnPrev();
        initMPlayer();
        //
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        wakelock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        //
        App.get().registerAudiosService(this);
        
    }

    @Override
    public void onDestroy()
    {
        unregisterRecievers();
        super.onDestroy();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(intent!=null){
            String cmd = intent.getAction();
            switch(cmd){
                case CMD_START:
                    cmd_start();
                    break;
                case CMD_END:
                    cmd_end();
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
                    handleMediabtn();
                    break;
            }
        }
        return START_NOT_STICKY;
    }
    
    public static void end(Context ctx){
        Intent si = new Intent(ctx, AudioService.class);
        si.setAction(CMD_END);
        ctx.startService(si);
    }
    
    public static void connect(Context ctx, ConnectionListener listener){
        App.get().setConnl(listener);
        Intent si = new Intent(ctx, AudioService.class);
        si.setAction(CMD_START);
        ctx.startService(si);
    }
    
    public static void playFromIntent(Context context, String songpath){
        //
    }
    
    public void onActivityDestroyed(){
        if(!isPlaying()){
            cmd_end();
        }
    }
    
    public void getSongsList(){
        SongLibrary.getInstance().getAllSongs(this, getsonglistresult);
    }
    
    // sets and plays the specified song
    public void playSong(SongItem si){
        if(si==null || mPlayer==null){
            return;
        }
        mPlayer.stop();
        mPlayer.reset();
        currentSong = si;
        prefs.saveLastPath(si.path);
        try
        {
            mPlayer.setDataSource(si.path);
            songset = true;
            mPlayer.prepare();
            resume();
            
            APEvents.getInstance().postSongChangeEvent(currentSong);
            APEvents.getInstance().postPlayStateEvent_start();
        }
        catch (IllegalArgumentException e)
        {}
        catch (IllegalStateException e)
        {}
        catch (SecurityException e)
        {}
        catch (IOException e)
        {}
        
    }
    
    // sets the specified song as current but does bot play it
    public void setSong(SongItem si){
        currentSong = si;
        try
        {
            mPlayer.setDataSource(si.path);
            mPlayer.prepare();
            songset = true;
            APEvents.getInstance().postSongChangeEvent(currentSong);
        }
        catch (IllegalArgumentException e){}
        catch (IllegalStateException e){}
        catch (IOException e){}
        catch (SecurityException e){}
    }
    
    public void playPause(){
        if(currentSong!=null){
            if(isPlaying()){
                pause();
            }else{
                if(songset){
                    resume();
                }else{
                    playSong(currentSong);
                }
            }
        }else{
            if(!songlist.isEmpty()){
                currentSong = songlist.get(0);
                if(currentSong!=null){
                    playSong(currentSong);
                }else{
                    // maybe the list is still loading.
                }
            }
        }
        
    }
    
    public void pause(){
        mPlayer.pause();
        //
        stopNotification();
        releaseWakeLock();
        APEvents.getInstance().postPlayStateEvent_pause();
    }
    
    public void resume(){
        try{
            mPlayer.start();
            APEvents.getInstance().postPlayStateEvent_start();
            //
            showNotification();
            registerRecievers();
            
        }catch(Exception e){
            //
            toast(e.getMessage());
        }
    }
    
    public void stop(){
        mPlayer.stop();
        mPlayer.reset();
        //
        stopNotification();
        unregisterRecievers();
        APEvents.getInstance().postPlayStateEvent_stop();
    }
    
    public boolean playNext(){
        if(songlist.isEmpty()){
            return false;
        }
        boolean songChanged=false;
        int idx = songlist.indexOf(currentSong);
        int m = songlist.size()-1;
        idx++;
        if(idx>m){
            idx=m;
        }else{
            mPlayer.stop();
            mPlayer.reset();
            currentSong = songlist.get(idx);
            playSong(currentSong);
            songChanged=true;
        }
        return songChanged;
    }
    
    public void playPrev(){
        if(songlist.isEmpty()){
            return;
        }
        int idx = songlist.indexOf(currentSong);
        idx--;
        if(idx<0){
            idx = 0;
        }
        boolean rest= false;
        
        if(restartCurrentOnPrev){
            int ct = mPlayer.getCurrentPosition();
            if(ct>6000){
                rest=true;
            }
        }
        
        if(rest){
            mPlayer.seekTo(0);
        }else{
            mPlayer.stop();
            mPlayer.reset();
            currentSong = songlist.get(idx);
            playSong(currentSong);
        }
    }
    
    public boolean isPlaying(){
        return mPlayer.isPlaying();
    }
    
    public SongItem getCurrentSong(){
        return currentSong;
    }
    
    public int getCurrentPosition(){
        return mPlayer.getCurrentPosition();
    }
    
    public int getDuration(){
        return mPlayer.getDuration();
    }
    
    public void seekTo(int pos){
        
        mPlayer.seekTo(pos);
    }
    
    public int getCurrentSongIndex(){
        if(currentSong==null){
            return 0;
        }
        if(!songlist.contains(currentSong)){
            return 0;
        }
        return songlist.indexOf(currentSong);
    }
    
    public void deleteSong(SongItem si){
        int m = songlist.size() - 1;
        if(currentSong==si){
            int i = getCurrentSongIndex();
            int n = i+1;
            if(n>m){
                n = i-1;
            }
            boolean ply = isPlaying();
            currentSong = songlist.get(n);
            playSong(currentSong);
            if(!ply){
                pause();
            }
            APEvents.getInstance().postSongChangeEvent(currentSong);
        }
        Utils.delete(si.path);
        getSongsList();
        Utils.toast(this,"song deleted!");
    }
    
    public void requestPlaystateUpdate(){
        if(mPlayer!=null){
            if(songset && currentSong!=null){
                APEvents.getInstance().postSongChangeEvent(currentSong);
            }
            
            if(mPlayer.isPlaying()){
                APEvents.getInstance().postPlayStateEvent_start();
            }
        }
    }
    
    ////////////////////
    ////  private  ////
    ///////////////////
    
    
    private SongLibrary.ResultCallback getsonglistresult = new SongLibrary.ResultCallback(){
        @Override
        public void onResult(final Object result){
            handle.post(new Runnable(){
                public void run(){
                    List<SongItem> sl = (List<SongItem>) result;
                    _ongetsongslist(sl);// delegate because 'this' refers to sth else in here.
                }
            });
        }
    };
    
    private void _ongetsongslist(List<SongItem> sl){
        boolean currentSongUpdated = false;
        boolean listChanged=false;
        
        if(songlist!=null){
            if(sl.containsAll(songlist) && songlist.containsAll(sl)){
                //
            }else{
                songlist = sl;
                listChanged = true;
            }
        }else{
            songlist = sl;
        }
        
        if(currentSong!=null){
            //if(listChanged){
                for(SongItem si : songlist){
                    if(si.path.equalsIgnoreCase(currentSong.path)){
                        currentSong = si;
                        currentSongUpdated = true;
                        break;
                    }
                }
            //}
            
        }else{
            String p = prefs.getLastPath();
            if(Utils.exists(p)){
                for(SongItem si : songlist){
                    if(si.path.equalsIgnoreCase(p)){
                        currentSong = si;
                        break;
                    }
                }
                if(currentSong==null && !songlist.isEmpty()){
                    currentSong = songlist.get(0);
                }
            }else{
                if(!songlist.isEmpty()){
                    currentSong = songlist.get(0);
                }
            }
            //
        }
        
        if(!songset){
            if(currentSong!=null){
                setSong(currentSong);
                //mPlayer.start();
                //mPlayer.stop();
            }
        }
        
        if(songlist!=null){
            APEvents.getInstance().postSongsListUpdated(songlist);
        }
        //
        if(currentSong!=null){
            APEvents.getInstance().postSongChangeEvent(currentSong);
        }
    }
    
    private void cmd_start(){
        ConnectionListener l = App.get().getConnl();
        if(l!=null){
            l.onAudioServiceConnect(this);
        }
        // update notification theme
    }
    
    private void cmd_end(){
        
        stop();
        stopForeground(false);
        stopForeground(true);
        
        mPlayer.release();
        mPlayer = null;
        
        unregisterRecievers();
        
        stopSelf();
        
        XApp.exit();
        //
    }
    
    private void requestAudioFocus(){
        int af = am.requestAudioFocus(audioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if(af!=AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            toast("faild to get audio focus!");
        }
    }
    
    private void dropAudioFocus(){
        am.abandonAudioFocus(audioFocusListener);
    }
    
    boolean playingBeforePause=false;
    AudioManager.OnAudioFocusChangeListener audioFocusListener = new AudioManager.OnAudioFocusChangeListener(){
        @Override
        public void onAudioFocusChange(int state){
            switch(state){
                case AudioManager.AUDIOFOCUS_GAIN:
                    //resume();
                    //
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                    //resume();
                    //
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    playingBeforePause = isPlaying();
                    pause();
                    unregisterRecievers();
                    dropAudioFocus();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    playingBeforePause = isPlaying();
                    pause();
                    unregisterRecievers();
                    dropAudioFocus();
                    break;
            }
        }
    };
    
    // todo: try everything
    private void registerRecievers(){
        if(recieversRegistered){
            //return;
        }
        aquireWakeLock();
        requestAudioFocus();
        registerMediaButton();
        recieversRegistered = true;
        //
    }
    
    private void unregisterRecievers(){
        unRegisterMediaButton();
        dropAudioFocus();
        releaseWakeLock();
        recieversRegistered = false;
    }
    
    private void registerMediaButton(){
        try{
            if(mediaButtonComponent == null){
                mediaButtonComponent = new ComponentName(this, ButtonReciever.class.getName());
            }
            am.registerMediaButtonEventReceiver(mediaButtonComponent);
        }catch(Exception e){}
    }
    
    private void unRegisterMediaButton(){
        try{
            am.unregisterMediaButtonEventReceiver(mediaButtonComponent);
        }catch(Exception e){}
    }
    
    /// headsethook ~~ media button
    Runnable btnSingle = new Runnable(){
        public void run(){
            playPause();
            btnSinglepress = false;
        }
    };
    
    boolean btnSinglepress=false;;
    
    private void handleMediabtn(){
        if(btnSinglepress){
            handle.removeCallbacks(btnSingle);
            btnSinglepress = false;
            playNext();
        }else{
            btnSinglepress = true;
            handle.postDelayed(btnSingle, 280);
        }
    }
    
    private void aquireWakeLock(){
        try{
            if(wakelock!=null){
                wakelock.acquire();
                //Utils.toast(ctx, "wakeful: "+wakelock.isHeld());
            }
        }catch(Exception e){}
    }
    
    private void releaseWakeLock(){
        try{
            if(wakelock!=null){
                if(wakelock.isHeld()){
                    wakelock.release();
                }
            }
        }catch(Exception e){}
    }
    
    public void updateNotif(){}
     
    private void showNotification(){
        startForeground(NID, Notific.get(this, currentSong, true));
    }
    
    private void stopNotification(){
        startForeground(NID, Notific.get(this, currentSong, false));
    }
    
    private void initMPlayer(){
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer p1){
                playNext();
            }
        });
    }
    
    private void toast(String msg){
        Utils.toast(ctx,msg);
    }
    
    public interface ConnectionListener{
        public void onAudioServiceConnect(AudioService service);
    }
    
    
    
}
