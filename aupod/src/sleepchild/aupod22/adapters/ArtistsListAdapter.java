package sleepchild.aupod22.adapters;
import android.widget.*;
import android.view.*;
import android.content.*;
import sleepchild.aupod22.*;
import java.util.*;
import android.graphics.*;
import sleepchild.aupod22.activity.*;
import sleepchild.aupod22.models.*;

public class ArtistsListAdapter extends BaseAdapter
{
    MainActivity ctx;
    List<String> artistlist = new ArrayList<>();
    Map<String, Integer> al = new HashMap<>();
    LayoutInflater inf;
    
    public ArtistsListAdapter(MainActivity act){
        this.ctx = act;
        inf = LayoutInflater.from(ctx);
    }
    
    public void update(List<SongItem> list){
        artistlist.clear();  
        Set<String> s = new HashSet<>();
        //*
        for(SongItem si : list){
            s.add(si.artist);
            //int ts = 0;
            Integer ts = al.get(si.artist);
            if(ts==null){
                ts = 0;
            }
            ts++;
            al.put(si.artist, ts);
        }
        for(String a : s){
            artistlist.add(a);
        }
        Collections.sort(artistlist, new Comparator<String>(){
            @Override
            public int compare(String p1, String p2){
                return p1.toLowerCase().compareTo(p2.toLowerCase());
            }
        });
        //*/
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        // TODO: Implement this method
        return artistlist.size();
    }

    @Override
    public Object getItem(int pos){
        return artistlist.get(pos);
    }

    @Override
    public long getItemId(int p1){
        return p1;
    }

    @Override
    public View getView(int pos, View v, ViewGroup p3){
        String artist = artistlist.get(pos);
        v = inf.inflate(R.layout.songlist_item, null, false);
        TextView name = (TextView) v.findViewById(R.id.songlist_itemTitle);
        name.setText(artist);
        
        TextView songs = (TextView) v.findViewById(R.id.songlist_itemArtist);
        Integer num = al.get(artist);
        //String snum = num==1? " Song" : " Songs"; snum = num + snum;
        String snum = num==1? num+" Song" : num+" Songs";
        songs.setText(snum);
        
        //v.findViewById(R.id.songlist_item_albumart).setVisibility(View.GONE);
        
        return v;
    }
    
}
