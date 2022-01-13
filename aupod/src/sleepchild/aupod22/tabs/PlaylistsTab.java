package sleepchild.aupod22.tabs;

import android.view.*;
import android.widget.*;
import sleepchild.view.tabview.*;
import sleepchild.aupod22.activity.MainActivity;
import sleepchild.aupod22.R;

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
