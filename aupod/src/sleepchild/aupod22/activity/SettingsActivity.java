package sleepchild.aupod22.activity;

import android.os.*;
import android.view.*;
import sleepchild.aupod22.activity.*;
import sleepchild.aupod22.*;

public class SettingsActivity extends BaseActivity
{
    public static final int requetsCode = 333749;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //
        init();
    }

    public void init(){
        //
    }

    public void onClick(View v){
        int id = v.getId();
        switch(id){
            case R.id.settings_item_interface:
                //
                break;
            case R.id.settings_item_medialibrary:
                //
                break;
            case R.id.settings_item_headsetbt:
                //
                break;
        }
        toast(""+id);
    }


}
