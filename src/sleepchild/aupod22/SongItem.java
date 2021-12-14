package sleepchild.aupod22;
import android.graphics.*;
import android.net.*;

public class SongItem
{
    String title="";
    String artist="";
    String path="";
    long duration=0;
    Uri artUri;
    Bitmap icon=null;
    //public boolean hasIcon=true;
    
    public SongItem(){
        //
    }
    
    public SongItem(String path){
        this.path = path;
    }
}
