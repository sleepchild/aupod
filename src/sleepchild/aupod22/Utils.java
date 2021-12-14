package sleepchild.aupod22;
import android.content.*;
import android.widget.*;
import java.io.*;

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
}
