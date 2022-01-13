package sleepchild.aupod22.models;

import android.graphics.Bitmap;

public class SongItem
{
    public String id="";
    public String title="";
    public String artist="";
    public String path="";
    public String album="";
    public long duration=0;
    public Bitmap icon=null;
    public boolean updated=false;
    public long artId;

    public SongItem(){
        //
    }

    public SongItem(String path){
        this.path = path;
    }
}