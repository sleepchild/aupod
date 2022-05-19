package sleepchild.aupod22.activity;

import android.os.Bundle;
import android.view.View;
import sleepchild.aupod22.R;
import java.util.Map;
import sleepchild.aupod22.ui.settings.*;
import android.widget.LinearLayout;
import java.util.HashMap;
import sleepchild.aupod22.ThemeManager.*;
import sleepchild.aupod22.*;
import android.widget.*;

public class SettingsActivity extends BaseActivity
{
    public static final int requetsCode = 333749;
    
////    private String tab_interface = "interface";
//    private String tab_library = "library";
//    private String tab_headset = "headset";
//    private String tab_notifications = "notifications";
//    
    private Map<String, SettingsTab> tabs = new HashMap<>();
    
    //LinearLayout main, tabContainer;
    
    boolean isTabOpen = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setThemeable(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //
        init();
    }

    public void init(){
        //
    }

    @Override
    protected void onApplyTheme(ThemeManager.Theme theme){
        super.onApplyTheme(theme);
        //
        setTextViewColor(R.id.title, theme.text);
        setViewsBackgroundColor(theme.dividers,R.id.d1);
    }
    
    private void registerTab(String name, SettingsTab tab){
        //tabs.put(name, tab);
    }
    
    private void showTab(String name){
        SettingsTab t = tabs.get(name);
        if(t!=null){
           // tabContainer.removeAllViews();
            //tabContainer.addView( t.getView());
           // tabContainer.animate().translationX(0).setDuration(300).start();
        }
        isTabOpen = true;
    }
    
    private void closeTab(){
        //tabContainer.setTranslationX(-tabContainer.getWidth());
        //tabContainer.animate().translationX(tabContainer.getWidth()).setDuration(300).start();
        isTabOpen = false;
    }
    
    

    @Override
    public void onClick(View v){
        int id = v.getId();
        
    }
    
    @Override
    public void onBackPressed()
    {
        if(isTabOpen){
            //closeTab();
        }else{
            super.onBackPressed();
        }
    }
    
    


}
