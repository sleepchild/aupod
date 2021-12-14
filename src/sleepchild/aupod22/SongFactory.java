package sleepchild.aupod22;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import android.provider.*;
import android.content.*;
import android.net.*;

public class SongFactory
{
    private static volatile SongFactory deftInstance;
    private ExecutorService worker;
    
    private SongFactory(){
        worker = Executors.newFixedThreadPool(3);
    }
    
    public static SongFactory getInstance(){
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
    
    public void getInfo(SongItem si){
        //
    }
    
    public void getAll(Context ctx, FactoryEvents listener){
        worker.submit(new GetSongListTask(ctx, listener));
    }
    
    public void getIcon(Context ctx, Uri uri, ImageHunter.Callback cb){
        worker.submit(new ImageHunter(ctx, uri, cb));
    }
    
    class GetSongListTask implements Runnable{
        FactoryEvents listener;
        List<SongItem> itemlist = new ArrayList<>();
        Context ctx_;
        Exception error;
        
        public GetSongListTask(Context ctx, FactoryEvents listener){
            this.listener = listener;
            this.ctx_ = ctx;
        }
        
        @Override
        public void run(){
            try{
                //
                List<SongItem> sl = SongRetreiverCursor.get(ctx_);
                if(sl==null){
                    //
                    listener.onGetSongsError("null");
                    return;
                }
                listener.onSongsReady(sl);
            }
            catch (Exception e)
            {
                listener.onGetSongsError("getSongError: "+e.getMessage());
            }
        }
        
    }
    
    public interface GetIconCallback{
        public void onDone();
        public void onFailed();
    }
    
    interface FactoryEvents{
        public void onSongsReady(List<SongItem> slist);  
        public void onGetSongsError(final String reason);
    }
}
