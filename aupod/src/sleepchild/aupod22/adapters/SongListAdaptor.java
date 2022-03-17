package sleepchild.aupod22.adapters;

import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import sleepchild.aupod22.models.*;
import sleepchild.aupod22.menu.*;
import sleepchild.aupod22.activity.*;
import sleepchild.aupod22.*;
import sleepchild.aupod22.library.*;
import sleepchild.view.*;

public class SongListAdaptor extends BaseAdapter
{
    Handler handle = new Handler();
    LayoutInflater inf;
    List<SongItem> songList = new ArrayList<>();
    MainActivity ctx;
    SongItem currentSong;

    public SongListAdaptor(MainActivity ctx){
        this.ctx = ctx;
        inf = LayoutInflater.from(ctx);
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
    public int getCount(){
        return songList.size();
    }

    @Override
    public SongItem getItem(int p1){
        return songList.get(p1);
    }

    @Override
    public long getItemId(int p1){
        return p1;
    }

    //@Override
    public CharSequence[] getAutofillOptions()
    {
        return null;
    }

    @Override
    public View getView(int pos, View p2, ViewGroup p3)
    {
        final SongItem item = songList.get(pos);

        View v = inf.inflate(R.layout.songlist_item, null, false);
        TextView title = (TextView) v.findViewById(R.id.songlist_itemTitle);
        title.setText(item.title);
        //
        TextView artist = (TextView) v.findViewById(R.id.songlist_itemArtist);
        artist.setText(item.artist);
        //
        final ImageView art = (ImageView) v.findViewById(R.id.songlist_item_albumart);
        if(item.icon!=null){
            art.setBackgroundDrawable(new BitmapDrawable(ctx.getResources(),item.icon));
        }else{
            
            //*
            SongInfoUpdater.updateSI(item, new SongInfoUpdater.ResultCallback(){
                public void onresult(){
                    if(item.icon!=null){
                        art.setBackgroundDrawable(new BitmapDrawable(ctx.getResources(),item.icon));
                    }
                }
            });
            //*/
            
        }
            
        ((View) v.findViewById(R.id.songlist_item_more)).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new SongOptions(ctx, item).show();
            }
        });
        
        TintedImageView mv = (TintedImageView) v.findViewById(R.id.songlist_itemsleepchild_view_T2);
        
        if(currentSong!=null && currentSong.equals(item)){
            v.setBackgroundResource(R.color.color2);
            title.setTextColor(ctx.getColor(R.color.pluto));
            artist.setTextColor(ctx.getColor(R.color.pluto));
            
            mv.setTint(ctx.getColor(R.color.pluto));
        }else{
            //mv.setTint(ctx.getColor(R.color.white));
        }
        v.setTag(item);
        return v;
    }

}
