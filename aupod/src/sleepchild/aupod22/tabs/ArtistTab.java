package sleepchild.aupod22.tabs;

import android.view.View;
import android.view.LayoutInflater;
import sleepchild.aupod22.activity.MainActivity;
import android.widget.ListView;
import sleepchild.view.tabview.Tab;
import sleepchild.aupod22.adapters.ArtistsListAdapter;
import java.util.List;
import sleepchild.aupod22.models.SongItem;
import sleepchild.aupod22.library.SongFactory;
import sleepchild.aupod22.R;

public class ArtistTab extends Tab {
    
    MainActivity act;
    View root;
    ListView list1;
    ArtistsListAdapter adapter;
    boolean t1;
    
    public ArtistTab(MainActivity act){
        this.act = act;
        root = LayoutInflater.from(act).inflate(R.layout.list_view,null,false);
        list1 = (ListView) root.findViewById(R.id.list_view_list1);
        adapter = new ArtistsListAdapter(act);
        list1.setAdapter(adapter);
        list1.setBackgroundResource(R.color.black);
    }
    
    public void update(List<SongItem> list){
        adapter.update(list);
    }

    @Override
    public View getView(){
        return root;
    }

    @Override
    public void onTabShown()
    {
        if(!t1){
            t1 = true;
            if(SongFactory.get().songs!=null){
                update(SongFactory.get().songs);
            }
        }
        super.onTabShown();
    }

    @Override
    public void onTabHidden()
    {
        // TODO: Implement this method
        super.onTabHidden();
    }

    @Override
    public void onTabAlreadyVisible()
    {
        // TODO: Implement this method
        super.onTabAlreadyVisible();
    }
    
}
