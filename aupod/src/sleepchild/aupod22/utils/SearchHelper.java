package sleepchild.aupod22.utils;
import java.util.*;
import android.os.*;
import java.util.concurrent.*;

public class SearchHelper
{
    Handler handle = new Handler(Looper.getMainLooper());
    ExecutorService worker;
    Thread mThread;
    
    private static SearchHelper deft;
    private SearchHelper(){
        worker = Executors.newSingleThreadExecutor();
    }
    
    private static SearchHelper get(){
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
    
    
    // todo: find a way to stop the thread when a new query comes in
    private void search_(final String query, ResultCallback cb){
        worker.submit(new SearchTask(query, cb));
    }
    
    private class SearchTask implements Runnable{
        ResultCallback cb;
        String sQuery;
        public SearchTask(String query, ResultCallback cb){
            this.sQuery = query;
            this.cb = cb;
        }
        
        public void run(){
            //
            final List<SearchResult> l2 = new ArrayList<>();
            // search logic
            //
            handle.postDelayed(new Runnable(){
                public void run(){
                    cb.onSearchResult(l2);
                }
            },1);
        }
    }
    
    public static void serch(String query, ResultCallback cb){
        get().search_(query, cb);
    }
    
    public static interface ResultCallback{
        public void onSearchResult(List<SearchResult> list);
    }
    
}
