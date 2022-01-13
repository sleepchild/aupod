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

public class TabView extends LinearLayout {

    public TabView(Context ctx){
        super(ctx);
        init();
    }

    public TabView(Context ctx, AttributeSet attrs){
        super(ctx, attrs);
        init();
        //setAttrs(attrs,0,0);
        //applyAttrs();
    }

    public TabView(Context ctx, AttributeSet attrs, int defStyleAttr){
        super(ctx, attrs, defStyleAttr);
        init();
        //setAttrs(attrs,defStyleAttr,0);
        //applyAttrs();
    }

    public TabView(Context ctx, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(ctx, attrs, defStyleAttr, defStyleRes);
        init();
        //setAttrs(attrs, defStyleAttr, defStyleRes);
        //applyAttrs();
    }

    //////
    Context ctx;

    LinearLayout tabstrip;
    LinearLayout tabcontainer;

    int tabStripBackground=0;
    int tabContainerBackground=0;
    int rootBackground=0;
    int cornerRadius=0;
    int colorWhite, colorBlack;

    boolean showStrip = true;
    Tab currentTab=null;

    //Map<String, Tab> tablist = new HashMap<>();

    List<Tab> tablist = new ArrayList<>();
    List<TextView> tabTitles = new ArrayList<>();

    GradientDrawable gdrc;

    private void init(){
        ctx = getContext();
        View v = LayoutInflater.from(ctx).inflate(R.layout.customview_tabview, null, false);

        tabcontainer = (LinearLayout) v.findViewById(R.id.customview_tabview_tabcontainer);
        tabstrip = (LinearLayout) v.findViewById(R.id.customview_tabview_tabstrip);

        addView(v, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

        gdrc = new GradientDrawable();
        gdrc.setCornerRadius(20);
        int col = ctx.getResources().getColor(R.color.color2);
        gdrc.setStroke(5, col);
        //
    }

    public void addTab(String title, Tab tab){
        tablist.add(tab);
        addTitle(title);
    }

    private void addTitle(String title){
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

    private void setTitles(int index){
        int cc = tabstrip.getChildCount();
        for(int i=0;i<cc;i++){
            TextView tv = (TextView) tabstrip.getChildAt(i);
            tv.setBackgroundColor(Color.TRANSPARENT);
        }
        tabstrip.getChildAt(index).setBackground(gdrc);
    }

    public void showTab(int index){
        if(index>=0 && index<tablist.size()){
            Tab tab = tablist.get(index);
            if(currentTab==tab){
                tab.onTabAlreadyVisible();
            }else{
                if(currentTab!=null){
                    currentTab.onTabHidden();
                }
                show(tab);
                setTitles(index);
                currentTab.onTabShown();
            }
        }
    }

    private void show(Tab tab){
        currentTab = tab;
        tabcontainer.removeAllViews();
        tabcontainer.addView(tab.getView());
    }



}
