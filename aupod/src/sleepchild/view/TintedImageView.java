package sleepchild.view;
import android.widget.*;
import android.content.*;
import android.util.*;
import android.graphics.drawable.*;
import android.graphics.*;
import sleepchild.aupod22.*;

public class TintedImageView extends ImageView
{
    public TintedImageView(Context ctx){
        super(ctx);
        init();
    }
    
    public TintedImageView(Context ctx, AttributeSet attrs){
        super(ctx, attrs);
        init();
    }
    
    public TintedImageView(Context ctx, AttributeSet attrs, int defStyleAttr){
        super(ctx, attrs, defStyleAttr);
        init();
    }
    
    public TintedImageView(Context ctx, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(ctx, attrs, defStyleAttr, defStyleRes);
        init();
    }
    
    Context ctx;
    private void init(){
        ctx = getContext();
        setTint(ctx.getResources().getColor(R.color.color2));
    }

    @Override
    public void setBackgroundResource(int resid)
    {
        // TODO: Implement this method
        super.setBackgroundResource(resid);
        //setTint();
    }
    
    
    public void setTint(int color){
        setColorFilter(color);
        Drawable d = getBackground();
        if(d!=null){
            //d = d.mutate();
            
            //d.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
            d.setTint(color);
            setBackground(d);
        }
    }
    
    
    
    
}
