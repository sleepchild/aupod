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
    private void search_(final String query, List<SongItem> songs, ResultCallback cb){
        worker.submit(new SearchTask(query, songs, cb));
    }
    
    private class SearchTask implements Runnable{
        ResultCallback cb;
        String squery;
        List<SongItem> slist;
        public SearchTask(String query,List<SongItem> songs, ResultCallback cb){
            this.squery = query.toLowerCase();
            this.cb = cb;
            this.slist = songs;
        }
        
        public void run(){
            //
            final List<SearchResult> l2 = new ArrayList<>();
            Set<SongItem> rest = new HashSet<>();
            Set<SongItem> arts = new HashSet<>();
            Set<SongItem> albu = new HashSet<>();
            // search
            String[] p = squery.split(" ");
            int e = p.length;
            //
            for(SongItem si : slist){
                String t=si.title.toLowerCase(),
                    a=si.artist.toLowerCase(),
                    b=si.album.toLowerCase();
                int i=0;
                SearchResult r=null;
                for(String q : p){
                    if(i==0){
                        if(t.contains(q)){
                            rest.add(si);
                        }
                        
                        if(a.contains(q)){
                            arts.add(si);
                        }
                        
                        if(b.contains(q)){
                            albu.add(si);
                        }
                    }else{
                        //*
                        if(!rest.contains(si) && !arts.contains(si) && !albu.contains(si)){
                            r=null;
                            break; //
                        }
                        //*/
                        //
                        if(t.contains(q)||a.contains(q)||b.contains(q)){
                            
                        }else{
                            rest.remove(si);
                            arts.remove(si);
                            albu.remove(si);
                        }
                    }
                    
                    if(i==e-1){// final itteration
                        if(rest.contains(si)){
                            SearchResult re = new SearchResult(si.title, 0, si);
                            l2.add(re);
                        }
                        if(arts.contains(si)){
                            SearchResult re = new SearchResult(si.title, 1, si);
                            l2.add(re);
                        }
                        if(albu.contains(si)){
                            SearchResult re = new SearchResult(si.title, 2, si);
                            l2.add(re);
                        }
                        
                    }
                    i++;
                }
            }
            /*
            Collections.sort(l2, new Comparator<SearchResult>(){
                @Override
                public int compare(SearchResult p1, SearchResult p2)
                {
                    if(p1.type==0){
                        return -1;
                    }else if(p1.type==1){
                        return 0;
                    }
                    return 1;
                }
            });
            //*/
            // send result
            handle.postDelayed(new Runnable(){
                public void run(){
                    cb.onSearchResult(l2);
                }
            },1);
        }
    }
    
    public static void serch(String query,List<SongItem> songs, ResultCallback cb){
        get().search_(query, songs, cb);
    }
    
    public static interface ResultCallback{
        public void onSearchResult(List<SearchResult> list);
    }
}
