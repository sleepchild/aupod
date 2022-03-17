package sleepchild.aupod22;
import java.util.*;
import sleepchild.aupod22.models.*;

public class APEvents{
   
    private static volatile APEvents deft;
    
    private List<PlaybackStateListener> playStateListeners = new ArrayList<>();
    private List<SongListUpdateListener> songsupdateListener = new ArrayList<>();
    
    private APEvents(){}
    
    public static APEvents getInstance(){
        APEvents inst = deft;
        if(inst==null){
            synchronized(APEvents.class){
                inst = APEvents.deft;
                if(inst==null){
                    inst = APEvents.deft = new APEvents();
                }
            }
        }
        return inst;
    }
    
    public void addPlaybackEventListener(PlaybackStateListener l){
        playStateListeners.add(l);
    }
    
    public void removePlaybackEventListener(PlaybackStateListener l){
        playStateListeners.remove(l);
    }
    
    public void addSongsListUpdateListener(SongListUpdateListener listener){
        songsupdateListener.add(listener);
    }
    
    public void removeSongsListUpdateListener(SongListUpdateListener listener){
        songsupdateListener.remove(listener);
    }
    
    public void postSongsListUpdated(List<SongItem> ulist){
        for(SongListUpdateListener l : songsupdateListener){
            l.onSongsListUpdated(ulist);
        }
    }
    
    public void postPlayStateEvent_start(){
        for(PlaybackStateListener l : playStateListeners){
            l.onPlaybackStart();
        }
    }
    
    public void postPlayStateEvent_pause(){
        for(PlaybackStateListener l : playStateListeners){
            l.onPlaybackPause();
        }
    }
    
    public void postPlayStateEvent_stop(){
        for(PlaybackStateListener l : playStateListeners){
            l.onPlaybackStop();
        }
    }
    
    public void postSongChangeEvent(SongItem newsong){
        for(PlaybackStateListener l : playStateListeners){
            l.onSongChanged(newsong);
        }
    }
    
    public static interface PlaybackStateListener{
        public void onPlaybackStart();
        public void onPlaybackPause();
        public void onPlaybackStop();
        public void onSongChanged(SongItem newsong);
    }
    
    public interface SongListUpdateListener{
        public void onSongsListUpdated(List<SongItem> ulist);
    }
    
    
}
