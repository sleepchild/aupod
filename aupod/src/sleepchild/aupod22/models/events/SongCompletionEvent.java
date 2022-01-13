package sleepchild.aupod22.models.events;

import sleepchild.aupod22.models.SongItem;

public class SongCompletionEvent
{
    private SongItem si;
    public SongCompletionEvent(SongItem si){
        this.si = si;
    }
    // return the song that just completed
    public SongItem getCompletedSong(){
        return si;
    }
}
