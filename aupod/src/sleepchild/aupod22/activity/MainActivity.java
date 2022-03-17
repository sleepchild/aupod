package sleepchild.aupod22.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.os.*;
import android.graphics.*;
import android.graphics.drawable.*;
import sleepchild.aupod22.models.*;
import sleepchild.aupod22.tabs.*;
import sleepchild.view.*;
import sleepchild.view.tabview.Tab;
import sleepchild.view.tabview.TabView;
import sleepchild.aupod22.activity.*;
import sleepchild.aupod22.adapters.*;
import sleepchild.aupod22.*;
import sleepchild.aupod22.utils.*;
import sleepchild.aupod22.library.*;
import sleepchild.aupod22.service.*;

public class MainActivity extends BaseActivity implements 
    AudioService.ConnectionListener, 
    APEvents.PlaybackStateListener, 
    APEvents.SongListUpdateListener{
    
    AudioService aupod;
    ImageView currentImage;
    public final static int REQUEST_CODE = 22466;
    ListView list001;
    SongListAdaptor sAdaptor;
    TextView currentsongTitle, currentsongArtist;
    TintedImageView btnPlayPause;
    TabView tabview;
    SongsListTab songsTab;
    ArtistTab artistTab;
    AlbumsTab albumsTab;
    PlaylistsTab playlistTab;
    SongsQueueTab sqTab;

    SearchPanel searchPanel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},123);
        init();
    }
    //*
    private void init(){
        //
        currentsongTitle = (TextView) findViewById(R.id.activity_main_tv_title);
        currentsongArtist = (TextView) findViewById(R.id.activity_main_tv_artist);
        currentImage = (ImageView) findViewById(R.id.activity_main_iv_currentimage);
        btnPlayPause = (TintedImageView)findViewById(R.id.activity_main_iv_playpause);
        btnPlayPause.setBackgroundResource(R.drawable.ic_play);
        
        searchPanel = new SearchPanel(this);

        tabview = (TabView) findViewById(R.id.activity_main_tabview1);

        songsTab = new SongsListTab(this);
        sqTab = new SongsQueueTab(this);
        artistTab = new ArtistTab(this);
        albumsTab = new AlbumsTab(this);
        playlistTab = new PlaylistsTab(this);

        //tabview.addTab("QUEUE", sqTab);
        tabview.addTab("SONGS", songsTab);
        tabview.addTab("ARTIST", artistTab);
        tabview.addTab("ALBUMS", albumsTab);
        tabview.addTab("PLAYLIST",playlistTab);

        tabview.showTab(songsTab);
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

    @Override
    public void onAudioServiceConnect(AudioService service){
        aupod = service;
        APEvents.getInstance().addPlaybackEventListener(this);
        aupod.getSongsList();
    }

    int t=1;
    @Override
    public void onSongsListUpdated(List<SongItem> list){
        songsTab.update(list);
       // toast("onSongsListUpdated:"+t);
        t++;
        if(aupod!=null){
            SongItem si = aupod.getCurrentSong();
            if(si!=null){
                songsTab.setCurrent(si);
            }
        }
    }    
    
    public AudioService getAudioService(){
        return aupod;
    }
    
    @Override
    public void onPlaybackStart()
    {
         btnPlayPause.setBackgroundResource(R.drawable.ic_pause);
         btnPlayPause.reset();
    }

    @Override
    public void onPlaybackPause()
    {
        btnPlayPause.setBackgroundResource(R.drawable.ic_play);
        btnPlayPause.reset();
    }

    @Override
    public void onPlaybackStop()
    {
        // TODO: Implement this method
    }

    @Override
    public void onSongChanged(final SongItem si){
        currentsongTitle.setText(si.title);
        currentsongArtist.setText(si.artist);
        if(si.icon!=null){
            currentImage.setBackgroundDrawable(new BitmapDrawable(si.icon));
        }else{
            currentImage.setBackgroundResource(R.drawable.fallback_cover);
            SongInfoUpdater.updateSI(si, new SongInfoUpdater.ResultCallback(){
                @Override
                public void onresult(){
                    if(si.icon!=null){
                        currentImage.setBackgroundDrawable(new BitmapDrawable(si.icon));
                    }else{
                        currentImage.setBackgroundResource(R.drawable.fallback_cover);
                    }
                }
            });
        }
        //
        songsTab.setCurrent(si);
    }
    
    @Override
    protected void onPause(){
        //
        APEvents.getInstance().removePlaybackEventListener(this);
        APEvents.getInstance().removeSongsListUpdateListener(this);
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        APEvents.getInstance().addPlaybackEventListener(this);
        APEvents.getInstance().addSongsListUpdateListener(this);
        //SongLibrary.getInstance().update(this);
        AudioService.connect(this, this);
        //
    }

    @Override
    public void onBackPressed() {
        if(searchPanel.isVisible()){
            searchPanel.hide(); 
        }else{
            super.onBackPressed();
        }
    }


}
