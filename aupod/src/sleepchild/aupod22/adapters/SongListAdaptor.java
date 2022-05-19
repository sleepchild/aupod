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
import sleepchild.aupod22.utils.*;

public class SongListAdaptor extends BaseAdapter
{
    Handler handle = new Handler();
    LayoutInflater inf;
    List<SongItem> songList = new ArrayList<>();
    MainActivity ctx;
    SongItem currentSong;
    ThemeManager.Theme theme;

    public SongListAdaptor(MainActivity ctx){
        this.ctx = ctx;
        inf = LayoutInflater.from(ctx);
        //theme = ThemeManager.get().getTheme();
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
    
    public void setTheme(ThemeManager.Theme theme){
        this.theme = theme;
        //notifyDataSetChanged();
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
        title.setTextColor(theme.text);
        //
        TextView artist = (TextView) v.findViewById(R.id.songlist_itemArtist);
        artist.setText(item.artist);
        artist.setTextColor(theme.text);
        //
        final ImageView art = (ImageView) v.findViewById(R.id.songlist_item_albumart);
        if(item.icon!=null){
            art.setBackgroundDrawable(new BitmapDrawable(ctx.getResources(),item.icon));
        }else{
            //art.setBackgroundResource(R.color.black);
            art.setImageBitmap(BitmapUtils.tint(ctx.getResources(), R.drawable.cover_f, theme.icon));
        }
            
        v.findViewById(R.id.songlist_item_more).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new SongOptions(ctx, item).show();
            }
        });
        v.setBackgroundColor(theme.background);
        
        TintedImageView more_icon = (TintedImageView) v.findViewById(R.id.songlist_items_more_icon);
        more_icon.setTint(theme.icon);
        
        if(currentSong!=null && currentSong.equals(item)){
            v.setBackgroundColor(theme.text);
            title.setTextColor(theme.background);
            artist.setTextColor(theme.background);
            
            more_icon.setTint(theme.background);
            
            if(cj.color!=theme.background){
            App.runInBackground(new Runnable(){
                public void run(){
                    final Bitmap b = BitmapUtils.tint(ctx.getResources(), R.drawable.cover_f, theme.background);
                    if(b!=null){
                        App.runInUiThread(new Runnable(){
                            public void run(){
                                art.setImageBitmap(b);
                                cj.bmp = b;
                                cj.color = theme.background;
                            } 
                        });
                    }
                }
            });
            }else{
                art.setImageBitmap(cj.bmp);
            }
        }
        
        v.setTag(item);
        return v;
    }
    
    class J{
        Bitmap bmp;
        int color;
    }
    J cj = new J();

}
