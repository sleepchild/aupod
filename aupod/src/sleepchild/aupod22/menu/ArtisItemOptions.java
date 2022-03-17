package sleepchild.aupod22.menu;
import android.app.*;

public class ArtisItemOptions{
    Dialog dlg;
    Activity act;
    
    public ArtisItemOptions(Activity ctx){
        act = ctx;
        dlg = new Dialog(ctx);
    }
    
    public void show(){
        dlg.show();
    }
    
    public void dismiss(){
        dlg.dismiss();
    }
}
