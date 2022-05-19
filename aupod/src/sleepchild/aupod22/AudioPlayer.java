package sleepchild.aupod22;
import android.media.*;
import java.io.*;

/*
*/
public class AudioPlayer implements
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnSeekCompleteListener
{
    
    private MediaPlayer player;
    PlayerEventListener ev;
    
    public AudioPlayer(PlayerEventListener listener){
        ev = listener;
        initPlayer();
    }
    
    private void initPlayer(){
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnErrorListener(this);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnSeekCompleteListener(this);
        
    }
    
    
    public void setSong(String path){
        stop();
        try
        {
            player.setDataSource(path);
            player.prepare();
            ev.onSongPrepared();
        }
        catch (IOException e)
        {}
        catch (SecurityException e)
        {}
        catch (IllegalArgumentException e)
        {}
        catch (IllegalStateException e)
        {}
    }
    
    public void setSongAsync(String path){
        stop();
        try
        {
            player.setDataSource(path);
            player.prepareAsync();
        }
        catch (IOException e)
        {}
        catch (SecurityException e)
        {}
        catch (IllegalArgumentException e)
        {}
        catch (IllegalStateException e)
        {}
    }
    
    public void resume(){
        player.start();
        ev.onPlaybackStarted();
    }
    
    public void pause(){
        player.pause();
        ev.onPlaybackPaused();
    }
    
    public void stop(){
        player.stop();
        ev.onPlaybackStopped();
    }

    @Override
    public void onCompletion(MediaPlayer p1)
    {
        ev.onSongFinished();
    }

    @Override
    public boolean onError(MediaPlayer p1, int p2, int p3)
    {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer p1)
    {
        ev.onSongPrepared();
    }

    @Override
    public void onSeekComplete(MediaPlayer p1)
    {
        //
    }
    
    public static interface PlayerEventListener{
        public void onPlaybackStarted();
        public void onPlaybackPaused();
        public void onPlaybackStopped();
        
        public void onSongPrepared();
        public void onError();
        public void onSongFinished();
    }
    
}
