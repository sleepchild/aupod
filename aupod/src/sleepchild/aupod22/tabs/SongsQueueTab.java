package sleepchild.aupod22.tabs;
import sleepchild.view.tabview.*;
import android.view.*;
import sleepchild.aupod22.activity.*;
import sleepchild.aupod22.*;

public class SongsQueueTab extends Tab
{
    View root;
    MainActivity act;
    
    public SongsQueueTab(MainActivity act){
        this.act = act;
        root = act.getLayoutInflater().inflate(R.layout.songsqueuetab, null, false);
        
        //
    }

    @Override
    public View getView()
    {
        return root;
    }

    
}
