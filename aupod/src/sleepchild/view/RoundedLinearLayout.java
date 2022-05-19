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
    int colorBorder;
    int mRadius=20;
    RectF bounds;
    Context ctx;
    Paint backgroundPaint, borderPaint;
    int borderWidth=5;
    
    public RoundedLinearLayout(Context ctx){
        super(ctx);
        init();
    }

    public RoundedLinearLayout(Context ctx, AttributeSet attrs){
        this(ctx, attrs,0);
    }

    public RoundedLinearLayout(Context ctx, AttributeSet attrs, int defStyleAttr){
        super(ctx, attrs, defStyleAttr);
        this.ctx = ctx;
        setAttrs(attrs);
        init();
    }

    public RoundedLinearLayout(Context ctx, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        this(ctx, attrs, defStyleAttr);
    }
    
    void setAttrs(AttributeSet attrs){
        TypedArray a =  ctx.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundedLinearLayout, 0 ,0);
        colorBG = a.getColor(R.styleable.RoundedLinearLayout_bgColor, Color.TRANSPARENT);
        colorBorder = a.getColor(R.styleable.RoundedLinearLayout_borderColor, colorBorder);
        mRadius = a.getDimensionPixelSize(R.styleable.RoundedLinearLayout_radius,mRadius);
        borderWidth = a.getDimensionPixelSize(R.styleable.RoundedLinearLayout_borderWidth, borderWidth);
        a.recycle();
    }
    
    private void init(){
        ctx = getContext();
        
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(colorBG);
        backgroundPaint.setStyle(Paint.Style.FILL);
        //
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(colorBorder);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        
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
        
        setClipChildren(true);
        setClipToPadding(true);
        
        setBackground(null);
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void setBackgroundColor(int color){
        super.setBackgroundColor(color);
    }
    
    public void setBorderColor(int color){
        colorBorder = color;
        borderPaint.setColor(color);
        invalidate();
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
        //backgroundPaint.setColor(colorBG);
        canvas.drawRoundRect(bounds, mRadius, mRadius, backgroundPaint);
        canvas.drawRoundRect(bounds, mRadius, mRadius, borderPaint);
        super.onDraw(canvas);
    }
    
    

    
    
    
    
}
