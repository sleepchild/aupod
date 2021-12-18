package sleepchild.aupod22.postmanmodels;
import sleepchild.aupod22.*;

public class SongCompletionEvent
{
    private SongItem si;
    public SongCompletionEvent(SongItem si){
        this.si = si;
    }
    
    public SongItem getLstSong(){
        return si;
    }
}
