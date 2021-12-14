package sleepchild.aupod22;
import android.os.*;
import android.widget.*;
import android.graphics.*;
import java.util.*;
import mrtubbs.postman.*;
import sleepchild.aupod22.postmanmodels.*;
import android.graphics.drawable.*;
import android.view.*;
import java.util.concurrent.*;

public class PlayerActivity extends BaseActivity
{

    TextView currentTitle, currentArtist;
    LinearLayout art2Background;
    ImageView currentArt;
    AudioService aupod;
    SongItem si;
    ImageView playpauseIcon;
    SeekBar seeker;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auplayer);
        init();
    }
    
    void init(){
        currentTitle = (TextView) findViewById(R.id.auplayer_tv_title);
        currentArtist = (TextView) findViewById(R.id.auplayer_tv_artist);
        art2Background = (LinearLayout) findViewById(R.id.aupalyer_iv_art2_background);
        currentArt = (ImageView) findViewById(R.id.auplayer_iv_art);
        playpauseIcon = (ImageView) findViewById(R.id.auplayer_btn_playpause);
        seeker = (SeekBar) findViewById(R.id.aupalyer_seeker);
    }
    
    void updateInfo(){
        SongItem si = aupod.getCurrentSong();
        currentTitle.setText(si.title);
        currentArtist.setText(si.artist);
        if(si.icon!=null){
            currentArt.setImageBitmap(si.icon);
            art2Background.setBackground(new BitmapDrawable(getResources(), si.icon));
        }else{
            //
        }
        seeker.setMax(aupod.getDuration());
    }
    
    void updateButtons(){
        if(aupod!=null){
            if(aupod.isPlaying()){
                playpauseIcon.setImageResource(R.drawable.ic_pause);
            }else{
                playpauseIcon.setImageResource(R.drawable.ic_play);
            }
        }
    }
    
    Thread seekerThread;
    void startSeeker(){
        if(seekerThread!=null && seekerThread.isAlive()){
            seekerThread.interrupt();
        }
        seekerThread = null;
        
        seekerThread = new Thread(new Runnable(){
            public void run(){
                if(aupod!=null){
                    while(aupod.isPlaying()){
                        runOnUiThread(new Runnable(){
                            public void run(){
                                seeker.setProgress(aupod.getCurrentPosition());
                                try
                                {
                                    Thread.sleep(1000);
                                }
                                catch (InterruptedException e)
                                {}
                            }
                        });
                    }
                }
            }
        });
        
        seekerThread.start();
    }
    void stopSeeker(){
        if(seekerThread!=null&&seekerThread.isAlive()){
            seekerThread.interrupt();
        }
        seekerThread=null;
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
                    aupod.playnext();
                }
                break;
            case R.id.auplayer_btn_prev:
                if(aupod!=null){
                    aupod.playPrevious();
                }
                break;
        }
    }
    
    @PostEvent
    public void onConnect(AudioServiceConnect conn){
        aupod = conn.service;
        updateInfo();
        updateButtons();
    }
    
    @PostEvent
    public void onSongResumed(SongResumeEvent evt){
        runOnUiThread(new Runnable(){
            public void run(){
                updateInfo();
                updateButtons();
                //startSeeker();
            }
        });
    }
    
    @PostEvent
    public void onSongPaused(SongPauseEvent evt){
        runOnUiThread(new Runnable(){
            public void run(){
                updateInfo();
                updateButtons();
            }
        });
    }
    
    @Override
    protected void onPause()
    {
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
