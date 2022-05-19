package sleepchild.aupod22.activity;

import android.os.Bundle;
import android.content.*;
import android.view.*;
import sleepchild.aupod22.R;
import sleepchild.aupod22.*;
import sleepchild.aupod22.models.*;
import sleepchild.aupod22.library.*;
import sleepchild.aupod22.service.*;

public class IntentActivity extends BaseActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iplayer);
        Intent i = getIntent();
        String path = clean(i.getDataString());
        //
        AudioService.playFromIntent(getApplicationContext(), path);
        
        //finish();
    }

    private String clean(String str){
        String cstr = str.replace("%20"," ");
        return cstr;
    }
    //
}
