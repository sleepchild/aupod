package sleepchild.aupod22.postmanmodels;
import sleepchild.aupod22.*;

public class SongPauseEvent
{
    public SongItem songitem;
    public SongPauseEvent(SongItem songitem){
        this.songitem = songitem;
    }
}
