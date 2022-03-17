package sleepchild.aupod22;

import android.app.Application;
import android.content.Context;
import sleepchild.aupod22.library.*;
import android.widget.*;

public class XApp extends Application{
    private static Context ctx;
    private static XApp inst;
    
    @Override
    public void onCreate(){
        super.onCreate();
        inst = this;
        ctx = getApplicationContext();
        //SongLibrary.getInstance().update(this);
    }
    
    public static XApp get(){
        return inst;
    }
    
    public static Context getContext(){
        return ctx;
    }
    
    public static void exit(){
        inst._end();
    }
    
    private void _end(){
        Runtime.getRuntime().exit(1);
    }
    
}
