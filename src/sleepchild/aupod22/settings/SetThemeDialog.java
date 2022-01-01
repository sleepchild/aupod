package sleepchild.aupod22.settings;

import android.view.*;
import android.app.*;
import sleepchild.aupod22.*;

public class SetThemeDialog implements View.OnClickListener    
{
    private SettingsActivity act;
    private Dialog dlg;
    public SetThemeDialog(SettingsActivity act){
        this.act = act;
        this.dlg = new Dialog(act);
        dlg.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //dlg.setContentView();
    }

    @Override
    public void onClick(View p1)
    {
        // TODO: Implement this method
    }
}
