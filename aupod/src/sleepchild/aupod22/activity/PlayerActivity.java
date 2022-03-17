package sleepchild.aupod22.activity;

import android.os.*;
import android.widget.*;
import android.graphics.*;
import java.util.*;
import sleepchild.aupod22.models.*;
import android.graphics.drawable.*;
import android.view.*;
import java.util.concurrent.*;
import sleepchild.aupod22.menu.*;
import sleepchild.view.*;
import android.view.animation.*;
import sleepchild.aupod22.utils.*;
import sleepchild.aupod22.activity.*;
import sleepchild.aupod22.*;
import sleepchild.aupod22.library.*;
import sleepchild.aupod22.service.*;

public class PlayerActivity extends BaseActivity implements AudioService.ConnectionListener,
APEvents.PlaybackStateListener
{

    TextView currentTitle, currentArtist;
    TextView trackTimeCurrent, trackTimeDuration;
    LinearLayout art2Background;
    ImageView currentArt;
    AudioService aupod;
    TintedImageView playpauseIcon;
    SeekBar seeker;
    Runnable seekerTick;
    boolean seektouch;
    Handler handle;
    SongItem mCurrentSong;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auplayer);
        init();
    }

    void init(){
        handle = new Handler();
        currentTitle = (TextView) findViewById(R.id.auplayer_tv_title);
        currentArtist = (TextView) findViewById(R.id.auplayer_tv_artist);
        art2Background = (LinearLayout) findViewById(R.id.aupalyer_iv_art2_background);
        currentArt = (ImageView) findViewById(R.id.auplayer_iv_art);
        playpauseIcon = (TintedImageView) findViewById(R.id.auplayer_btn_playpause);
        //
        //*
        seeker = (SeekBar) findViewById(R.id.aupalyer_seeker);
        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                @Override
                public void onProgressChanged(SeekBar p1, int pos, boolean touch)
                {
                    if(aupod!=null && seektouch){
                        aupod.seekTo(pos);
                        trackTimeCurrent.setText(formatTime(pos));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar p1)
                {
                    stopSeeker();
                    seektouch = true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar p1)
                {
                    seektouch = false;
                    if(aupod!=null && aupod.isPlaying()){
                        startSeeker();
                    }
                }
            });

        seekerTick = new Runnable(){
            @Override
            public void run(){
                if(aupod!=null){
                    int pos = aupod.getCurrentPosition();
                    seeker.setProgress(pos);
                    trackTimeCurrent.setText(formatTime(pos));
                    handle.postDelayed(seekerTick,1000);
                }
            }
        };
        //*/

        trackTimeCurrent = (TextView) findViewById(R.id.auplayer_currentpos);
        trackTimeDuration = (TextView) findViewById(R.id.auplayer_duration);

    }
    
    private void updateInfo(){
        if(aupod==null){
            return;
        }
        final SongItem si = aupod.getCurrentSong();
        if(si==null){
            return;
        }
        
        currentTitle.setText(si.title);
        currentArtist.setText(si.artist);
        if(si.icon!=null){
            currentArt.setImageBitmap(si.icon);
            setLargeArt(si.icon);
        }else{
            SongInfoUpdater.updateSI(si, new SongInfoUpdater.ResultCallback(){
                public void onresult(){
                    if(si.icon!=null){
                        currentArt.setImageBitmap(si.icon);
                        setLargeArt(si.icon);
                    }else{
                        currentArt.setImageResource(R.drawable.fallback_cover);
                        art2Background.setBackgroundDrawable(null);
                    }
                }
            });
        }
        
        if(aupod!=null){
            if(aupod.isPlaying()){
                playpauseIcon.setBackgroundResource(R.drawable.ic_pause);
            }else{
                playpauseIcon.setBackgroundResource(R.drawable.ic_play);
            }
        }
        
        int pos = aupod.getCurrentPosition();
        trackTimeDuration.setText(formatTime((int)si.duration));
        trackTimeCurrent.setText(formatTime(pos));
        
        seeker.setMax((int)si.duration);
        seeker.setProgress(pos);
        
        //
    }
    
    void setLargeArt(final Bitmap bmp){
        //art2Background.setBackgroundDrawable(new BitmapDrawable(bmp));
        App.runInBackground(new Runnable(){
            public void run(){
                final Bitmap img = FastBlur.fastblur(bmp,0.2f,3);
                if(img!=null){
                    App.runInUiThread(new Runnable(){
                        public void run(){
                            art2Background.setBackgroundDrawable(new BitmapDrawable(img));
                        }
                    });
                }
            }
        });
    }
    
    
    private void startSeeker(){
        stopSeeker();
        handle.postDelayed(seekerTick,0);
    }
    
    private void stopSeeker(){
        handle.removeCallbacks(seekerTick);
        handle.removeCallbacks(seekerTick);
    }
    
    String formatTime(int time){
        //String t = "";
        int hrs = (int) (time/ (1000*60*60)) % 60;
        int min = (int) ( time/ (1000*60)) % 60;
        int sec = (int) (time /1000) % 60;
        if(hrs==0){
            return d(min)+":"+d(sec);
        }
        
        return d(hrs)+":"+d(min)+":"+d(sec);
    }

    String d(int t){
        if(t<10){
            return "0"+t;
        }
        return ""+t;
    }

    public void onButton(View v){
        int id = v.getId();
        switch(id){
            case R.id.auplayer_btn_playpause:
                if(aupod!=null){
                    aupod.playPause();
                }
                break;
            case R.id.auplayer_btn_next:
                if(aupod!=null){
                    aupod.playNext();
                }
                break;
            case R.id.auplayer_btn_prev:
                if(aupod!=null){
                    aupod.playPrev();
                }
                break;
            case R.id.auplayer_btn_back:
                finish();
                break;
            case R.id.auplayer_btn_options:
                new CurrentSongOptions(this, aupod.getCurrentSong()).show();
                break;
        }
    }

    @Override
    public void onAudioServiceConnect(AudioService service){
        aupod = service;
        updateInfo();
        aupod.requestPlaystateUpdate();
    }

    @Override
    public void onPlaybackStart(){
        playpauseIcon.setBackgroundResource(R.drawable.ic_pause);
        startSeeker();
    }

    @Override
    public void onPlaybackPause(){
        playpauseIcon.setBackgroundResource(R.drawable.ic_play);
        stopSeeker();
    }

    @Override
    public void onPlaybackStop(){
        updateInfo();
        playpauseIcon.setBackgroundResource(R.drawable.ic_play);
        stopSeeker();
    }

    @Override
    public void onSongChanged(SongItem newsong){
        updateInfo();
    }
    
    @Override
    protected void onPause(){
        stopSeeker();
        APEvents.getInstance().removePlaybackEventListener(this);
        super.onPause();
    }


    @Override
    protected void onResume(){
        super.onResume();
        APEvents.getInstance().addPlaybackEventListener(this);
        AudioService.connect(this, this);
        //
    }

}
