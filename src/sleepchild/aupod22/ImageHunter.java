package sleepchild.aupod22;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageHunter implements Runnable
{
    Context ctx;
    Callback callback;
    Uri mUri;
    Handler mHandle = new Handler(Looper.getMainLooper());
    
    public ImageHunter(Context ctx, Uri uri, Callback callback){
        this.ctx = ctx;
        this.mUri = uri;
        this.callback = callback;
    }
    
    @Override
    public void run(){
        try
        {
            InputStream is = ctx.getContentResolver().openInputStream(mUri);
            final Bitmap bmp = BitmapFactory.decodeStream(is);
            is.close();
            //
            if(callback!=null){
                mHandle.post(new Runnable(){
                   public void run(){
                       callback.onValue(bmp);
                   } 
                });
            }
        }
        catch (FileNotFoundException e){}
        catch(IOException ie){}
    }
    
    public interface Callback{
        public void onValue(Bitmap icon);
    }
    
}
