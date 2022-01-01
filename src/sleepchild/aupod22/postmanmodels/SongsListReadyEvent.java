package sleepchild.aupod22.postmanmodels;
import java.util.*;
import sleepchild.aupod22.*;

public class SongsListReadyEvent
{
    public List<SongItem> songList;
    public SongsListReadyEvent(List<SongItem> songlist){
        this.songList = songlist;
    }
}
