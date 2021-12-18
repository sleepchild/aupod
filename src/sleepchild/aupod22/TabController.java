package sleepchild.aupod22;
import java.util.*;
import android.widget.*;
import sleepchild.aupod22.tabs.*;

public class TabController
{
    private MainActivity act;
    private FrameLayout tabContainer;
    LinearLayout tabSwitcher;
    private Tab current;
    //private Map<String, Tab> tablist = new HashMap<>();
    private List<Tab> tabs = new ArrayList<>();
    private List<TabVisibilityEvents> tabVisiEvents = new ArrayList<>();
    
    public TabController(MainActivity activity){
        this.act = activity;
        tabContainer = (FrameLayout) act.findViewById(R.id.activity_main_tabholder);
        
    }
    
    public void add(Tab tab){
        tabs.add(tab);
    }
    
    public void show(int index){
        Tab tab = tabs.get(index);
        if(current!=null&&current.equals(tab)){
            return;
        }
        tabContainer.removeAllViews();
        current = tab;
        tabContainer.addView(current.getView());
    }
    
    private void notifyShown(Tab tab){
        //
    }
    
    private void notifyAlreadyVisible(Tab tab){
        //
    }
    
    private void notidyHidden(Tab tab){
        //
    }
    
    public interface TabVisibilityEvents{
        public void onShown(Tab tab);
        public void onAlreadyVisible(Tab tab);
        public void onHidden(Tab tab);
    }
}
