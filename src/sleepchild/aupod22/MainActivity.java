package sleepchild.aupod22;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.os.*;
import android.graphics.*;
import android.net.*;
import android.graphics.drawable.*;
import mrtubbs.postman.*;
import mrtubbs.postman.sampleobjects.*;
import sleepchild.aupod22.postmanmodels.*;
//import sleepchild.view.*;

public class MainActivity extends BaseActivity
{
    AudioService aupod;
    ImageView currentImage;
    public final static int requestCode = 22466;
    ListView list001;
    SongListAdaptor sAdaptor;
    TextView currentsongTitle, currentsongArtist;
    ImageView miniPlayPause;
    TabController tabController;
    SongsListTab songsTab;
    View cv;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        init();
        getWindow().setNavigationBarColor(getResources().getColor(R.color.pluto));
        //AudioService.start(this);
    }
    //*
    private void init(){
        //
        currentsongTitle = (TextView) findViewById(R.id.activity_main_tv_title);
        currentsongArtist = (TextView) findViewById(R.id.activity_main_tv_artist);
        currentImage = (ImageView) findViewById(R.id.activity_main_iv_currentimage);
        miniPlayPause = (ImageView)findViewById(R.id.activity_main_iv_playpause);
        tabController = new TabController(this);
        songsTab = new SongsListTab(this);
        tabController.add(songsTab);//index 0
        
        tabController.show(0);
        cv = findViewById(R.id.activity_main_tabtoggle_songs);
    }
    
    public void switchTab(View v){
        int id = v.getId();
        switch(id){
            case R.id.activity_main_tabtoggle_songs:
                tabController.show(0);
                break;
            case R.id.activity_main_tabtoggle_artists:
                toast("w.i.p");
                break;
            case R.id.activity_main_tabtoggle_albums:
                toast("w.i.p");
                break;
            case R.id.activity_main_tabtoggle_playlists:
                toast("w.i.p");
                break;
        }
        cvc(v);
    }
    public void cvc(View v){
        
        cv.setBackgroundColor(0);
        TextView tv = (TextView)((LinearLayout)cv).getChildAt(0);
        tv.setTextColor(getColor(R.color.white));
    
        v.setBackgroundColor(getResources().getColor(R.color.white));
        tv = (TextView)((LinearLayout)v).getChildAt(0);
        tv.setTextColor(getColor(R.color.black));
        
        cv = v;
    }
    
    public void onClick(View v){
        int id = v.getId();
        switch(id){
            case R.id.activity_main_showsettings:
                startActivity(SettingsActivity.class);
                break;
            case R.id.activity_main_btn_playpause:
                if(aupod!=null){
                    aupod.playPause();
                }
                PostMan.getInstance().post(new StringMess("a mesage"));
                break;
            case R.id.activity_main_btn_next:
                if(aupod!=null){
                    aupod.playnext();
                }
                break;
        }
    }
    
    public void showFullPlayer(View v){
        startActivity(PlayerActivity.class);
    }
    
    @PostEvent
    public void onServiceConnect(AudioServiceConnect event){
        //toast("connect!");
        aupod = event.service;
        aupod.getSongList();
    }
    
    @PostEvent
    public void onSongListReady(final SongsListReadyEvent event){
        runOnUiThread(new Runnable(){
            public void run(){
                //toast(""+event.songList.size());
                songsTab.update(event.songList);
                updateCurrentInfo();
                updateButtons();
            }
        });
    }
    
    @PostEvent
    public void onSongResume(SongResumeEvent evt){
        runOnUiThread(new Runnable(){
            public void run(){
                updateCurrentInfo();
                updateButtons();
            }
        });
    }
    
    @PostEvent
    public void OnSongPaused(SongPauseEvent event){
        runOnUiThread(new Runnable(){
            public void run(){
                updateCurrentInfo();
                updateButtons();
            }
        });
    }
    
    private void updateButtons(){
        if(aupod!=null){
            if(aupod.isPlaying()){
                miniPlayPause.setBackgroundResource(R.drawable.ic_pause);
            }else{
                miniPlayPause.setBackgroundResource(R.drawable.ic_play);
            }
        }
    }
    
    
    private void updateCurrentInfo(){
        if(aupod==null){return;}
        SongItem si = aupod.getCurrentSong();
        currentsongTitle.setText(si.title);
        currentsongArtist.setText(si.artist);
        if(si.icon!=null){
            currentImage.setBackgroundDrawable(new BitmapDrawable(getResources(), si.icon));
        }else{
            Imgur.with(this).load(si.artUri).callback(new Imgur.ImgurResult(){
                @Override
                public void onImage(Bitmap image)
                {
                    currentImage.setBackgroundDrawable(new BitmapDrawable(getResources(), image));
                }
            }).start();
        }
        //findViewById(R.id.u56).invalidate();
        //list001.setSelection(sAdaptor.getPos(si));
    }

    @Override
    protected void onPause()
    {
        PostMan.getInstance().unregister(this);
        super.onPause();
    }
    
    @Override
    protected void onResume()
    {
        PostMan.getInstance().register(this);
        super.onResume();
        AudioService.start(this);
    }
    
    
}
