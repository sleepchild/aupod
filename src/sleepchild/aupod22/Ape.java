package sleepchild.aupod22;
import java.io.*;
import android.graphics.*;
//import java.sql.*;
import java.util.*;

public class Ape
{
    public Ape(){
        File fl = new File("/sdcard/AppProjects/apcBin/aupod/res/drawable/ic_play.png");
        try
        {
            FileInputStream ins = new FileInputStream(fl);
            Bitmap bmp = BitmapFactory.decodeStream(ins);
            
            int w = bmp.getWidth();
            int h = bmp.getHeight();
            
            Bitmap o = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
            //int x =0; //int y=0;
            //String colors = "";
            for(int y=0;y<h;y++){
                for(int x=0;x<w;x++){
                    int p = bmp.getPixel(x,y);
                    if(p==0){
                        o.setPixel(x,y,p);
                    }else{
                        o.setPixel(x,y,Color.parseColor("#000000"));
                    }
                }
            }
            int t = (int) new Date().getTime();
            FileOutputStream out = new FileOutputStream("/sdcard/Alarms/moss/"+t+".png");
            o.compress(Bitmap.CompressFormat.PNG,100,out);
        }
        catch (FileNotFoundException e)
        {}catch(IOException ioe){}
    }
}
