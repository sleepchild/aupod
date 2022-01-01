package sleepchild.aupod22.menu;

import sleepchild.aupod22.BaseActivity;
import android.content.*;
import android.widget.*;
import android.view.*;
import android.app.*;
import sleepchild.aupod22.SongItem;
import sleepchild.aupod22.TagEditorActivity;
import sleepchild.aupod22.R;
import sleepchild.aupod22.*;

public class SongOptions implements View.OnClickListener
{
    BaseActivity ctx;
    SongItem si;
    RelativeLayout container;
    TextView title, artist;
    Dialog dlg;
    
    
    public SongOptions(BaseActivity ctx, SongItem si){
        this.ctx = ctx;
        this.si = si;
        dlg = new Dialog(ctx);
        Window w = dlg.getWindow();
        w.requestFeature(Window.FEATURE_NO_TITLE);
        
        dlg.setContentView(R.layout.popup_auplayer_songoptions);
        //
        title = ftv(R.id.popup_auplayer_songoptions_title);
        artist = ftv(R.id.popup_auplayer_songoptions_artist);
        
        title.setText(si.title);
        artist.setText(si.artist);
        
        LinearLayout ops = (LinearLayout) fv(R.id.popup_auplayer_songoptions_clk);
        int cc = ops.getChildCount();
        for(int i=0;i<cc;i++){
            View v = ops.getChildAt(i);
            v.setOnClickListener(this);
            String f = getFlag(v);
            if(f!=null){
                if(f.equals("MainActivity") && !(ctx instanceof MainActivity) ){
                    v.setVisibility(View.GONE);
                }else if(f.equals("PlayerActivity") && !(ctx instanceof PlayerActivity) ){
                    v.setVisibility(View.GONE);
                }
            }
        }
    }
    
    private String getFlag(View v){
        String[] p = getValues(v);
        if(p!=null && p.length>=2){
            return p[1];
        }
        return null;
    }
    
    private String[] getValues(View v){
        if(v.getTag()!=null && (v.getTag() instanceof String)){
            String t = v.getTag().toString();
            String[] p = t.split(" ");
            return p;
        }
        return null;
    }
    
    private View fv(int resid){
        return dlg.findViewById(resid);
    }
    
    private TextView ftv(int resid){
        return (TextView) fv(resid);
    }
    
    
    public void show(){
        dlg.show();
    }
    
    public void hide(){
        dlg.dismiss();
        title.setText("");
        artist.setText("");
        si = null;
    }

    @Override
    public void onClick(View v)
    {
        if(v.getTag()!=null){
            String tag = v.getTag().toString();
            if(tag!=null){
                tag = tag.split(" ")[0];
            }
            switch(tag){
                case "showinfo":
                    //
                    break;
                case "addtoplaylist":
                    //
                    break;
                case "editfiletags":
                    TagEditorActivity.start(ctx, si);
                    break;
                case "deletesong":
                    new ConfirmDeleteDialog(ctx, si).show();
                    break;
                case "showsettings":
                    ctx.startActivity(SettingsActivity.class);
                    break;
                /*
                case "":
                    //
                    break;
                case "":
                    //
                    break;
                //*/
                
            }
        }
        hide();
    }
    
}
