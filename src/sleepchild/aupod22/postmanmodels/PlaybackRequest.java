package sleepchild.aupod22.postmanmodels;

public class PlaybackRequest
{
    public enum PLAYBACK_TYPE{
        PLAY,
        PLAY_SONGITEM,
        PAUSE,
        STOP
    }
    
    private PLAYBACK_TYPE mType;
    public Object object;
    
    public PlaybackRequest(PLAYBACK_TYPE type, Object obj){
        this.mType = type;
        this.object = obj;
    }
    
    public PLAYBACK_TYPE getType(){
        return mType;
    }
}
