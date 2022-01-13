package sleepchild.aupod22.models.events;

import sleepchild.aupod22.models.SongItem;

public class SongChangedEvent{
    private SongItem si;
    public SongChangedEvent(SongItem si){
        this.si = si;
    }
    public SongItem getSong(){
        return si;
    }
}
