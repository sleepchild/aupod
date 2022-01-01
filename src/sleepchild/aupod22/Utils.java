package sleepchild.aupod22;
import android.content.*;
import android.widget.Toast;
import java.io.FileOutputStream;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.renderscript.RenderScript;
import android.renderscript.*;

public class Utils
{
    public static void toast(Context ctx, String msg){
        Toast.makeText(ctx, msg, 500).show();
    }
    
    public static void log(String msg){
        try
        {
            FileOutputStream out = new FileOutputStream("/sdcard/aupodlog.txt");
            out.write(msg.getBytes());
            out.flush();
            out.close();
        }
        catch (FileNotFoundException e)
        {}catch(IOException e){}
    }
    
    public static Drawable bmpToDrawable(Context ctx, Bitmap bmp){
        return new BitmapDrawable(ctx.getResources(), bmp);
    }
    
    void blur(){
        //
    }
    
    public static Bitmap blur(Context ctx, Bitmap image, float BLUR_RADIUS ) {
        if (null == image) return null;
        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(ctx);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }
}
