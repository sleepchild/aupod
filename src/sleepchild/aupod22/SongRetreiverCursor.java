package sleepchild.aupod22;
import android.database.*;
import android.content.*;
import android.provider.*;
import java.util.*;
import android.net.*;

public class SongRetreiverCursor
{
    Context ctx;
    public SongRetreiverCursor(Context ctx){
        this.ctx = ctx;
    }
    
    public static List<SongItem> get(Context ctx){
        List<SongItem> stitles = new ArrayList<>();
        
        Uri suri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
		String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        
        Cursor c = ctx.getContentResolver().query(
        suri,
        null,
        selection,
        null,
        sortOrder);
        
        if(c==null || c.getCount() ==0){
            return null;
        }
        
        c.moveToFirst();
        
        do{
            SongItem si = new SongItem();
            
            si.title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
            si.artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            si.path = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
            int artid = c.getInt(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
            si.artUri = ContentUris.withAppendedId(albumArtUri, artid);
            
            stitles.add(si);
            //
        }while(c.moveToNext());
        
        c.close();
        
        return stitles;
    }
    
   
    
    
}
