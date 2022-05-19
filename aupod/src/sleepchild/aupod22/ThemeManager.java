package sleepchild.aupod22;
import java.io.*;
import sleepchild.aupod22.utils.*;
import org.json.*;
import android.content.*;
import android.graphics.*;

public class ThemeManager
{
    private static ThemeManager deft;
    private static Theme ctheme;
    
    private File themeFile;
    private String dir;
    
    final private static String TAG = "ThemeManager";
    
    private String key_bg = "background", 
      key_text = "text",
      key_icon = "icons",
      key_div = "dividers";
    
    private ThemeManager(){
        dir = App.getAppDirectory()+"theme/";
        themeFile = new File(dir+"/default");
        themeFile.getParentFile().mkdirs();
        ctheme = new Theme();
    }
    
    private static ThemeManager get(){
        if(deft==null){
            deft = ThemeManager.deft = new ThemeManager();
        }
        return deft;
    }
    
    public static Theme getTheme(){
        return get().igetTheme();
    }
    
    private Theme igetTheme(){
        if(ctheme==null){
            reloadTheme();
            //App.get().toast("ctheme=null");
        }else{
           File fl = new File(themeFile.getAbsolutePath());
           if(fl.lastModified() != ctheme.lastmod){
               //App.get().toast(fl.lastModified()+" = "+ctheme.lastmod);
               reloadTheme();
           }
        }
        return ctheme;
    }
    
    private Theme reloadTheme(){
        
        if(themeFile.exists()){
           ctheme = fromFile(themeFile.getAbsolutePath());
        }else{
           ctheme = newdefault(themeFile.getAbsolutePath());
        }
        return ctheme;
    }
    
    // note: to keep things in sync, this shud be the only method that performs a read on the themefile
    private Theme fromFile(String path){
        String json = Utils.readTextFile(path);
        JSONObject o = null;
        try{
            o = new JSONObject(json);
        }catch (JSONException e){
            XApp.logConsumableException(TAG, e);
        }
        ctheme.background = ttc(o, key_bg, "#ffffff");
        ctheme.text = ttc( o, key_text, "#082B56");
        ctheme.icon = ttc( o, key_icon, "#082B56");
        ctheme.dividers = ttc( o, key_div, "#D7D7D7");
        
        ctheme.lastmod = new File(path).lastModified();//themeFile.getAbsolutePath()).lastModified();
        //App.get().toast("reep: "+ctheme.lastmod);
        return ctheme;
    }
    
    private Theme newdefault(String path){
        new File(path).getParentFile().mkdirs();
        //
        JSONObject o = new JSONObject();
        try{
            o.put(key_bg, "#ffffff");
            o.put(key_text, "#082B56");
            o.put(key_icon, "#082b56");
            o.put(key_div, "#D7D7D7");
        }
        catch (JSONException e){
            XApp.logConsumableException(TAG, e);
        }
        Utils.touch(path, o.toString());
        return fromFile(path);
    }
    
    private int ttc(JSONObject o, String key, String fallback){
        int col = Color.parseColor(fallback);
        try{
            col = Color.parseColor(o.getString(key));
        }catch (JSONException e){
            col = Color.parseColor(fallback);
            XApp.logConsumableException(TAG, e);
        }catch(Exception x){
            col = Color.parseColor(fallback);
            XApp.logConsumableException(TAG, x);
        }
        return col;
    }
    
    public void setTheme(Theme theme){
        //
    }
    
    public static class Theme{
        public int background=0;
        public int text=0;
        public int icon=0;
        public int dividers=0;
        public long lastmod=0;
    }
}
