package sleepchild.aupod22;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SPrefs{
    public Context ctx;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    
    /// keys ..
    private enum K{
        LAST_PLAYING_SONG_PATH,
        LAST_PLAYING_SONG_TIME_POSITION,
        APP_THEME,
        BG_COLOR,
        TEXT_COLOR_P,
        TEXT_COLOR_S
    }
    
    public SPrefs(Context ctx){
        this.ctx = ctx;
        pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        edit = pref.edit();
    }
    
    // remember the last song to play
    // todo: this should be a user option
    public String getLastPath(){
        return getString(K.LAST_PLAYING_SONG_PATH,"");
    }
    
    public void saveLastPath(String path){
        saveString(K.LAST_PLAYING_SONG_PATH,path);
    }
    
    // remember track pos of last song to play
    // todo: this should be a user option
    public void setPos(int pos){
        saveInt(K.LAST_PLAYING_SONG_TIME_POSITION,pos);
    }
    
    public int getPos(){
        return getInt(K.LAST_PLAYING_SONG_TIME_POSITION,0);
    }
    
    ////// app theme
    //background color
    public int getBackgroundColor(){
        return getInt(K.BG_COLOR, 0);
    }
    
    public void setBackgroundColor(int color){
        saveInt(K.BG_COLOR,color);
    }
    
    //primary text color
    
    //secondary text color
    
    
    
    
    //||||||| helper methods ||||||\\
    
    // enum K to String
    private String k2s(K key){
        return key.toString();
    }
    
    // string
    private String getString(K key, String deft){
        return pref.getString(k2s(key), deft);
    }
    
    private void saveString(K key, String value){
        edit.putString(k2s(key), value).commit();
    }
    
    // Integer
    private int getInt(K key, int deft){
        return pref.getInt(k2s(key), deft);
    }
    
    private void saveInt(K key, int value){
        edit.putInt(k2s(key), value).commit();
    }
    
    // boolean
    private boolean getBool(K key, boolean deft){
        return pref.getBoolean(k2s(key), deft);
    }
    
    private void saveBool(K key, boolean value){
        edit.putBoolean(k2s(key), value).commit();
    }
    
}
