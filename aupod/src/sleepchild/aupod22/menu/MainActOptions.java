package sleepchild.aupod22.menu;
import android.app.*;
import sleepchild.aupod22.*;
import android.content.*;
import android.view.*;
import sleepchild.aupod22.activity.*;

public class MainActOptions extends MDialog
{
    private MainActivity act;
    
    public MainActOptions(MainActivity a){
        super(a, R.layout.dlg_mainact_options);
        act = a;
        init();
    }
    
    void init(){
        //
        setClickable(R.id.mainact_options_settings);
        setClickable(R.id.mainact_options_exit);
    }

    @Override
    public void onShow(DialogInterface i){
        getWindow().setGravity(Gravity.RIGHT|Gravity.TOP);
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        switch(id){
            case R.id.mainact_options_settings:
                act.startActivity(SettingsActivity.class);
                break;
            case R.id.mainact_options_exit:
                XApp.exit();
                break;  
        }
        dismiss();
    }
    
    
    
    
}
