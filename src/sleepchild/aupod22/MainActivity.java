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
import sleepchild.aupod22.postmanmodels.*;
import sleepchild.aupod22.tabs.*;
import sleepchild.view.*;
import sleepchild.view.tabview.Tab;
import sleepchild.view.tabview.TabView;

public class MainActivity extends BaseActivity
{
    AudioService aupod;
    ImageView currentImage;
    public final static int REQUEST_CODE = 22466;
    ListView list001;
    SongListAdaptor sAdaptor;
    TextView currentsongTitle, currentsongArtist;
    ImageView miniPlayPause;
    TabView tabview;
    SongsListTab songsTab;
    ArtistTab artistTab;
    AlbumsTab albumsTab;
    PlaylistsTab playlistTab;
    
    SearchPanel searchPanel;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        init();
    }
    //*
    private void init(){
        //
        currentsongTitle = (TextView) findViewById(R.id.activity_main_tv_title);
        currentsongArtist = (TextView) findViewById(R.id.activity_main_tv_artist);
        currentImage = (ImageView) findViewById(R.id.activity_main_iv_currentimage);
        miniPlayPause = (ImageView)findViewById(R.id.activity_main_iv_playpause);
        searchPanel = new SearchPanel(this);
        
        tabview = (TabView) findViewById(R.id.activity_main_tabview1);
        
        songsTab = new SongsListTab(this);
        artistTab = new ArtistTab(this);
        albumsTab = new AlbumsTab(this);
        playlistTab = new PlaylistsTab(this);
        
        tabview.addTab("SONGS", songsTab);
        tabview.addTab("ARTIST", artistTab);
        tabview.addTab("ALBUMS", albumsTab);
        tabview.addTab("PLAYLIST",playlistTab);
        
        tabview.showTab(0);
    }
    
    public void onClick(View v){
        int id = v.getId();
        switch(id){
            case R.id.activity_main_btn_showsettings:
                startActivity(SettingsActivity.class);
                break;
            case R.id.activity_main_btn_search:
                searchPanel.show();
                break;
            case R.id.activity_main_btn_playpause:
                if(aupod!=null){
                    aupod.playPause();
                }
                break;
            case R.id.activity_main_btn_next:
                if(aupod!=null){
                    aupod.playNext();
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
                //songsTab.showCurrent();
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
        //*
        if(aupod!=null){
            if(aupod.isPlaying()){
                miniPlayPause.setBackgroundResource(R.drawable.ic_pause);
            }else{
                miniPlayPause.setBackgroundResource(R.drawable.ic_play);
            }
        }
        //*/
    }
    
    
    private void updateCurrentInfo(){
        //todo: merge in @updateButtons methods
        if(aupod==null){return;}
        //*
        final SongItem si = aupod.getCurrentSong();
        currentsongTitle.setText(si.title);
        currentsongArtist.setText(si.artist);
        if(si.icon!=null){
            currentImage.setBackgroundDrawable(new BitmapDrawable(getResources(), si.icon));
        }else{
            SongFactory.get().updateSong(si, new SongFactory.UCB(){
                @Override
                public void onResult(final SongItem item){
                    runOnUiThread(new Runnable(){
                        public void run(){
                            si.icon = item.icon;
                            currentImage.setBackgroundDrawable(Utils.bmpToDrawable(ctx, si.icon));
                            if(si.icon==null){
                                currentImage.setBackgroundResource(R.drawable.fallback_cover);
                            }
                        }
                    });
                }
            });
        }
        songsTab.setCurrent(si);
        //*/
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

    @Override
    public void onBackPressed()
    {
        if(searchPanel.isVisible()){
           searchPanel.hide(); 
        }else{
            super.onBackPressed();
        }
        
    }
    
    
}
