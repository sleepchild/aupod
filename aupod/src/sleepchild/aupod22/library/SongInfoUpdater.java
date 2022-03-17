package sleepchild.aupod22.library;
import java.util.*;
import sleepchild.aupod22.models.*;
import com.mpatric.mp3agic.*;
import java.io.*;
import android.graphics.*;
import sleepchild.aupod22.*;

public class SongInfoUpdater
{
    private List<SongItem> mlist;
    
    private SongInfoUpdater(List<SongItem> list){
       // 
    }
    
    public static List<SongItem> update(List<SongItem> list){
         for(SongItem si : list){
             updateSI(si);
         }
         return list;
    }
    
    public static void updateSI(SongItem si){
        if(si.icon!=null){
            return;
        }
        try{
            Mp3File m3 = new Mp3File(si.path);
            ID3v2 v2 = m3.getId3v2Tag();
            if(v2!=null){
                byte[] b = v2.getAlbumImage();
                if(b!=null){
                    Bitmap bmp = BitmapFactory.decodeByteArray(b,0, b.length);
                    if(bmp!=null){
                        si.icon = bmp;
                    }
                    
                }
            }
        }
        catch (UnsupportedTagException e){}
        catch (IOException e){}
        catch (InvalidDataException e){}
        //
    }
    
    public static void updateSI(final SongItem si, final ResultCallback cb){
        App.runInBackground(new Runnable(){
            public void run(){
                updateSI(si);
                if(cb!=null){
                    App.runInUiThread(new Runnable(){
                        public void run(){
                            cb.onresult();
                        }
                    });
                    //cb.onresult();
                }
            }
        });
    }
    
    public static interface ResultCallback{
        public void onresult();
    }
    
    
}
