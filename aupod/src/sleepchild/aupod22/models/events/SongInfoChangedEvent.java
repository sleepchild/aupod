package sleepchild.aupod22.models.events;

import sleepchild.aupod22.models.SongItem;

public class SongInfoChangedEvent
{
    public SongItem songitem;
    public SongInfoChangedEvent(SongItem si){
        this.songitem = si;
    }
}
