package sleepchild.aupod22;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.content.ContentResolver;
import java.io.InputStream;
import java.io.FileNotFoundException;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.*;
import android.graphics.drawable.*;

public class Imgur{
    private Context ctx;
    //private Uri mUri;
    private ExecutorService worker;
    
    //private ImageView ImageViewTarget;
    //private int fallbackResId;
    private Handler mHandle;
    
    private static volatile Imgur deftInstance;
    
    private Imgur(Context ctx){
       this.ctx = ctx;// XApp.getContext();
       worker = Executors.newFixedThreadPool(2);
       mHandle = new Handler(Looper.getMainLooper());
    }
    
    public static Imgur with(Context ctx){
        Imgur instance = deftInstance;
        if(instance==null){
            synchronized(SongFactory.class){
                instance = Imgur.deftInstance;
                if(instance==null){
                    instance = Imgur.deftInstance = new Imgur(ctx);
                }
            }
        }
        return instance;
    }
    
    public Imgur.TaskBuilder load(Uri uri){
        return new TaskBuilder(this, uri);
    }
    
    public Handler getHandler(){
        return mHandle;
    }
    
    public Context getContext(){
        return ctx;
    }
    
    public void start(TaskBuilder task){
        worker.submit(task);
    }
    
    public static class TaskBuilder implements Runnable{
        private Context mCtx;
        private Uri mUri;
        private Handler mHandle;
        private Object mTarget;
        private Bitmap image;
        private Drawable imageDrawable;
        //private Drawable fallbackDrawable;
        private Bitmap fallbackBitmap;
        private Imgur parent;
        private ImgurResult cb;
        
        public TaskBuilder(Imgur imgurParent, Uri uri){
            this.parent = imgurParent;
            this.mCtx = parent.getContext();
            this.mUri = uri;
            this.mHandle = parent.getHandler();
        }
        
        public void into(Object target){
            this.mTarget = target;
            start();
            //return this;
        }
        
        public TaskBuilder callback(ImgurResult cb){
            this.cb = cb;
            return this;
        }
        
        public TaskBuilder elseLoad(int fallback){
           // fallbackBitmap = mCtx.getResources().getDrawable(fallback);
            return this;
        }
        
        public void start(){
            parent.start(this);
        }

        @Override
        public void run()
        {
            try
            {
                InputStream is = mCtx.getContentResolver().openInputStream(mUri);
                image = BitmapFactory.decodeStream(is);
                if(image!=null){
                    mHandle.postDelayed(new Runnable(){
                        public void run(){
                            loadImageIntoTarget();
                        }
                    },0);
                }
                //loadImageInTarget();
            }
            catch (FileNotFoundException e){
                // on error
            }
        }
        
        private void loadImageIntoTarget(){
            if(mTarget!=null){
                if(mTarget instanceof ImageView){
                    ((ImageView)mTarget).setImageBitmap(image);
                }
            }
            if(cb!=null){
                cb.onImage(image);
            }
        }
        
    }
    
    public interface ImgurResult{
        public void onImage(Bitmap image);
    }
    
    
}
