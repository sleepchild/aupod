package sleepchild.aupod22.utils;
import java.util.*;
import android.os.*;
import java.util.concurrent.*;
import sleepchild.aupod22.models.*;

public class SearchHelper
{
    Handler handle = new Handler(Looper.getMainLooper());
    ExecutorService worker;
    Thread mThread;
    SearchTask currentTask;
    
    private static SearchHelper deft;
    
    private SearchHelper(){
        worker = Executors.newSingleThreadExecutor();
    }
    
    public static SearchHelper get(){
        SearchHelper inst = deft;
        if(inst==null){
            synchronized(SearchHelper.class){
                inst = SearchHelper.deft;
                if(inst==null){
                    inst = SearchHelper.deft = new SearchHelper();
                }
            }
        }
        return inst;
    }
    
    public void search(final String query, List<SongItem> songs, ResultCallback cb){
        if(currentTask!=null){
            //currentTask.setAbort();
        }
        currentTask = new SearchTask(query, songs, cb);
        worker.submit(currentTask);
    }
    
    class SearchTask implements Runnable {
        private String squery;
        private ResultCallback scb;
        private List<SongItem> slist;
        private boolean sabort = false;
        
        public SearchTask(String query, List<SongItem> list, ResultCallback cb){
            squery = query;
            slist = list;
            scb = cb;
        }
        
        public void setAbort(){
            sabort = true;
        }

        @Override
        public void run(){
            final List<SearchResult> res = new ArrayList<>();
            
            for(SongItem si : slist){
                if(sabort){ break; }
                if(si.title.toLowerCase().contains(squery.toLowerCase())){
                    res.add(new SearchResult(si.title, 0, si));
                }else if(si.artist.toLowerCase().contains(squery.toLowerCase())){
                    res.add(new SearchResult(si.title, 2, si));
                }else if(si.album.toLowerCase().contains(squery.toLowerCase())){
                    res.add(new SearchResult(si.title, 3, si));
                }
            }
            
            if(!sabort && scb!=null){
                handle.post(new Runnable(){
                    public void run(){
                        scb.onSearchResult(res);
                    }
                });
            }
        }
        
    }
    
    
    public static interface ResultCallback{
        public void onSearchResult(List<SearchResult> list);
    }
}
