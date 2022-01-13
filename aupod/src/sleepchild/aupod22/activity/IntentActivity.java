package sleepchild.aupod22.activity;

import android.os.Bundle;
import android.content.*;
import android.view.*;
import sleepchild.aupod22.R;

public class IntentActivity extends BaseActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iplayer);
        Intent i = getIntent();
        String path = clean(i.getDataString());
        //AudioService.playSong(this,path);
        finish();
        //startActivity(PlayerActivity.class);
    }

    String clean(String str){
        return str.replaceAll("%20"," ");
    }



}