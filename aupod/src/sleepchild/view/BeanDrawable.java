package sleepchild.view;
import android.graphics.drawable.*;
import android.graphics.*;

public class BeanDrawable extends Drawable
{
    Paint bgPaint;
    Paint textPaint;
    
    int bgColor;
    int textColor;
    
    Path opath;
    
    RectF bound;
    
    String text="S";
    
    public BeanDrawable(int bgcolor, int textcolor){
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(70);
        
        bgColor = bgcolor;
        textColor = textcolor;
        
        opath = new Path();
        
        bound = new RectF(0,0,0,0);
        //
    }
    
    public void setText(String text){
        this.text = text.toUpperCase();
        invalidateSelf();
    }
    
    public void setTexyColor(int color){
        textColor = color;
        //invalidateSelf();
    }
    
    public void setBackgroundColor(int color){
        bgColor = color;
        //invalidateSelf();
    }

    @Override
    public void draw(Canvas c)
    {
        bgPaint.setColor(bgColor);
        textPaint.setColor(textColor);
        
        //c.drawRect(bound, bgPaint);
        c.drawPath(opath, bgPaint);
        c.drawText(text, bound.left+10, bound.bottom - (bound.height()/4), textPaint);
        
    }
    
    void computePath(){
        float l = bound.left;
        float t = bound.top;
        float r = bound.right;
        float b = bound.bottom;
        
        float w = bound.width();
        float h = bound.height();
        
        opath.reset();
        opath.moveTo(l,t+10);
        opath.quadTo(l,t, l+10, t);
        opath.lineTo(l+10, t);
        
        opath.lineTo(r, t +(h/2) );
        opath.lineTo(l-10,b);
        
        opath.lineTo(l,t+10);
        
        opath.close();
        
    }

    @Override
    protected void onBoundsChange(Rect bounds){
        super.onBoundsChange(bounds);
        bound.set(bounds);
        textPaint.setTextSize(bounds.height()/1.5f);
        computePath();
    }
    
    @Override
    public void setAlpha(int p1)
    {
        // TODO: Implement this method
    }

    @Override
    public void setColorFilter(ColorFilter p1)
    {
        // TODO: Implement this method
    }

    @Override
    public int getOpacity()
    {
        // TODO: Implement this method
        return 0;
    }

    
}
