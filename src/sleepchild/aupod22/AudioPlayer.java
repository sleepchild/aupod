package sleepchild.aupod22;
import android.content.*;
import android.media.*;
import java.io.*;

public class AudioPlayer
{
    private AudioService ctx;
    public MediaPlayer mPlayer;
    public boolean isActive=false;
    SongItem currentsong;
    
    public AudioPlayer(AudioService ctx){
        this.ctx = ctx;
        initPlayer();
    }
    
    private void initPlayer(){
        if(mPlayer!=null){
            try{
                mPlayer.stop();
                mPlayer.release();
            }catch(Exception e){}
        }
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer p1)
            {
                isActive=false;
                
            }
        });
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer p1)
            {
                isActive=true;
            }
        });
        
    }
    
    // play the specified song
    public void play(SongItem si){
        currentsong = si;
        if(mPlayer != null){
            mPlayer.stop();
            mPlayer.reset();
        }
        try
        {
            mPlayer.setDataSource(si.path);
            mPlayer.prepare();
            mPlayer.start();
            isActive=true;
            ctx.onSongPlaying(si);
        }
        catch (SecurityException e)
        {}
        catch (IllegalArgumentException e)
        {}
        catch (IllegalStateException e)
        {}
        catch (IOException e)
        {}
    }
    
    // resume playing current song
    public void resume(){
        mPlayer.start();
        ctx.onSongPlaying(currentsong);
    }
    
    // pause the song
    public void pause(){
        if(mPlayer!=null && mPlayer.isPlaying()){
            mPlayer.pause();
        }
    }
    
    // stop and reset the player
    public void stop(){
        if(mPlayer != null){
            mPlayer.stop();
            mPlayer.reset();  
        }
        isActive=false;
    }
    
    public boolean isPlaying(){
        return mPlayer != null && mPlayer.isPlaying();
    }
    
    public int getDuration(){
        return mPlayer.getDuration();
    }
    
    public int getCurrentPosition(){
        return mPlayer.getCurrentPosition();
    }
}
