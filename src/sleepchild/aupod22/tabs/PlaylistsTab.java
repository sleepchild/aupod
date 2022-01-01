package sleepchild.aupod22.tabs;

import sleepchild.aupod22.*;
import android.view.*;
import android.widget.*;
import sleepchild.view.tabview.*;

public class PlaylistsTab extends Tab
{
    MainActivity act;
    View root;
    ListView list1;

    public PlaylistsTab(MainActivity act){
        this.act = act;
        root = LayoutInflater.from(act).inflate(R.layout.list_view,null,false);
        list1 = (ListView) root.findViewById(R.id.list_view_list1);
    }

    @Override
    public View getView(){
        return root;
    }
}
