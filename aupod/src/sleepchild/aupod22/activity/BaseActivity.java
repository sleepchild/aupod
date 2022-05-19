package sleepchild.aupod22.activity;

import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.view.*;
//import android.inputmethodservice.*;
import android.view.inputmethod.*;
import sleepchild.aupod22.*;
import sleepchild.view.*;

public class BaseActivity extends Activity implements View.OnClickListener
{

    public Context ctx;
    public InputMethodManager ims;
    private boolean themeable = false;

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
    
    public <T extends View> T findView(int resid){
        return (T) findViewById(resid);
    }

    public View vid(int resid){
        return findViewById(resid);
    }

    public void startActivity(Class<?> clazz){
        startActivity(new Intent(this, clazz));
    }

    public void showView(int resid){
        showView(findViewById(resid));
    }
    
    public void showView(View v){
        v.setVisibility(View.VISIBLE);
    }

    public void hideView(int resid){
        hideView(findViewById(resid));
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

    protected void setClickable(int resid){
        findViewById(resid).setOnClickListener(this);
    }
    
    @Override public void onClick(View v){}

    void setThemeable(boolean value){
        themeable = value;
    }
    
    //@Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {
        // TODO: Implement this method
    }
    
    protected void onApplyTheme(ThemeManager.Theme theme){
        //getWindow().setStatusBarColor(theme.background);
        
        View v = findViewById(R.id.root);
        if(v!=null){
            v.setBackgroundColor(theme.background);
        }
    }
    
    //  theme helper methods
    protected void setTextViewText(int resid, CharSequence text){
        TextView tv = findView(resid);
        tv.setText(text);
        tv=null;
    }
    
    protected void setTextViewColor(int resid, int color){
        TextView tv = findView(resid);
        tv.setTextColor(color);
    }
    
    protected void setTextViewColor(TextView tv, int color){
        tv.setTextColor(color);
    }
    
    protected void setTextViewsColor(int color, TextView... tvs){
        for(TextView v : tvs){
            v.setTextColor(color);
        }
    }
    
    protected void setViewsBackgroundColor(int color, int... resids){
        for(int id : resids){
            findViewById(id).setBackgroundColor(color);
        }
    }
    
    protected void setTextViewsColor(int color, int... resids){
        if(resids!=null){
            for(int id : resids){
                setTextViewColor(id, color); 
            }
        }
    }
    
    protected void setTintablesTint(int color, int... resids){
        for(int id : resids){
            TintedImageView iv = findView(id);
            iv.setTint(color);
        }
    }
    
    
    //////
    
    private void checkTheme(){
        if(themeable){ // && themechanged){
            onApplyTheme(ThemeManager.getTheme());
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkTheme();
    }
    
    


}
