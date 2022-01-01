package sleepchild.view;
import android.content.*;
import android.util.*;
import android.widget.*;
import android.graphics.drawable.*;
import android.graphics.*;
import sleepchild.aupod22.*;
import android.content.res.*;

public class RoundedLinearLayout extends LinearLayout
{
    int colorBG=0;
    int mRadius=20;
    RectF bounds;
    Context ctx;
    Paint mPaint;
    
    public RoundedLinearLayout(Context ctx){
        super(ctx);
        init();
    }

    public RoundedLinearLayout(Context ctx, AttributeSet attrs){
        this(ctx, attrs,0);
    }

    public RoundedLinearLayout(Context ctx, AttributeSet attrs, int defStyleAttr){
        super(ctx, attrs, defStyleAttr);
        init();
        setAttrs(attrs);
    }

    public RoundedLinearLayout(Context ctx, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(ctx, attrs, defStyleAttr, defStyleRes);
        init();
        setAttrs(attrs);
    }
    
    void setAttrs(AttributeSet attrs){
        TypedArray a =  ctx.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundedLinearLayout, 0 ,0);
        colorBG = a.getColor(R.styleable.RoundedLinearLayout_bgColor, colorBG);
        mRadius = a.getDimensionPixelSize(R.styleable.RoundedLinearLayout_radius,mRadius);
        a.recycle();
    }
    
    private void init(){
        ctx = getContext();
        
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        
        setWillNotDraw(true);
        
        
        Drawable d = getBackground();
        if(d!=null){
            if(d instanceof ColorDrawable){
                ColorDrawable cd = (ColorDrawable) d;
                if(cd!=null){
                    colorBG = cd.getColor();
                }
            }
        }
        
        setBackground(null);
        setBackgroundColor(Color.TRANSPARENT);
        
    }

    @Override
    public void setBackgroundColor(int color){
        super.setBackgroundColor(color);
    }
    
    

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if(oldw!=w){
            bounds = new RectF(0,0,w,h);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas){
        mPaint.setColor(colorBG);
        canvas.drawRoundRect(0,0, (int) bounds.width(), (int) bounds.height(), mRadius, mRadius, mPaint);
        super.onDraw(canvas);
    }
    
    

    
    
    
    
}
