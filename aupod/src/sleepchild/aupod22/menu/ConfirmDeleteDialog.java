package sleepchild.aupod22.menu;
//import android.content.*;
import android.app.*;
import android.view.*;
import android.content.*;
import sleepchild.aupod22.models.*;
import sleepchild.aupod22.*;
import sleepchild.aupod22.activity.*;
import sleepchild.aupod22.service.*;

public class ConfirmDeleteDialog
{
    String path;
    MainActivity act;
    AlertDialog dlg;
    public ConfirmDeleteDialog(MainActivity ctx,final SongItem si){
       this.act = ctx; 
       String a = (si.artist=="<unknown>"? "": " - ")+si.artist;
       String msg = si.title+a;
       dlg = new AlertDialog.Builder(act)
           .setMessage(msg)
           .setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface p1, int p2)
                {
                    AudioService au = App.get().getAudioService();
                    if(au!=null){
                        au.deleteSong(si);
                    }
                    dlg.dismiss();
                }
            })
            .setNegativeButton("cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface p1, int p2)
                {
                    dlg.dismiss();
                }
            })
           .create();
       //dlg.getWindow().setBackgroundDrawableResource(R.color.pluto);
       dlg.setTitle("Are you sure to delete");
       //dlg.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
       //
    }
    
    public void show(){
        dlg.show();
    }
}
