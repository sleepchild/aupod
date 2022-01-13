package sleepchild.view;

import android.widget.*;
import android.content.*;
import android.util.*;
import android.graphics.drawable.*;
import android.graphics.*;

public class RoundedImageView extends ImageView
{
    public RoundedImageView(Context ctx){
        super(ctx);
        init();
    }

    public RoundedImageView(Context ctx, AttributeSet attrs){
        super(ctx, attrs);
        init();
    }

    public RoundedImageView(Context ctx, AttributeSet attrs, int defStyleAttr){
        super(ctx, attrs, defStyleAttr);
        init();
    }

    public RoundedImageView(Context ctx, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(ctx, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        
    }

    @Override
    public void setBackground(Drawable background)
    {
        // TODO: Implement this method
        super.setBackground(background);
    }

    @Override
    public void setBackgroundDrawable(Drawable background)
    {
        // TODO: Implement this method
        super.setBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundResource(int resid)
    {
        // TODO: Implement this method
        super.setBackgroundResource(resid);
    }

    @Override
    public void setBackgroundColor(int color)
    {
        // TODO: Implement this method
        super.setBackgroundColor(color);
    }
    
    private void setBG(){
        GradientDrawable gd = new GradientDrawable();
        gd.setStroke(5, Util.getColor(""));
        gd.setCornerRadius(20);
        
        super.setBackground(gd);
    }



}
