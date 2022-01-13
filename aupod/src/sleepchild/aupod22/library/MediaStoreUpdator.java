package sleepchild.aupod22.library;

import android.content.*;
import android.provider.*;
import android.net.*;
import java.nio.*;
import sleepchild.aupod22.models.*;

public class MediaStoreUpdator
{
    public MediaStoreUpdator(){
        //
    }
    
    public static void update(Context ctx, SongItem si, String artPath){
        ContentResolver cr = ctx.getContentResolver();
        ContentValues vals = new ContentValues();
        vals.put(MediaStore.Audio.Media.TITLE, si.title);
        vals.put(MediaStore.Audio.Media.ARTIST, si.artist);
        vals.put(MediaStore.Audio.Media.ALBUM, si.album);
        if(si.icon!=null && si.artId!=0 && artPath!=null){
            Uri artworkUris = Uri.parse("content://media/external/audio/albumart");
            cr.delete(ContentUris.withAppendedId(artworkUris, si.artId), null, null);
            
            ContentValues cv = new ContentValues();
            cv.put("album_id",si.artId);
            cv.put("_data", artPath);
            cr.insert(artworkUris, cv);
        }
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        cr.update(uri,vals,MediaStore.Audio.Media._ID +" = ? " , new String[]{si.id});
    }
}
