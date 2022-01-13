package sleepchild.aupod22.library;

import android.content.*;
import java.util.*;
import java.util.concurrent.*;
import sleepchild.aupod22.utils.*;
import android.os.*;
import sleepchild.aupod22.*;

public class SongLibrary{
    // 
    private static volatile SongLibrary deftInstance;
    
    /*
    * main list of songs
    * this is the list we will use to resolve things like artists, albums, genres etc.
    *
    * note: thus we need to ensure it is always up-to-date
    *        prehaps by implementing some kind of directory/file watcher or a listener to mediastore updates
    *
    */
    private List<String> songlist = new ArrayList<>();
    
    //
    private List<String> artistList = new ArrayList<>();
    
    //
    private List<String> albumList = new ArrayList<>();
    
    private ExecutorService worker;
    
    private Handler handle = new Handler(Looper.getMainLooper());
    
    /*
    *  track if ::discover() has been called at least once
    */
    private volatile boolean discoverdOnce=false;
    private static boolean isDiscovering=false;
    
    private SongLibrary(){
        worker = Executors.newFixedThreadPool(2);
    }
    
    // ensure a single instance of this class (to some degree)
    public static SongLibrary get(){
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
    
    /*
    *  Begin a query for songs on the device from android.media.MediaStore.
    * 
    *  Idea--ly, this MUST be the first method to be called.
    *  and it should be done so as early as posible.
    *  for instance in the main/starting Activity's onstart method
    *      or if the android.app.Application class is used, it's onCreate method is a perhaps the best place.
    *
    */
    public void discover(final Context ctx){
        isDiscovering = true;
        worker.submit(new Runnable(){
            public void run(){
                //final List<test.MP3> l = new test().findmp3("/sdcard/");
                //+l.size());
                handle.post(new Runnable(){
                    public void run(){
                        //Utils.toast(ctx, ""+l.size());
                    }
                });
            }
        });
        discoverdOnce=true;
    }
    
    // returns the main list of songs found
    public void getAllSongs(){
        //
        //return songlist;
    }
    
    // returns a list of Artists.
    public void getAllArtists(){
        //
        //return artistList;
    }
    
    // retuns a list of albums found
    public void getAllAlbums(){
        //
        //return albumList;
    }
    
    // return genres
    
}
