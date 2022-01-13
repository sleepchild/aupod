package sleepchild.aupod22.models.events;

import sleepchild.aupod22.models.SongItem;

public class SongResumeEvent
{
    public SongItem songitem;
    public SongResumeEvent(SongItem songitem){
        this.songitem = songitem;
    }
}
