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
import sleepchild.aupod22.ThemeManager.*;

public class PlayerActivity extends BaseActivity implements 
  AudioService.ConnectionListener,
  APEvents.PlaybackStateListener
{

    private TextView currentTitle, currentArtist;
    private TextView trackTimeCurrent, trackTimeDuration;
    private LinearLayout art2Background;
    private ImageView currentArt;
    private AudioService aupod;
    private TintedImageView playpauseIcon;
    private SeekBar seeker;
    private Runnable seekerTick;
    private boolean seektouch;
    private Handler handle;
    //private SongItem mCurrentSong;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setThemeable(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auplayer);
        init();
        
    }

    private void init(){
        handle = new Handler();
        currentTitle = findView(R.id.auplayer_tv_title);
        currentArtist = findView(R.id.auplayer_tv_artist);
        art2Background = findView(R.id.aupalyer_iv_art2_background);
        currentArt = findView(R.id.auplayer_iv_art);
        playpauseIcon = findView(R.id.auplayer_btn_playpause);
        //
        //*
        seeker = findView(R.id.aupalyer_seeker);
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

        trackTimeCurrent = findView(R.id.auplayer_currentpos);
        trackTimeDuration = findView(R.id.auplayer_duration);

        //
        setClickable(R.id.auplayer_btn_playpause);
        setClickable(R.id.auplayer_btn_prev);
        setClickable(R.id.auplayer_btn_next);
        setClickable(R.id.auplayer_btn_options);
        setClickable(R.id.auplayer_btn_back);
    }
    
    private void updateInfo(){
        final SongItem si = aupod.getCurrentSong();
        if(aupod==null || si==null){
            return;
        }
        
        currentTitle.setText(si.title);
        currentArtist.setText(si.artist);
        
        //hideView(R.id.auplayer_iv_art_b);
        if(si.icon!=null){
            currentArt.setImageBitmap(si.icon);
            setLargeArt(si.icon);
        }else{
            App.runInBackground(new Runnable(){
                public void run(){
                    final Bitmap bmp = BitmapUtils.tint(ctx.getResources(), R.drawable.cover_f, ThemeManager.getTheme().icon);
                    if(bmp!=null){
                        handle.post(new Runnable(){
                            public void run(){
                                currentArt.setImageBitmap(bmp);
                            }
                        });
                    }
                }
            });
            //showView(R.id.auplayer_iv_art_b);
            //setLargeArt( BitmapFactory.decodeResource(ctx.getResources(), R.drawable.fallback_cover));
        }
        
        if(aupod!=null){
            if(aupod.isPlaying()){
                playpauseIcon.setBackgroundResource(R.drawable.ic_pause);
            }else{
                playpauseIcon.setBackgroundResource(R.drawable.ic_play);
            }
        }
        
        playpauseIcon.reset();
        
        //playpauseIcon.setTint(tm.);
        
        int pos = aupod.getCurrentPosition();
        trackTimeDuration.setText(formatTime((int)si.duration));
        trackTimeCurrent.setText(formatTime(pos));
        
        seeker.setMax((int)si.duration);
        seeker.setProgress(pos);
        
        //
    }
    
    private void setLargeArt(final Bitmap bmp){
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
    
    private String formatTime(int time){
        //String t = "";
        int hrs = (int) (time/ (1000*60*60)) % 60;
        int min = (int) ( time/ (1000*60)) % 60;
        int sec = (int) (time /1000) % 60;
        if(hrs==0){
            return d(min)+":"+d(sec);
        }
        
        return d(hrs)+":"+d(min)+":"+d(sec);
    }

    private String d(int t){
        if(t<10){
            return "0"+t;
        }
        return ""+t;
    }

    @Override
    public void onClick(View v){
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
        playpauseIcon.setBackgroundResourceAndReset(R.drawable.ic_pause);
        startSeeker();
    }

    @Override
    public void onPlaybackPause(){
        playpauseIcon.setBackgroundResourceAndReset(R.drawable.ic_play);
        stopSeeker();
    }

    @Override
    public void onPlaybackStop(){
        updateInfo();
        playpauseIcon.setBackgroundResourceAndReset(R.drawable.ic_play);
        stopSeeker();
    }

    @Override
    public void onSongChanged(SongItem newsong){
        updateInfo();
    }

    ThemeManager.Theme theme;
    
    // todo: this takes too long
    @Override
    protected void onApplyTheme(ThemeManager.Theme theme)
    {
        super.onApplyTheme(theme);
        this.theme = theme;
        //
        themer.dispatchMessage(new Message());
    }
    
    Handler themer = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            //
            setTextViewsColor(theme.text,
                              trackTimeCurrent,
                              trackTimeDuration,
                              currentArtist,
                              currentTitle);
            //
            currentTitle.setBackgroundColor(theme.background);
            currentArtist.setBackgroundColor(theme.background);
            //
            setTintablesTint(theme.icon,
                             R.id.au1,
                             R.id.au2,
                             R.id.au3,
                             R.id.au4,
                             R.id.auplayer_btn_playpause);
            //
            seeker.getProgressDrawable().setColorFilter(theme.icon, PorterDuff.Mode.SRC_ATOP);
            seeker.getThumb().setColorFilter(theme.icon, PorterDuff.Mode.SRC_ATOP);
        }
     };
    
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
