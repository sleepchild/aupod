package sleepchild.aupod22;
import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.view.*;
//import android.inputmethodservice.*;
import android.view.inputmethod.*;

public class BaseActivity extends Activity
{

    public Context ctx;
    public InputMethodManager ims;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.ctx = getApplicationContext();
        ims = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
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
    
    public void showView(View v){
        v.setVisibility(View.VISIBLE);
    }
    
    public void hideView(View v){
        v.setVisibility(View.GONE);
    }
    
    public boolean isViewVisible(View v){
        return v.getVisibility()==View.VISIBLE;
    }
    
    public void showKeyboard(View v){
        v.requestFocus();
        ims.showSoftInput(v,InputMethodManager.SHOW_IMPLICIT);
    }

    //@Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {
        // TODO: Implement this method
    }

    
}
