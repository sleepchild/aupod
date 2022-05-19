package sleepchild.aupod22.library;

import android.content.*;
import java.util.*;
import java.util.concurrent.*;
import sleepchild.aupod22.utils.*;
import android.os.*;
import sleepchild.aupod22.*;
import sleepchild.aupod22.models.*;

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
    private List<SongItem> songlist = new ArrayList<>();
    
    /**
        List of music artists; this list is generated by:
         - looping thru the songlist
         - get artist for each song and add to a hashmap
        see the getArtistsInt() method
    **/
    private List<ArtistItem> artistList = new ArrayList<>();
    
    /*
    */
    private List<String> albumList = new ArrayList<>();
    
    private ExecutorService worker;
    
    private Handler handle = new Handler(Looper.getMainLooper());
    
    private static boolean isDiscovering=false;
    
    private ResultCallback mcb;
    
    private SongLibrary(){
        worker = Executors.newFixedThreadPool(2);
    }
    
    public static SongLibrary getInstance(){
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
    
    public void start(Context ctx){
        //
    }
    
    /*
    *  Begin a query for songs on the device from android.media.MediaStore.
    */
    public void update(final Context ctx){
        isDiscovering = true;
        worker.submit(new Runnable(){
            public void run(){
                final List<SongItem> sl = SongRetreiverCursor.get(ctx);
                //*
                handle.post(new Runnable(){
                    public void run(){
                        songlist.clear();
                        songlist.addAll(sl);
                        
                        if(mcb!=null){
                            mcb.onResult(songlist);
                        }
                    }
                });
                //*/
                //SongInfoUpdater.update(sl);
            }
        });
    }
    
    // returns the list of all songs found on the device
    public void getAllSongs(Context ctx, ResultCallback cb){
        mcb = cb;
        update(ctx);
        //cb.onResult(songlist);
    }
    
    public List<SongItem> getSongsByArtist(String artist){
        //
        return null;
    }
    
    // gets a list of Artists.
    public void getAllArtists(ResultCallback <List<ArtistItem>> cb){
         ge(cb);
    }
    
    private void ge(ResultCallback <List<ArtistItem>> cb){
        artistList.clear();
        HashMap<String, ArtistItem> amap = new HashMap<>();
        for(SongItem si : songlist){
            ArtistItem itm = amap.get(si.artist);
            if(itm==null){
                itm = new ArtistItem();
                itm.name = si.artist;
                amap.put(si.artist, itm);
            }
            //
            itm.songcount++;
        }
        for(ArtistItem ai : amap.values()){
            artistList.add(ai);
        }
        Collections.sort(artistList, new Comparator<ArtistItem>(){
            public int compare(ArtistItem l, ArtistItem r){
                
                return l.name.compareToIgnoreCase(r.name);
            }
        });
        cb.onResult(artistList);
    }
    
    public ArtistItem getArtistsByName(String name){
        return null;
    }
    
    // retuns a list of albums.
    public void getAllAlbums(ResultCallback cb){
        //return albumList;
    }
    
    public void getQueue(ResultCallback cb){
        //
    }
    
    public void getPlaylists(ResultCallback cb){
        //
    }
    
    
    public static interface ResultCallback<T>{
        public void onResult(T result);
    }
    
    
}
