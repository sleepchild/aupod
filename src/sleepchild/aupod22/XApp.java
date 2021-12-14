package sleepchild.aupod22;

import android.app.Application;
import android.content.Context;

public class XApp extends Application
{
    private static Context ctx;

    @Override
    public void onCreate(){
        super.onCreate();
        ctx = getApplicationContext();
    }
    
    public static Context getContext2(){
        return ctx;
    }
    
}
