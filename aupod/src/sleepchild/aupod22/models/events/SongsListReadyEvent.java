package sleepchild.aupod22.models.events;

import java.util.List;
import sleepchild.aupod22.models.*;

public class SongsListReadyEvent
{
    public List<SongItem> songList;
    public SongsListReadyEvent(List<SongItem> songlist){
        this.songList = songlist;
    }
}
