package sleepchild.aupod22.activity;

import android.os.*;
import android.widget.*;
import android.graphics.*;
import java.util.*;
import sleepchild.postman.PostMan;
import sleepchild.postman.PostEvent;
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
import sleepchild.aupod22.models.events.*;
import sleepchild.aupod22.library.*;

public class PlayerActivity extends BaseActivity
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
        seeker = (SeekBar) findViewById(R.id.aupalyer_seeker);
        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                @Override
                public void onProgressChanged(SeekBar p1, int pos, boolean touch)
                {
                    if(seektouch){
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
                    if(aupod.isPlaying()){
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

        trackTimeCurrent = (TextView) findViewById(R.id.auplayer_currentpos);
        trackTimeDuration = (TextView) findViewById(R.id.auplayer_duration);

    }

    void updateInfo(){
        //*
        final SongItem si = aupod.getCurrentSong();
        if(mCurrentSong==si){
            return;
        }
        mCurrentSong=si;
        currentTitle.setText(si.title);
        currentArtist.setText(si.artist);
        if(si.icon!=null){
            currentArt.setImageBitmap(si.icon);
            art2Background.setBackground(null);
            SongFactory.get().submit(new Runnable(){
                    public void run(){
                        final Bitmap bmp = FastBlur.fastblur(si.icon,1,8);
                        if(bmp!=null){
                            handle.postDelayed(new Runnable(){
                                    public void run(){
                                        //art2Background.setVisibility(View.INVISIBLE);
                                        art2Background.setBackground(new BitmapDrawable(getResources(), bmp));
                                        //*
                                        Animation anim = new AlphaAnimation(0,1);
                                        anim.setDuration(500);
                                        anim.setRepeatCount(0);
                                        anim.setInterpolator(new DecelerateInterpolator());
                                        anim.setAnimationListener(new Animation.AnimationListener(){
                                                @Override
                                                public void onAnimationStart(Animation p1)
                                                {
                                                    // TODO: Implement this method
                                                }

                                                @Override
                                                public void onAnimationEnd(Animation p1)
                                                {
                                                    art2Background.setVisibility(View.VISIBLE);
                                                }

                                                @Override
                                                public void onAnimationRepeat(Animation p1)
                                                {
                                                    // TODO: Implement this method
                                                }
                                            });
                                        art2Background.setAnimation(anim);
                                        //*/
                                        //anim.start();
                                    }

                                },0);
                        }
                    }
                });
        }else{
            currentArt.setImageResource(R.drawable.fallback_cover);
            art2Background.setBackground(null);
        }

        if(aupod.isPlaying()){
            seeker.setMax(aupod.getDuration());
        }else{
            seeker.setMax((int)si.duration);
        }

        seeker.setProgress(aupod.getCurrentPosition());
        trackTimeDuration.setText(formatTime((int)si.duration));
        //*/

    }

    void updateButtons(){
        //*
        if(aupod!=null){
            if(aupod.isPlaying()){
                playpauseIcon.setImageResource(R.drawable.ic_pause);
            }else{
                playpauseIcon.setImageResource(R.drawable.ic_play);
            }
        }
        //*/
    }

    void startSeeker(){
        stopSeeker();
        handle.postDelayed(seekerTick, 1000);
    }

    void stopSeeker(){
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

    @PostEvent
    public void onConnect(AudioServiceConnect conn){
        aupod = conn.service;
        updateInfo();
        //*
        updateButtons();
        if(aupod.isPlaying()){
            startSeeker();
        }
        //*/
    }

    @PostEvent
    public void onSongResumed(SongResumeEvent evt){
        runOnUiThread(new Runnable(){
                public void run(){
                    updateInfo();
                    updateButtons();
                    startSeeker();
                }
            });
    }

    @PostEvent
    public void onSongPaused(SongPauseEvent evt){
        runOnUiThread(new Runnable(){
                public void run(){
                    updateInfo();
                    updateButtons();
                    stopSeeker();
                }
            });
    }

    //@PostEvent
    public void onSongStopped(){

    }

    @Override
    protected void onPause()
    {
        stopSeeker();
        //APEvents.getInstance().unregister(this);
        PostMan.getInstance().unregister(this);
        super.onPause();
    }


    @Override
    protected void onResume()
    {
        //APEvents.getInstance().register(this);
        PostMan.getInstance().register(this);
        AudioService.start(this);
        super.onResume();
    }

}
