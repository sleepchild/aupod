package sleepchild.aupod22.utils;
import java.util.*;
import android.graphics.*;
import android.content.res.*;
import sleepchild.aupod22.*;

public class BitmapUtils
{
    private static BitmapUtils deft;
    
    private static class C{ //cache
        Bitmap bmp;
        int color;
        int id;
    }
    
    private Map<Integer, C> cache;
    
    private BitmapUtils(){
        cache = new HashMap<>();
    }
    
    private static BitmapUtils get(){
        BitmapUtils inst = deft;
        if(inst==null){
            synchronized(BitmapUtils.class){
                inst = BitmapUtils.deft;
                if(inst==null){
                    inst = BitmapUtils.deft = new BitmapUtils();
                }
            }
        }
        return inst;
     }
    
    public static Bitmap tint(Resources res, int resid, int color){
        return get().itint(res, resid, color);
    }
    private Bitmap itint(Resources res, final int id, final int color){
        C t = cache.get(id);
        if(t==null){
            t = new C();
        }else {
            //if(t.bmp!=null){
                //App.get().toast("using cache -- "+id+" -- "+cache.size());
                return t.bmp;
           // }
        }
        //App.get().toast("skip cache --"+id+" -- "+cache.size());

        Bitmap src =BitmapFactory.decodeResource(res, id);
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(dest);
        Paint mpaint = new Paint();
        mpaint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        c.drawBitmap(src,0,0,mpaint);
        //dest.recycle();
        //src.recycle();
        t.bmp = dest;
        t.color = color;
        t.id = id;
        cache.put(id, t);

        return dest;
    }
}
