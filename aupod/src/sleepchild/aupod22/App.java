package sleepchild.aupod22;
import sleepchild.aupod22.service.*;
import sleepchild.aupod22.activity.*;
import android.os.*;
import java.util.concurrent.*;

public class App
{
    public static int REQUEST_CODE_AUDIOSERVICE = 300;
    public static int REQUEST_CODE_ = 301;
    
    private static volatile App deft;
    
    private AudioService aupod;
    
    public MainActivity mainactt;
    
    private static Handler handle = new Handler(Looper.getMainLooper());
    
    private AudioService.ConnectionListener connl;
    
    ExecutorService worker;
    
    private App(){
        worker = Executors.newFixedThreadPool(2);
    }
    
    public static App get(){
        App inst = deft;
        if(inst==null){
            synchronized(App.class){
                inst = App.deft;
                if(inst==null){
                    inst = App.deft = new App();
                }
            }
        }
        return inst;
    }
    
    public static void runInBackground(Runnable task){
        //new Thread(task).start();
        get().worker.submit(task);
    }
    
    public static void runInUiThread(Runnable task){
        handle.post(task);
    }
    
    public void registerAudiosService(AudioService service){
        aupod = service;
    }
    
    public AudioService getAudioService(){
        return aupod;
    }
    
    public AudioService.ConnectionListener getConnl(){
        AudioService.ConnectionListener c = connl;
        connl = null;
        return c;
    }
    
    public void setConnl(AudioService.ConnectionListener temp){
        connl = temp;
    }
}
