package sleepchild.view;
import android.widget.*;
import android.content.*;
import android.util.*;
import sleepchild.aupod22.*;

public class ContainerLayout extends RelativeLayout
{
    public ContainerLayout(Context ctx){
        super(ctx);
        init();
    }
   
    public ContainerLayout(Context ctx, AttributeSet attrs){
        super(ctx, attrs);
        init();
    }

    public ContainerLayout(Context ctx, AttributeSet attrs, int defStyleAttr){
        super(ctx, attrs, defStyleAttr);
        init();
    }

    public ContainerLayout(Context ctx, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(ctx, attrs, defStyleAttr, defStyleRes);
        init();
    }
    
    SPrefs pref;
    Context ctx;
    
    private void init(){
        //
        ctx = getContext();
        pref = new SPrefs(ctx);
        
        setBackgroundColor();
        
    }
    
    public void setBackgroundColor(){
        int col = pref.getBackgroundColor();
        if(col==0){
            col = getResources().getColor(R.color.pluto);
        }
        setBackgroundColor(col);
    }
}
