package sleepchild.aupod22.library;

import android.database.*;
import android.content.*;
import android.provider.*;
import java.util.*;
import android.net.*;
import sleepchild.aupod22.models.*;
import java.io.*;

public class SongRetreiverCursor
{
    //Context ctx;
    private SongRetreiverCursor(){
        //this.ctx = ctx;
    }
    
    public static List<SongItem> get(Context ctx){
        
        List<SongItem> sitems = new ArrayList<>();
        if(ctx==null){return sitems;}
        
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
            return sitems;
        }
        c.moveToFirst();
        
        do{
            SongItem si = new SongItem();
            
            si.path = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
            
            if(new File(si.path).exists()){
                si.id = c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID));
                si.title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
                si.artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                si.album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                si.duration = c.getInt(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
                si.artId = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                //
                sitems.add(si);
            }
            
        }while(c.moveToNext());
        
        c.close();
        
        return sitems;
    }
    
}
