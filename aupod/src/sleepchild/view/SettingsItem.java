package sleepchild.view;
import android.widget.*;
import android.content.*;
import android.util.*;
import android.view.*;
import sleepchild.aupod22.*;
import android.graphics.drawable.*;
import android.graphics.*;
import android.content.res.*;

public class SettingsItem extends LinearLayout
{
    TextView mTextView;
    ImageView mIcon;
    int mTextColor=0;
    int mIconColor=0;
    
    public SettingsItem(Context ctx){
        super(ctx);
        init();
    }
    
    public SettingsItem(Context ctx, AttributeSet attrs){
        this(ctx, attrs,0);
    }
    
    public SettingsItem(Context ctx, AttributeSet attrs, int defStyleAttrs){
        super(ctx, attrs, defStyleAttrs);
        init();
        
        TypedArray a = ctx.getTheme().obtainStyledAttributes(attrs,R.styleable.SettingsItem,defStyleAttrs,0);
        
        String textStr =  a.getString(R.styleable.SettingsItem_text);
        int textCol = a.getColor(R.styleable.SettingsItem_textColor,0);
        if(textCol==0){
            textCol = ctx.getResources().getColor(R.color.text);
        }
        setText(textStr);
        setTextColor(textCol);
        //
        Drawable d = a.getDrawable(R.styleable.SettingsItem_icon);
        int iconCol = a.getColor(R.styleable.SettingsItem_iconColor,0);
        if(iconCol==0){
            iconCol = textCol;
        }
        setIcon(d);
        setIconColor(iconCol);
        
        a.recycle();
    }
    
    private void init(){
        getViewLayout();
    }
    
    private void getViewLayout(){
        View v = LayoutInflater.from(getContext()).inflate(R.layout.customview_settingsitem,null, false);
        mTextView = (TextView) v.findViewById(R.id.customview_settingsitem_text);
        mIcon = (ImageView) v.findViewById(R.id.customview_settingsitem_icon);
        addView(v);
    }
    
    public void setText(CharSequence text){
        mTextView.setText(text);
    }
    
    public void setIcon(Bitmap icon){
        setIcon(new BitmapDrawable(getContext().getResources(), icon));
    }
    
    public void setIcon(Drawable drawable){
        mIcon.setBackground(drawable);
    }
    
    public void refresh(){
        //
    }
    
    public void setIconColor(String color){
        setIconColor(Util.getColor(color));
    }
    
    public void setIconColor(int color){
        if(mIconColor==color){
            return;
        }
        mIconColor = color;
        mIcon.setColorFilter(color);
        Drawable d = mIcon.getBackground();
        if(d!=null){
            d = d.mutate();
            d.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
            mIcon.setBackground(d);
        }else{
            //mIcon.getBackground()
        }
    }
    
    public void setTextColor(String color){
        setTextColor(Util.getColor(color));
    }
    
    public void setTextColor(int color){
        if(mTextColor==color){
            return;
        }
        mTextColor=color;
        mTextView.setTextColor(color);
    }
    
    public void onUpdate(){
       // 
    }
    
}
