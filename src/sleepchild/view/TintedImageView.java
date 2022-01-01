package sleepchild.view;
import android.widget.*;
import android.content.*;
import android.util.*;
import android.graphics.drawable.*;
import android.graphics.*;

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
    
    private void init(){
        setTint(Util.getColor("#ffffff"));
    }
    
    private void setTint(int color){
        setColorFilter(color);
        Drawable d = getBackground();
        if(d!=null){
            d = d.mutate();
            d.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
            setBackground(d);
        }
    }
    
    
    
    
}
