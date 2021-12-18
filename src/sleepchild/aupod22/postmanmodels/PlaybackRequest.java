package sleepchild.aupod22.postmanmodels;

public class PlaybackRequest{
    
    public static class TYPE{
        public static final int PLAY_SONGITEM = 371;
        public static final int PLAY = 372;
        public static final int PAUSE = 373;
        public static final int STOP = 377;
    }
    
    private int mType;
    public Object object;
    
    public PlaybackRequest(int type, Object obj){
        this.mType = type;
        this.object = obj;
    }
    
    public int getType(){
        return mType;
    }
}
