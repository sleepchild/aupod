package sleepchild.aupod22;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import mrtubbs.postman.*;
import sleepchild.aupod22.postmanmodels.*;

public class SongListAdaptor extends BaseAdapter
{
    Handler handle = new Handler();
    LayoutInflater inf;
    List<SongItem> songList = new ArrayList<>();
    Context ctx;
    SongItem currentSong;
    
    public SongListAdaptor(Context ctx){
        this.ctx = ctx;
        inf = LayoutInflater.from(ctx);
        //PostMan.getInstance().register(this);
    }
    
    public void update(List<SongItem> list){
        this.songList = list;
        notifyDataSetChanged();
    }
    
    public void setCurrent(SongItem songitem){
        //
        this.currentSong = songitem;
        notifyDataSetChanged();
    }
    
    public int getPos(SongItem si){
        return songList.indexOf(si);
    }

    @Override
    public int getCount()
    {
        // TODO: Implement this method
        return songList.size();
    }

    @Override
    public Object getItem(int p1)
    {
        return songList.get(p1);
    }

    @Override
    public long getItemId(int p1)
    {
        // TODO: Implement this method
        return p1;
    }

    //@Override
    public CharSequence[] getAutofillOptions()
    {
        // TODO: Implement this method
        return null;
    }
    
    @PostEvent
    public void onSongResume(SongResumeEvent evt){
        handle.postDelayed(new Runnable(){
            public void run(){
                //
            }
        },0);
    }

    @Override
    public View getView(int pos, View p2, ViewGroup p3)
    {
        final SongItem item = songList.get(pos);
        
        View v = inf.inflate(R.layout.songlist_item, null, false);
        TextView title = (TextView) v.findViewById(R.id.songlist_itemTitle);
        title.setText(item.title);
        
        TextView artist = (TextView) v.findViewById(R.id.songlist_itemArtist);
        artist.setText(item.artist);
        
        if(currentSong!=null && currentSong.equals(item)){
            v.setBackgroundResource(R.color.songitem_active_bg);
        }
        
        final ImageView art = (ImageView) v.findViewById(R.id.songlist_item_albumart);
        if(item.updated){
            if(item.icon!=null){
                art.setBackgroundDrawable(new BitmapDrawable(ctx.getResources(),item.icon));
            }
        }else{
            // this be a mess..
            SongFactory.get().updateSong(item, new SongFactory.UCB(){
                @Override
                public void onResult(final SongItem si)
                {
                    handle.postDelayed(new Runnable(){
                        public void run(){
                            //item.title = si.title;
                            //item.artist = si.artist;
                            item.icon = si.icon;
                            item.updated = true;
                            if(item.icon!=null){
                                art.setBackgroundDrawable(new BitmapDrawable(ctx.getResources(),item.icon));
                            }
                        }
                    },0);
                }
            });
        }
        v.setTag(item);
        return v;
    }
    
}

