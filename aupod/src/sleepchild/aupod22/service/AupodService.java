package sleepchild.aupod22.service;
import android.app.*;
import android.os.*;
import android.content.*;

public class AupodService extends Service
{

    @Override
    public IBinder onBind(Intent p1)
    {
        // TODO: Implement this method
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO: Implement this method
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // TODO: Implement this method
        return super.onStartCommand(intent, flags, startId);
    }
    
}
