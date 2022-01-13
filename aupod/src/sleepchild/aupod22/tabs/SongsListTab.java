package sleepchild.aupod22.tabs;

import android.view.*;
import android.content.*;
import android.widget.*;
import java.util.*;
import sleepchild.postman.PostMan;
import sleepchild.aupod22.models.*;
import sleepchild.aupod22.*;
import sleepchild.view.tabview.Tab;
import sleepchild.aupod22.adapters.*;
import sleepchild.aupod22.activity.*;

public class SongsListTab extends Tab
{
    private View root;
    private SongListAdaptor adapter;
    private SongItem currentSong;
    private ListView list1;
    //MainActivity act;
    
    //*
    public SongsListTab(MainActivity ctx){
        //act = ctx;
        root = LayoutInflater.from(ctx).inflate(R.layout.list_view, null, false);
        list1 = (ListView) root.findViewById(R.id.list_view_list1);
        adapter = new SongListAdaptor(ctx);
        list1.setAdapter(adapter);
        //list1.setFastScrollEnabled(true);
        //list1.setScrollbarFadingEnabled(false);
        //list1.setFastScrollAlwaysVisible(true);
        //list1.setScrollBarStyle(R.style.scrollbarstyles);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> p1, View v, int pos, long p4){
                SongItem si = (SongItem) v.getTag();
                PostMan.getInstance().post(new PlaybackRequest(PlaybackRequest.TYPE.PLAY_SONGITEM, si));
            }
        });
    }
    //*/

    public void update(List<SongItem> list){
        adapter.update(list);
    }
    
    public void setCurrent(SongItem currentSong){
        adapter.setCurrent(currentSong);
        this.currentSong = currentSong;
    }
    
    public void showCurrent(){
        if(currentSong!=null){
            list1.setSelection(adapter.getPos(currentSong));
        }
    }

    @Override
    public void onTabShown(){
        super.onTabShown();
    }

    @Override
    public void onTabAlreadyVisible(){
        super.onTabAlreadyVisible();
        showCurrent();
    }

    @Override
    public void onTabHidden(){
        super.onTabHidden();
    }

    @Override
    public View getView(){
        return root;
    }

}
