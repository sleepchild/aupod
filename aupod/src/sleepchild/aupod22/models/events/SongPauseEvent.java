package sleepchild.aupod22.models.events;

import sleepchild.aupod22.models.SongItem;

public class SongPauseEvent
{
    public SongItem songitem;
    public SongPauseEvent(SongItem songitem){
        this.songitem = songitem;
    }
}
