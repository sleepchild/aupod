package sleepchild.aupod22;
import java.util.*;
import sleepchild.aupod22.models.*;

public class PlayQueue{
    
    private String qname;
    SongItem selectedSong;
    List<SongItem> slist;
    int songIndex=0;
    
    private List<SongItem> songlist = new ArrayList<>();
    
    
    public PlayQueue(String name, SongItem startSong, List<SongItem> list){
        qname = name;
    }
    
    public SongItem getSelectedSong(){
        
        return selectedSong;
    }
    
    public String getName(){
        return qname;
    }
    
    public void getList(){
        //
    }
    
    // should this be allowed
    public void rename(String newName){
        //
    }
    
    public void addSong(SongItem song){
        //
    }
}
