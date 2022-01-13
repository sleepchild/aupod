package sleepchild.aupod22.models;

public class PlaybackRequest{
    
    public static class TYPE{
        public static final int PLAY_SONGITEM = 371;
        public static final int RESUME = 372;
        public static final int PAUSE = 373;
        public static final int STOP = 377;
        public static final int DELETE = 988;
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
