package sleepchild.aupod22;

import android.content.*;
import java.util.*;

public class SongLibrary{
    // 
    private static volatile SongLibrary deftInstance;
    // main list of songs
    // this is the list we will use to resolve things like
    //// artists, albums, genres etc.
    // so we need to ensure it is always up-to-date
    private List<String> songlist = new ArrayList<>();
    
    private SongLibrary(){}
    
    // ensure a singleton instance of SongLibrary class process-wide
    public SongLibrary get(){
        SongLibrary inst = deftInstance;
        if(inst==null){
            synchronized(SongLibrary.class){
                inst = SongLibrary.deftInstance;
                if(inst==null){
                    inst = SongLibrary.deftInstance = new SongLibrary();
                }
            }
        }
        return inst;
    }
    
    // begin a query for songs on the device from MediaStore
    public void discover(Context ctx){
        //
    }
    
    // returns the main list of songs found
    public void getAllSongs(){
        // 
    }
    
    // returns a list of
    public void getAllArtists(){
        //
    }
    
    public void getAllAlbums(){
        //
    }
    
}
