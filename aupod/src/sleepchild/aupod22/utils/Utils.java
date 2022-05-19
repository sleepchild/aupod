package sleepchild.aupod22.utils;

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
import java.util.*;
import java.io.*;
import sleepchild.aupod22.*;

public class Utils
{
    private static final String TAG ="Utils";
    public static void toast(Context ctx, String msg){
        Toast.makeText(ctx, msg, 500).show();
    }

    public static Drawable bmpToDrawable(Context ctx, Bitmap bmp){
        return new BitmapDrawable(ctx.getResources(), bmp);
    }
    
    public static boolean delete(String path){
        return new File(path).delete();
    }
    
    public static boolean exists(String path){
        return new File(path).exists();
    }

    void blur(){
        //
    }
    
    long gettime(){
        Math.abs(6);
        return new Date().getTime();
    }
    
    public static String formatTime(int time){
        //String t = "";
        int hrs = (int) (time/ (1000*60*60)) % 60;
        int min = (int) ( time/ (1000*60)) % 60;
        int sec = (int) (time /1000) % 60;
        if(hrs==0){
            return d(min)+":"+d(sec);
        }

        return d(hrs)+":"+d(min)+":"+d(sec);
    }

    public static String d(int t){
        if(t<10){
            return "0"+t;
        }
        return ""+t;
    }
    
    public static void touch(File path, String textContent){
        touch(path.getAbsolutePath(), textContent.getBytes());
    }
    
    public static void touch(File path, byte[] data){
        touch(path, data);
    }
    
    public static void touch(String path, String textContent){
        touch(path, textContent.getBytes());
    }
    
    public static void touch(String path, byte[] data){
        try{
            FileOutputStream out = new FileOutputStream(path);
            out.write(data);
            out.flush();
            out.close();
        }
        catch (FileNotFoundException e){
            XApp.logConsumableException(TAG, e);
        }catch(IOException ioe){
            XApp.logConsumableException(TAG, ioe);
        }
    }
    
    public static String readTextFile(File file){
        return readTextFile(file.getAbsolutePath());
    }
    
    public static String readTextFile(String path){
        String data="";
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(path));
            String line;
            while((line = br.readLine()) != null){
                data += line + "\n";
            }
        }
        catch (FileNotFoundException e){
            XApp.logConsumableException(TAG, e);
        }catch(IOException ioe){
            XApp.logConsumableException(TAG, ioe);
        }
        finally{
            if (br != null){
                try{br.close();}catch (IOException e){XApp.logConsumableException(TAG, e);}}
        }
        
        return data;
    }
}
