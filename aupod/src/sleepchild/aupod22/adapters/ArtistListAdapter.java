package sleepchild.aupod22.adapters;

import android.widget.*;
import android.view.*;
import sleepchild.aupod22.*;
import java.util.*;
import sleepchild.aupod22.models.*;
import sleepchild.aupod22.library.*;

public class ArtistListAdapter extends BaseAdapter
{
    private LayoutInflater inf;
    private ThemeManager.Theme tm;
    
    List<ArtistItem> alist = new ArrayList<>();
    
    public ArtistListAdapter(LayoutInflater inflator){
        this.inf = inflator;
        //alist = SongLibrary.getInstance().getAllArtists();
    }
    
    public void update(List<ArtistItem> list){
        alist = list;
        notifyDataSetChanged();
    }
    
    public void setTheme(ThemeManager.Theme theme){
        tm = theme;
    }
    
    @Override
    public int getCount(){
        return alist.size();
    }

    @Override
    public ArtistItem getItem(int pos){
        return alist.get(pos);
    }

    @Override
    public long getItemId(int pos){
        return pos;
    }

    @Override
    public View getView(int pos, View v, ViewGroup p3){
        
        ArtistItem itm = getItem(pos);
        
        if(v==null){
            v = inf.inflate(R.layout.adapteritem_artist, null, false);
        }
        
        TextView name = (TextView) v.findViewById(R.id.artistitem_title);
        name.setText(itm.name);
        name.setTextColor(tm.text);
        
        TextView count = (TextView) v.findViewById(R.id.artistitem_songcount);
        String p = itm.songcount>1 ? " songs" : " song";
        count.setText(itm.songcount+" "+p);
        count.setTextColor(tm.text);
        v.setBackgroundColor(tm.background);
        return v;
    }
    
    
    
    
}
