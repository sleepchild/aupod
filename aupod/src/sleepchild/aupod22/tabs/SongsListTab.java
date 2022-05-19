package sleepchild.aupod22.tabs;

import android.view.*;
import android.content.*;
import android.widget.*;
import java.util.*;
import sleepchild.aupod22.models.*;
import sleepchild.aupod22.*;
import sleepchild.view.tabview.Tab;
import sleepchild.aupod22.adapters.*;
import sleepchild.aupod22.activity.*;
import sleepchild.view.*;
import sleepchild.aupod22.service.*;
import sleepchild.view.fastscroll.*;
import sleepchild.aupod22.ThemeManager.*;
import android.graphics.drawable.*;

public class SongsListTab extends Tab
{
    private View root;
    private SongListAdaptor adapter;
    private SongItem currentSong;
    private FastScrollListView list1;
    //ThemeManager.Theme theme;
    //MainActivity act;
    
    //*
    public SongsListTab(MainActivity ctx){
        //act = ctx;
        root = LayoutInflater.from(ctx).inflate(R.layout.default_listview, null, false);
        list1 = (FastScrollListView) root.findViewById(R.id.list_view_list1);
        adapter = new SongListAdaptor(ctx);
        list1.setAdapter(adapter);
        list1.setOgtl(new FastScrollListView.GT(){
            @Override
            public String getItemText(int pos){
                if(pos<0){
                    pos = 0;
                }else if(pos>=adapter.getCount()){
                    pos = adapter.getCount()-1;
                }
                if(adapter!=null){
                    if(!adapter.isEmpty()){
                        SongItem i = adapter.getItem(pos);
                        if(i!=null){
                            String t = i.title;
                            if(t!=null){
                                if(t.length()>1){
                                    t = t.substring(0,1);
                                    return t.toUpperCase();
                                }
                            }
                        }
                    }
                }
                return "...";
            }
        });
        
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> p1, View v, int pos, long p4){
                SongItem si = (SongItem) v.getTag();
                AudioService aupod = App.get().getAudioService();
                if(aupod!=null){
                    aupod.playSong(si);
                }
            }
        });
    }
    
    String ss(String t){
        if(t.toLowerCase().matches("[a-z]")){
            return t;
        }
        
        return "...";  
    }

    public void update(List<SongItem> list){
        adapter.update(list);
    }
    
    public void setCurrent(SongItem currentSong){
        adapter.setCurrent(currentSong);
        this.currentSong = currentSong;
    }
    
    public void showCurrent(){
        if(currentSong!=null){
            list1.setSelection(adapter.getPos(currentSong));
        }
    }

    @Override
    public void onTabShown(){
        super.onTabShown();
    }

    @Override
    public void onTabAlreadyVisible(){
        super.onTabAlreadyVisible();
        showCurrent();
    }

    @Override
    public void onTabHidden(){
        super.onTabHidden();
    }

    @Override
    public View getView(){
        return root;
    }

    @Override
    public void onApplyTheme(ThemeManager.Theme theme){
        if(adapter!=null){
            adapter.setTheme(theme);
        }
        list1.setThumbColor(theme.icon);
        list1.setThumbTextColor(theme.text);
        list1.setDivider(new ColorDrawable(theme.dividers));
    }
   

}
