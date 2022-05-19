package sleepchild.aupod22.tabs;
import sleepchild.aupod22.*;
import android.view.*;
import android.widget.*;
import sleepchild.view.tabview.Tab;
import sleepchild.aupod22.activity.*;
import sleepchild.aupod22.ThemeManager.*;
import sleepchild.aupod22.models.*;
import java.util.*;

public class AlbumsTab extends Tab
{
    MainActivity act;
    View root;
    ListView list1;

    public AlbumsTab(MainActivity act){
        this.act = act;
        root = LayoutInflater.from(act).inflate(R.layout.default_listview,null,false);
        list1 = (ListView) root.findViewById(R.id.list_view_list1);
    }
    
    public void update(List<SongItem> list){
        
    }

    @Override
    public View getView(){
        return root;
    }

    @Override
    public void onApplyTheme(ThemeManager.Theme theme)
    {
        //
    }
    
    
}
