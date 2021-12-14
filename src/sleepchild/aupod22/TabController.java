package sleepchild.aupod22;
import java.util.*;
import android.widget.*;

public class TabController
{
    private MainActivity act;
    private FrameLayout tabContainer;
    private Tab current;
    private List<Tab> tabs = new ArrayList<>();
    private List<TabVisibilityEvents> tabVisiEvents = new ArrayList<>();
    
    public TabController(MainActivity activity){
        this.act = activity;
        tabContainer = act.findViewById(R.id.activity_main_tabholder);
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
    
    public interface TabVisibilityEvents{
        public void onShown(Tab tab);
        public void onHidden(Tab tab);
    }
}
