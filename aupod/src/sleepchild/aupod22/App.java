package sleepchild.aupod22;

import android.app.Application;
import android.content.Context;
import sleepchild.aupod22.library.*;

public class App extends Application
{
    private static Context ctx;

    @Override
    public void onCreate(){
        super.onCreate();
        ctx = getApplicationContext();
        SongLibrary.get().discover(this);
        //new Ape(); Utils.toast(ctx,"done");
    }
    
    public static Context getContext(){
        return ctx;
    }
    
}
