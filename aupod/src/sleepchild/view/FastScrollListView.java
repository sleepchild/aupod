package sleepchild.view;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.view.View;
import android.widget.AbsListView;
import android.os.Handler;
import android.widget.ListView;
import android.widget.*;
import sleepchild.aupod22.*;
import android.graphics.drawable.*;


public class FastScrollListView extends ListView{


    private Rect thumbRect;
    private Drawable thumb;
    BeanDrawable cat;
    private boolean isHandlingTouchEvent;
    boolean drawThumb = true;
    private float mDownY;
    //
    int thumbHeight=100;
    int thumbMinHeight = 100;
    int thumbWidth = 20;
    
    float my;
    float tt;

    Handler handle=new Handler();
    
    TextView tv;

    public FastScrollListView(Context ctx){
        super(ctx);
        init();
    }

    public FastScrollListView(Context ctx, AttributeSet attrs){
        super(ctx,attrs);
        init();
    }

    public FastScrollListView(Context ctx, AttributeSet attrs, int defStyles){
        super(ctx,attrs, defStyles);
        init();
    }

    private void init(){
        //
        thumbRect = new Rect(0,0, thumbWidth, thumbHeight);

        int tc = getContext().getResources().getColor(R.color.color2);
        thumb = new ColorDrawable(tc);
       // tc = Color.parseColor("#a0204077");
        tc = Color.argb(100,
            Color.red(tc),
            Color.green(tc),
            Color.blue(tc)
        );
        
        cat = new BeanDrawable(tc, Color.WHITE);
        
        hideThumb();        
        //
    }

    public void setThumbHeight(int height){
        thumbHeight = height;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        //float y= ev.getY();
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(isTouchingThumb(ev) && drawThumb){
                    return onTouchEvent(ev);
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event){
        float y = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(isTouchingThumb(event) && drawThumb){
                    mDownY = y;
                    isHandlingTouchEvent=true;
                    tt = y;
                    my = thumbRect.top;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //*
                int deltaY = Math.round(y - mDownY);
                if(isHandlingTouchEvent){
                    if(deltaY!=0){
                       handleScroll(event, deltaY);
                       mDownY = y;
                    }
                }
                //*/
                break;
            case MotionEvent.ACTION_UP:
                isHandlingTouchEvent=false;
                invalidate();
                break;
        }
        if(isHandlingTouchEvent){
            invalidate();
            requestDisallowInterceptTouchEvent(true);
            return true;
        }
        return super.onTouchEvent(event);
    }
    

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);
        postDraw(canvas);
    }

    private void postDraw(Canvas c){

        updateThumbRect();
      
        //thumb.setBounds((thumbRect.left)+getScrollX(),thumbRect.top+getScrollY(), thumbRect.right+getScrollX(), thumbRect.bottom+getScrollY());
        //thumb.draw(c);

        if(drawThumb){
            thumb.setBounds((thumbRect.left)+getScrollX(),thumbRect.top+getScrollY(), thumbRect.right+getScrollX(), thumbRect.bottom+getScrollY());
            thumb.draw(c);
        }else{
            //  thumb.setBounds(0,thumbRect.top+getScrollY(), thumbWidth, thumbRect.bottom+getScrollY());
            //  thumb.draw(c);
        }

        if(isHandlingTouchEvent){
            cat.setBounds((thumbRect.left-350)+getScrollX(),thumbRect.top+getScrollY(), (thumbRect.right+getScrollX())-(thumbWidth+10), thumbRect.top+100);
            cat.draw(c);
        }
    }


    public void updateThumbRect(){
        int range = computeVerticalScrollRange();
        int h = getHeight();

        if(range>=0){

            int extent = computeVerticalScrollExtent();
            int maxOffset = range - extent;

            if(maxOffset>=0){
                int offset = computeVerticalScrollOffset();
                float percent = offset * 1f / maxOffset;
                
                float vp = extent * 1f / range;
                int th = Math.max(thumbMinHeight, Math.round(vp * h));

                int top = Math.round( (getHeight() - thumbRect.height()) * percent);
                thumbRect.top = top;
                thumbRect.bottom = top+ th;

                if(isHandlingTouchEvent){
                    //
                }

            }
        }
    }

    public boolean isTouchingThumb(MotionEvent evt){
        int x = (int) evt.getX();
        int y = (int) evt.getY();
        if(x>=(thumbRect.left-50)
           && y>=thumbRect.top
           && y<=thumbRect.bottom){
            return true;
        }
        return false;
    }
    
    private void handleScroll(MotionEvent ev, int dy){
        float y = ev.getY();
        
        float d = tt - my;
        
        int cc = getAdapter().getCount();
        int m = getHeight() - thumbHeight;
        
        int nt = Math.round(y) - Math.round(d);
        if(nt<0){
            nt = 0;
        }else if(nt>m){
            nt = m;
        }
        
        thumbRect.top = nt;
        thumbRect.bottom = thumbRect.top + thumbHeight;
        
        float l1 = nt;
        float l2 = m;
        float sp = l1 / l2 *100;
        
        float l3 = (sp/100) * cc ;
        
        sb = Math.round(l3);
        if(sb<0){
            sb=0;
        }else if(sb>cc){
            sb = cc;
        }
        
        setSelection(sb);
    }
    
    int sb=0;

    // this is has a wierd effect for very long lists
    // so we're using 'htouch' above
    private void handleScroll2(int dy){
        int range = computeVerticalScrollRange();
        if(range>=0){
            int extent = computeVerticalScrollExtent();
            int maxOffset = range - extent;

            if(maxOffset>=0){
                int offset = computeVerticalScrollOffset();
                float percent = offset * 1f / maxOffset;

                int top = Math.round( (getHeight() - thumbRect.height()) * percent);
                thumbRect.top = top;
                thumbRect.bottom = top+ thumbHeight;

                //isHandlingTouchEvent
                int newTop = thumbRect.top + dy;
                int minTop = 0;
                int maxTop = getHeight() - thumbHeight;

                if(newTop<minTop){
                    newTop= minTop;
                }else if(newTop>maxTop){
                    newTop= maxTop;
                }

                float scrollPercent = newTop * 1f / maxTop;
                int newOffset = Math.round((range - extent) * scrollPercent);
                int scrollBy = newOffset - offset;
                
                View v = this;
                if( v instanceof AbsListView){
                    ((AbsListView)v).smoothScrollBy(scrollBy, 0);
                }else{
                    scrollBy(0,scrollBy);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh)
    {
        // TODO: Implement this method
        super.onSizeChanged(w, h, ow, oh);

        thumbRect.right = w;
        thumbRect.left = thumbRect.right -15;
        invalidate();
    }

    @Override
    protected boolean awakenScrollBars()
    {
        return false;//super.awakenScrollBars();
    }

    @Override
    protected boolean awakenScrollBars(int startDelay)
    {
        // TODO: Implement this method
        return false;// super.awakenScrollBars(startDelay);
    }

    @Override
    protected boolean awakenScrollBars(int startDelay, boolean invalidate)
    {
        return false;// stop default scrollbars from showing
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        updateThumbRect();
        showThumb();
        hideThumb();
        
        gd();
    }
    
    void gd(){
        //int p = indexOfChild( getSelectedView());
        String txt = getScrollText(sb);
        
        if(txt!=null){
            cat.setText(txt);
        }
    }
    
    public String getScrollText(int pos){
        if(gt!=null){
            return gt.ogt(pos);
        }
        return  null;
    }
    
    public void setOgtl(GT l){
        gt = l;
    }
    
    GT gt;
    
    public interface GT{
        public String ogt(int pos);
    }

    private void showThumb(){
        drawThumb = true;
    }

    private void hideThumb(){
        handle.removeCallbacks(thider);
        handle.postDelayed(thider, 1000);
    }

    Runnable thider = new Runnable(){
        public void run(){
            if(isHandlingTouchEvent){
                return;
            }
            drawThumb = false;
            invalidate();
        }
    };

}
