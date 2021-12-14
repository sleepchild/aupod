package sleepchild.aupod22;
import android.view.*;
import android.content.*;
import android.widget.*;
import java.util.*;
import mrtubbs.postman.*;
import sleepchild.aupod22.postmanmodels.*;

public class SongsListTab extends Tab implements View.OnClickListener
{
    View root;
    SongListAdaptor adapter;
    
    SongsListTab(MainActivity ctx){
        root = LayoutInflater.from(ctx).inflate(R.layout.list_view, null, false);
        ListView list1 = (ListView) root.findViewById(R.id.list_view_list1);
        adapter = new SongListAdaptor(ctx);
        list1.setAdapter(adapter);
        list1.setFastScrollEnabled(true);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> p1, View v, int pos, long p4)
                {
                    SongItem si = (SongItem) v.getTag();
                    PostMan.getInstance().post(new PlaybackRequest(PlaybackRequest.PLAYBACK_TYPE.PLAY_SONGITEM, si));
                }
            });
    }
    
    public void update(List<SongItem> list){
        adapter.update(list);
    }

    @Override
    public void onClick(View p1){
        //
    }

    @Override
    public View getView(){
        return root;
    }

    
}
