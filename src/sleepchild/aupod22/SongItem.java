package sleepchild.aupod22;

import android.graphics.Bitmap;

public class SongItem
{
    public String title="";
    public String artist="";
    public String path="";
    public String album="";
    public long duration=0;
    public Bitmap icon=null;
    public boolean updated=false;
    
    public SongItem(){
        //
    }
    
    public SongItem(String path){
        this.path = path;
    }
}
