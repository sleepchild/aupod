package sleepchild.aupod22;
import android.content.*;
import android.media.*;
import java.io.*;
import mrtubbs.postman.*;
import sleepchild.aupod22.postmanmodels.*;

public class AudioPlayer{
    
    private MediaPlayer mPlayer;
    public boolean isActive=false;
    private SongItem currentsong;
    
    public AudioPlayer(){
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
                PostMan.getInstance().post(new SongCompletionEvent(currentsong));
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
    
    public void setSong(SongItem si){
        stop();
        currentsong = si;
        try
        {
            mPlayer.setDataSource(si.path);
            mPlayer.prepare();
        }
        catch (IllegalStateException e)
        {}
        catch (IOException e)
        {}
        catch (SecurityException e)
        {}
        catch (IllegalArgumentException e)
        {}
    }
    
    public SongItem getCurrentSong(){
        return currentsong;
    }
    
    // play the specified song
    public void play(SongItem si){
        setSong(si);
        resume();
    }
    
    // resume playing current song
    public void resume(){
        mPlayer.start();
        isActive = true;
        PostMan.getInstance().post(new SongResumeEvent(currentsong));
    }
    
    // pause the current song
    public void pause(){
        if(mPlayer!=null && mPlayer.isPlaying()){
            mPlayer.pause();
        }
        PostMan.getInstance().post(new SongPauseEvent(currentsong));
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
    
    public void seekTo(int pos){
        mPlayer.seekTo(pos);
    }
    
}
