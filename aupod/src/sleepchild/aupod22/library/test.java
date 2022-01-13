package sleepchild.aupod22.library;
import java.io.*;
import java.util.*;

public class test
{
    public test(){
        //
    }
    
    public List<MP3> findmp3(String path){
        return findmp3(new File(path));
    }
    
    public List<MP3> findmp3(File dir){
        List<MP3> list = new ArrayList<>();
        if(dir.isDirectory()){
            for(File f : dir.listFiles()){
                if(isAudio(f)){
                    list.add(new MP3(f));
                }
                if(f.isDirectory()){
                    list.addAll(findmp3(f));
                }
            }
        }
        return list;
    }
    
    String[] aext = new String[]{".mp3", ".wav", ".ogg"};
    boolean isAudio(File fl){
        if(fl.isDirectory()){
            return false;
        }
        String n = fl.getName().toLowerCase();
        for(String t : aext){
            if(n.endsWith(t)){
                return true;
            }
        }
        return false;
    }
    
    class MP3{
        File fl;
        public MP3(File fl){
            this.fl = fl;
        }
    }
}
