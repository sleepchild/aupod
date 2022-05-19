package sleepchild.aupod22.menu;

import android.app.*;
import android.widget.Gallery.*;
import android.content.*;
import android.view.*;

public class MDialog extends Dialog implements
    DialogInterface.OnShowListener,
    DialogInterface.OnDismissListener,
    View.OnClickListener
{
    private Activity act;
    
    public MDialog(Activity act, int resid){
        super(act);
        this.act = act;
        setContentView(resid);
        setOnShowListener(this);
        setOnDismissListener(this);
    }
    
    public MDialog(Activity act){
        super(act);
    }
    
    protected <T extends View> T findView(int resid){
        return (T) findViewById(resid);
    }
    
    public void showFull(){
        super.show();
        getWindow().setBackgroundDrawable(null);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }
    
    protected void setClickable(int resid){
        findViewById(resid).setOnClickListener(this);
    }
    
    protected Activity getActivity(){
        return this.act;
    }

    @Override
    public void onShow(DialogInterface i){}

    @Override
    public void onDismiss(DialogInterface i){}

    @Override
    public void onClick(View v){}

    
    


    
    
    
}
