package sleepchild.aupod22;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import android.provider.*;
import android.content.*;
import android.net.*;
import com.mpatric.mp3agic.*;
import android.graphics.*;
import android.os.*;

public class SongFactory
{
    private static volatile SongFactory deftInstance;
    private ExecutorService worker;
    List<FactoryEvents> eListeners = new ArrayList<>();
    
    private SongFactory(){
        worker = Executors.newFixedThreadPool(3);
    }
    
    public static SongFactory get(){
        SongFactory instance = deftInstance;
        if(instance==null){
            synchronized(SongFactory.class){
                instance = SongFactory.deftInstance;
                if(instance==null){
                    instance = SongFactory.deftInstance = new SongFactory();
                }
            }
        }
        return instance;
    }
    
    public SongFactory registerEventListener(FactoryEvents listener){
        eListeners.add(listener);
        return this;
    }
    
    public SongFactory unregisterEventListener(FactoryEvents listener){
        eListeners.remove(listener);
        return this;
    }
    
    public void getSongs(Context ctx){
        worker.submit(new GetSongListTask(ctx));
    }
    
    public static class GetSongListTask implements Runnable{
        List<SongItem> itemlist = new ArrayList<>();
        Context ctx;
        
        public GetSongListTask(Context ctx){
            this.ctx = ctx;
        }
        
        @Override
        public void run(){
            try{
                //
                List<SongItem> sl = SongRetreiverCursor.get(ctx);
                itemlist.addAll(sl);      
                SongFactory.get().notifyComplete(itemlist);
            }catch (Exception e){
                SongFactory.get().notifyError(e.getMessage());
            }
        } 
    }
    
    //
    public interface FactoryEvents{
        public void onGetSongsResult(List<SongItem> songlist);
        public void onGetSongsError(String message);
    }
    
    private void notifyComplete(List<SongItem> list){
        for(FactoryEvents l : eListeners){
            l.onGetSongsResult(list);
        }
    }
    
    private void notifyError(String message){
        for(FactoryEvents l : eListeners){
            l.onGetSongsError(message);
        }
    }
    
    ///
    public void updateSong(SongItem si, UCB cb){
        worker.submit(new SongUpdateTask(si,cb));
    }
    
    private class SongUpdateTask implements Runnable{
        SongItem si;
        UCB cb;
        
        public SongUpdateTask(SongItem si, UCB cb){
            this.si = si;
            this.cb = cb;
        }

        @Override
        public void run()
        {
            si = r(si);
            if(cb!=null){
                cb.onResult(si);
            }
        }
        
        private SongItem r(SongItem si){
            try{
                Mp3File m = new Mp3File(si.path);
                ID3v2 v2 =  m.getId3v2Tag();
                if(v2!=null){
                    si.title = v2.getTitle();
                    si.artist = v2.getArtist();
                    byte[] art = v2.getAlbumImage();
                    if(art!=null){
                        si.icon = BitmapFactory.decodeByteArray(art,0,art.length);
                    }
                }else{
                    ID3v1 v1 = m.getId3v1Tag();
                    if(v1!=null){
                        si.title = v1.getTitle();
                        si.artist = v1.getArtist();
                        //
                    }

                }

            }
            catch (UnsupportedTagException e)
            {}
            catch (InvalidDataException e)
            {}
            catch (IOException e)
            {}
            
            if(si.title==null || si.title.isEmpty()){
                String t = new File(si.path).getName();
                si.title = t.substring(0, t.lastIndexOf("."));
            }
            if(si.artist==null || si.artist.isEmpty()){
                si.artist = "<unknown>";
            }

            return si;
        }
          
    }
    public static interface UCB{
        public void onResult(SongItem si);
    }
}
