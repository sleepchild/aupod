package sleepchild.aupod22;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.*;
import android.graphics.*;
import android.graphics.drawable.*;

public class SongListAdaptor extends BaseAdapter
{
    LayoutInflater inf;
    List<SongItem> songList = new ArrayList<>();
    Context ctx;
    
    public SongListAdaptor(Context ctx){
        this.ctx = ctx;
        inf = LayoutInflater.from(ctx);
    }
    
    public void update(List<SongItem> list){
        this.songList = list;
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
        // TODO: Implement this method
        return songList.get(p1);
    }

    @Override
    public long getItemId(int p1)
    {
        // TODO: Implement this method
        return p1;
    }

    @Override
    public CharSequence[] getAutofillOptions()
    {
        // TODO: Implement this method
        return null;
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
        
        final ImageView art = (ImageView) v.findViewById(R.id.songlist_item_albumart);
        if(item.icon==null){
            Imgur.with(ctx).load(item.artUri).callback(new Imgur.ImgurResult(){
                public void onImage(Bitmap image){
                    item.icon = image;
                    art.setBackground(new BitmapDrawable(ctx.getResources(), image));
                    //art.setImageBitmap(image);
                }
            }).start();
        }else{
            art.setBackground(new BitmapDrawable(ctx.getResources(), item.icon));
        }
        //
        v.setTag(item);
        return v;
    }
    
}
