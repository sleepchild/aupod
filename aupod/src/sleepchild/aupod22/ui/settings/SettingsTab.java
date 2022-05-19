package sleepchild.aupod22.ui.settings;
import android.view.*;
import sleepchild.aupod22.activity.*;

// diy fragments
public abstract class SettingsTab
{
    private SettingsActivity act;
    
    public SettingsTab(SettingsActivity act){
        this.act = act;
    }
    
    public SettingsActivity getActivity(){
        return act;
    }
    
    public LayoutInflater getLayoutInflater(){
        return act.getLayoutInflater();
    } 
    
    public abstract View getView();
}
