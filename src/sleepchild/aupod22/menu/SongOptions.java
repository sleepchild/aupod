package sleepchild.aupod22.menu;

import sleepchild.aupod22.BaseActivity;
import android.content.*;
import android.widget.*;
import android.view.*;
import android.app.*;
import sleepchild.aupod22.SongItem;
import sleepchild.aupod22.TagEditorActivity;
import sleepchild.aupod22.R;

public class SongOptions implements View.OnClickListener
{
    BaseActivity ctx;
    SongItem si;
    RelativeLayout container;
    TextView title, artist;
    Dialog dlg;
    
    
    public SongOptions(BaseActivity ctx){
        this.ctx = ctx;
        dlg = new Dialog(ctx);
        Window w = dlg.getWindow();
        w.requestFeature(Window.FEATURE_NO_TITLE);
        
        dlg.setContentView(R.layout.popup_auplayer_songoptions);
        //
        title = ftv(R.id.popup_auplayer_songoptions_title);
        artist = ftv(R.id.popup_auplayer_songoptions_artist);
        
        LinearLayout ops = (LinearLayout) fv(R.id.popup_auplayer_songoptions_clk);
        int cc = ops.getChildCount();
        for(int i=0;i<cc;i++){
            View v = ops.getChildAt(i);
            v.setOnClickListener(this);
        }
    }
    
    private View fv(int resid){
        return dlg.findViewById(resid);
    }
    
    private TextView ftv(int resid){
        return (TextView) fv(resid);
    }
    
    public void show(SongItem si){
        if(si==null){return;}
        setup(si);
        dlg.show();
    }
    
    public void hide(){
        dlg.dismiss();
        clean();
    }
    
    private void setup(SongItem si){
        this.si = si;
        title.setText(si.title);
        artist.setText(si.artist);
    }
    
    private void clean(){
        title.setText("");
        artist.setText("");
        si = null;
    }

    @Override
    public void onClick(View v)
    {
        if(v.getTag()!=null){
            String tag = v.getTag().toString();
            switch(tag){
                case "showinfo":
                    //
                    break;
                case "addtoplaylist":
                    //
                    break;
                case "editfiletags":
                    hide();
                    ctx.startActivity(TagEditorActivity.class);
                    //hide();
                    break;
                case "deletesong":
                    //
                    break;
                /*
                case "":
                    //
                    break;
                case "":
                    //
                    break;
                case "":
                    //
                    break;
                //*/
                
            }
        }
    }
    
}
