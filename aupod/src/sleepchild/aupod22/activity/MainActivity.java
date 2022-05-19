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
import sleepchild.aupod22.ThemeManager.*;
import sleepchild.aupod22.menu.*;

public class MainActivity extends BaseActivity implements 
    AudioService.ConnectionListener, 
    APEvents.PlaybackStateListener, 
    APEvents.SongListUpdateListener,
    SearchPanel.OnSearchResultItemClickListener
{
    
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
    
    List<SongItem> songslist;
    
    Handler handle = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setThemeable(true);
        App.get().setMainActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},123);
        init();
    }
    //*
    private void init(){
        //
        currentsongTitle = findView(R.id.activity_main_tv_title);
        currentsongArtist = findView(R.id.activity_main_tv_artist);
        currentImage = findView(R.id.activity_main_iv_currentimage);
        btnPlayPause = findView(R.id.activity_main_iv_playpause);
        btnPlayPause.setBackgroundResource(R.drawable.ic_play);
        
        searchPanel = new SearchPanel(this, this);

        tabview = findView(R.id.activity_main_tabview1);

        songsTab = new SongsListTab(this);
        sqTab = new SongsQueueTab(this);
        artistTab = new ArtistTab(this);
        albumsTab = new AlbumsTab(this);
        playlistTab = new PlaylistsTab(this);

        tabview.addTab("QUEUE", sqTab);
        tabview.addTab("SONGS", songsTab);
        tabview.addTab("ARTIST", artistTab);
        tabview.addTab("ALBUMS", albumsTab);
        tabview.addTab("PLAYLIST",playlistTab);
        
        //tabview.showTab(sqTab);
        tabview.showTab(songsTab);
        //
    }

    
    @Override
    protected void onApplyTheme(ThemeManager.Theme theme){
        super.onApplyTheme(theme);
        tabview.onApplyTheme(theme);
        
        setTextViewsColor(theme.text,
            R.id.activity_main_tv_artist,
            R.id.activity_main_tv_title,
            R.id.title);
            
        setTintablesTint(theme.icon,
            R.id.au1,
            R.id.au2,
            R.id.au3,
            R.id.activity_main_iv_playpause
        );
        
        RoundedLinearLayout r1 = findView(R.id.rr1);
        r1.setBorderColor(theme.dividers);
    }

    public void onClick(View v){
        int id = v.getId();
        switch(id){
            case R.id.activity_main_btn_showsettings:
                new MainActOptions(this).show();
                break;
            case R.id.activity_main_btn_search:
                searchPanel.show(songslist);
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
        aupod.getSongsList();
        if(aupod.isPlaying()){
            onPlaybackStart();
        }
    }

    //int t=1;
    @Override
    public void onSongsListUpdated(List<SongItem> list){
        songsTab.update(list);
        albumsTab.update(list);
        //
        songslist = list;
        //toast("onSongsListUpdated:"+t);
        //t++;//
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
    public void onPlaybackStart(){
         handle.postDelayed(new Runnable(){
             public void run(){
                 btnPlayPause.setBackgroundResource(R.drawable.ic_pause);
                 btnPlayPause.reset();
             }
         },10);
    }

    @Override
    public void onPlaybackPause()
    {
        handle.postDelayed(new Runnable(){
            public void run(){
                btnPlayPause.setBackgroundResource(R.drawable.ic_play);
                btnPlayPause.reset();
            }
        }, 10);
    }

    @Override
    public void onPlaybackStop()
    {
        handle.postDelayed(new Runnable(){
            public void run(){
                btnPlayPause.setBackgroundResource(R.drawable.ic_play);
                btnPlayPause.reset();
            }
        }, 10);
    }

    @Override
    public void onSongChanged(final SongItem si){
        currentsongTitle.setText(si.title);
        currentsongArtist.setText(si.artist);
        if(si.icon!=null){
            currentImage.setBackgroundDrawable(new BitmapDrawable(si.icon));
        }else{
            currentImage.setImageBitmap(BitmapUtils.tint(ctx.getResources(), R.drawable.cover_f, ThemeManager.getTheme().icon));
        }
        //
        songsTab.setCurrent(si);
    }

    @Override
    public void onSearchResultItemClick(SongItem si)
    {
        if(aupod!=null){
            aupod.playSong(si);
        }
    }
    
    void unsubscribe(){
        APEvents.getInstance().removePlaybackEventListener(this);
        APEvents.getInstance().removeSongsListUpdateListener(this);
    }
    
    @Override
    protected void onPause(){
        //
        unsubscribe();
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        APEvents.getInstance().addPlaybackEventListener(this);
        APEvents.getInstance().addSongsListUpdateListener(this);
//        //SongLibrary.getInstance().update(this);
        AudioService.connect(this, this);
        //
    }

    @Override
    protected void onDestroy()
    {
        unsubscribe();
        if(aupod!=null){
            aupod.onActivityDestroyed();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
