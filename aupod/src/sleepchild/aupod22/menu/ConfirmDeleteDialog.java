package sleepchild.aupod22.menu;
//import android.content.*;
import android.app.*;
import android.view.*;
import android.content.*;
import sleepchild.aupod22.models.*;

public class ConfirmDeleteDialog
{
    String path;
    Activity ctx;
    AlertDialog dlg;
    public ConfirmDeleteDialog(Activity ctx, SongItem si){
       this.ctx = ctx; 
       String a = si.artist=="<unknown>"? "": " - "+si.artist;
       String msg = si.title+a;
       dlg = new AlertDialog.Builder(ctx)
           .setMessage(msg)
           .setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface p1, int p2)
                {
                    //
                }
            })
            .setNegativeButton("cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface p1, int p2)
                {
                    // TODO: Implement this method
                }
            })
           .create();
       dlg.setTitle("Are you sure to delete");
       //dlg.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
       //
    }
    
    public void show(){
        dlg.show();
    }
}
