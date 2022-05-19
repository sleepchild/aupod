package sleepchild.aupod22.tabs;

import android.view.View;
import android.view.LayoutInflater;
import sleepchild.aupod22.activity.MainActivity;
import android.widget.ListView;
import sleepchild.view.tabview.Tab;
import sleepchild.aupod22.adapters.ArtistListAdapter;
import java.util.List;
import sleepchild.aupod22.models.SongItem;
import sleepchild.aupod22.R;
import sleepchild.aupod22.ThemeManager.*;
import sleepchild.aupod22.*;
import sleepchild.aupod22.library.*;
import sleepchild.aupod22.models.*;
import sleepchild.view.fastscroll.*;

public class ArtistTab extends Tab {
    
    MainActivity act;
    View root=null;
    FastScrollListView list1;
    ArtistListAdapter adapter;
    
    public ArtistTab(MainActivity act){
        this.act = act;
        
        root = LayoutInflater.from(act).inflate(R.layout.default_listview,null,false);
        list1 = (FastScrollListView) root.findViewById(R.id.list_view_list1);
        adapter = new ArtistListAdapter(act.getLayoutInflater());
        list1.setAdapter(adapter);
        //list1.setBackgroundResource(R.color.black);
        //
    }
    
    @Override
    public View getView(){
        return root;
    }

    @Override
    public void onTabShown(){
        SongLibrary.getInstance().getAllArtists(new SongLibrary.ResultCallback<List<ArtistItem>>(){
            public void onResult(List<ArtistItem> list){
                adapter.update(list);
            }
        });
    }

    @Override
    public void onTabHidden(){
        //
    }

    @Override
    public void onTabAlreadyVisible(){
        //
    }

    @Override
    public void onApplyTheme(ThemeManager.Theme theme){
        adapter.setTheme(theme);
        list1.setThumbColor(theme.icon);
        list1.setThumbTextColor(theme.text);
    }
    
    
    
}
