package sleepchild.aupod22.menu;

import android.app.Activity;
import sleepchild.aupod22.models.*;
import android.app.*;
import sleepchild.aupod22.*;
import android.view.*;

public class CurrentSongOptions
{
    Activity act;
    SongItem si;
    
    Dialog dlg;
    
    public CurrentSongOptions(Activity ctx, SongItem song){
        this.act = ctx;
        this.si = song;
        //
        dlg = new Dialog(ctx);
        dlg.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dlg_auplayer_options);
    }
    
    public void show(){
        dlg.show();
    }
    
    public void dismiss(){
        dlg.dismiss();
    }
}
