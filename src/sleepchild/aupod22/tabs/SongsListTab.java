package sleepchild.aupod22.tabs;

import android.view.*;
import android.content.*;
import android.widget.*;
import java.util.*;
import mrtubbs.postman.*;
import sleepchild.aupod22.postmanmodels.*;
import sleepchild.aupod22.*;

public class SongsListTab extends Tab
{
    View root;
    SongListAdaptor adapter;
    SongItem currentSong;
    ListView list1;

    public SongsListTab(MainActivity ctx){
        root = LayoutInflater.from(ctx).inflate(R.layout.list_view, null, false);
        list1 = (ListView) root.findViewById(R.id.list_view_list1);
        adapter = new SongListAdaptor(ctx);
        list1.setAdapter(adapter);
        list1.setFastScrollEnabled(true);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> p1, View v, int pos, long p4)
            {
                SongItem si = (SongItem) v.getTag();
                PostMan.getInstance().post(new PlaybackRequest(PlaybackRequest.TYPE.PLAY_SONGITEM, si));
            }
        });
    }

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
    public View getView(){
        return root;
    }


}
