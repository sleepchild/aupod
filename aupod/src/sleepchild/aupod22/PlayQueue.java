package sleepchild.aupod22;
import java.util.*;
import sleepchild.aupod22.models.*;

public class PlayQueue{
    private String Qname;
    
    private List<SongItem> songlist = new ArrayList<>();
    
    public PlayQueue(String name, String songpath){
        //
    }
    
    public PlayQueue(String name, SongItem song){
        //
    }
    
    public PlayQueue(String name, Playlist list){
        //
    }
    
    public PlayQueue(String name, List<SongItem> list){
        if(list!=null){
            //
        }
    }
    
    public String getName(){
        return Qname;
    }
    
    public void rename(String newName){
        //
    }
    
    public void addSong(SongItem song){
        //
    }
}
