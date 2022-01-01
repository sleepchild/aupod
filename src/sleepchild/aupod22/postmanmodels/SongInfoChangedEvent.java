package sleepchild.aupod22.postmanmodels;
import sleepchild.aupod22.*;

public class SongInfoChangedEvent
{
    public SongItem songitem;
    public SongInfoChangedEvent(SongItem si){
        this.songitem = si;
    }
}
