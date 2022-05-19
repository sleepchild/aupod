package sleepchild.view.tabview;

import android.widget.*;
import android.content.*;
import android.util.*;
import android.view.*;
import android.graphics.*;
import sleepchild.aupod22.R;
import android.graphics.drawable.*;
import android.content.res.*;
import sleepchild.aupod22.tabs.*;
import java.util.*;
import sleepchild.aupod22.*;
import sleepchild.view.*;

public class TabView extends LinearLayout {

    public TabView(Context ctx){
        super(ctx);
        _init();
    }

    public TabView(Context ctx, AttributeSet attrs){
        super(ctx, attrs);
        _init();
    }

    public TabView(Context ctx, AttributeSet attrs, int defStyleAttr){
        super(ctx, attrs, defStyleAttr);
        _init();
    }

    public TabView(Context ctx, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(ctx, attrs, defStyleAttr, defStyleRes);
        _init();
    }

    //////
    Context ctx;

    LinearLayout tabstrip;
    RoundedLinearLayout tabcontainer;

    int tabStripBackground=0;
    int tabContainerBackground=0;
    int rootBackground=0;
    int cornerRadius=0;
    int colorBorder;

    boolean showStrip = true;
    Tab currentTab=null;
    
    int currentIndex=0;
    int borderWidth=0;

    //Map<String, Tab> tablist = new HashMap<>();

    List<Tab> tablist = new ArrayList<>();
    List<TextView> tabTitles = new ArrayList<>();

    GradientDrawable gdrc;

    private void _init(){
        ctx = getContext();
        View v = LayoutInflater.from(ctx).inflate(R.layout.customview_tabview, null, false);

        tabcontainer = (RoundedLinearLayout) v.findViewById(R.id.customview_tabview_tabcontainer);
        tabstrip = (LinearLayout) v.findViewById(R.id.customview_tabview_tabstrip);

        addView(v, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

        gdrc = new GradientDrawable();
        gdrc.setCornerRadius(20);
        colorBorder = ctx.getResources().getColor(R.color.color2);
        borderWidth = ctx.getResources().getDimensionPixelSize(R.dimen.border_width_bw2);
        gdrc.setStroke(borderWidth, colorBorder);
        //
    }

    public void addTab(String title, Tab tab){
        tablist.add(tab);
        _addTitle(title);
    }

    private void _addTitle(String title){
        LinearLayout v = (LinearLayout) LayoutInflater.from(ctx).inflate(R.layout.tabtitle,null, false);
        final TextView tv = (TextView) v.findViewById(R.id.tabtitleTextView);
        tv.setText(title);
        v.removeAllViews();
        tv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int i = tabTitles.indexOf(tv);
                showTab(i);
            }
        });
        tabTitles.add(tv);
        tabstrip.addView(tv);
    }

    private void _setTitles(int index){
        int cc = tabstrip.getChildCount();
        for(int i=0;i<cc;i++){
            TextView tv = (TextView) tabstrip.getChildAt(i);
            tv.setBackgroundColor(Color.TRANSPARENT);
        }
        tabstrip.getChildAt(index).setBackground(gdrc);
    }
    
    public void showTab(Tab tab){
        currentIndex = tablist.indexOf(tab);
        showTab(currentIndex);
    }

    public void showTab(int index){
        currentIndex = index;
        if(index>=0 && index<tablist.size()){
            Tab tab = tablist.get(index);
            if(currentTab==tab){
                tab.onTabAlreadyVisible();
            }else{
                if(currentTab!=null){
                    currentTab.onTabHidden();
                }
                _show(tab);
                _setTitles(index);
                currentTab.onTabShown();
            }
        }
    }
    
    private void _show(Tab tab){
        currentTab = tab;
        tabcontainer.removeAllViews();
        tabcontainer.addView(tab.getView());
    }
    
    public void onApplyTheme(ThemeManager.Theme theme){
        setBackgroundColor(theme.background);
        for(Tab t : tablist){
            t.onApplyTheme(theme);
        }
        for(TextView tv : tabTitles){
            tv.setTextColor(theme.text);
        }
        colorBorder = theme.dividers;
        //
        gdrc.setStroke(borderWidth, colorBorder);
        _setTitles(currentIndex);
        tabcontainer.setBorderColor(theme.dividers);
    }


}
