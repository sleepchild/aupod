package sleepchild.aupod22;
import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.view.*;

public class BaseActivity extends Activity
{

    Context ctx;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.ctx = getApplicationContext();
    }
    
    public void toast(String msg){
        Toast.makeText(ctx, msg, 500).show();
    }
    
    public View vid(int resid){
        return findViewById(resid);
    }
    
    public void startActivity(Class<?> clazz){
        startActivity(new Intent(this, clazz));
    }

    //@Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {
        // TODO: Implement this method
    }

    
}
