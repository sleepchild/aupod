package sleepchild.aupod22.tasks;
import android.os.*;
import android.content.*;
import sleepchild.aupod22.*;
import java.util.*;
import com.mpatric.mp3agic.*;
import java.io.*;
import android.graphics.*;

public class UpdateSongListTask extends AsyncTask<Void, Void, List<SongItem>>
{
    List<SongItem> songlist = new ArrayList<>();
    AudioService audios;
    
    public UpdateSongListTask(AudioService service, List<SongItem> oldlist){
        this.songlist = oldlist;
        this.audios = service;
    }

    @Override
    protected List<SongItem> doInBackground(Void[] p1)
    {
        for(SongItem si : songlist){
            si = update(si);
        }
        return songlist;
    }
    
    private SongItem update(SongItem si){
        try
        {
            Mp3File mp3 = new Mp3File(si.path);
            if(mp3.hasId3v2Tag()){
                ID3v2 v2 = mp3.getId3v2Tag();
                String t = v2.getTitle();
                if(t!=null && !t.isEmpty()){
                    //
                }
                byte[] art = v2.getAlbumImage();
                if(art!=null){
                    si.icon = BitmapFactory.decodeByteArray(art,0,art.length);
                }
            }
        }
        catch (InvalidDataException e)
        {}
        catch (UnsupportedTagException e)
        {}
        catch (IOException e)
        {}
        return si;
    }

    @Override
    protected void onPostExecute(List<SongItem> result)
    {
        audios.onSongListUpdated(result);
        super.onPostExecute(result);
    }
    
}
