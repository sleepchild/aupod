package sleepchild.aupod22;

import android.widget.*;
import android.view.*;
import java.util.*;
import android.text.*;
import sleepchild.aupod22.activity.*;
import sleepchild.aupod22.utils.*;
import sleepchild.aupod22.models.*;
import android.graphics.*;
import android.graphics.drawable.*;
import sleepchild.aupod22.service.*;
import android.app.*;
import android.widget.ActionMenuView.*;

public class SearchPanel implements SearchHelper.ResultCallback
{
    MainActivity act;
    EditText input;
    ListView list8;
    SearchResutlAdapter adaptor;
    List<SongItem> songlist=new ArrayList<>();
    Dialog dlg;
    OnSearchResultItemClickListener ocl;
    
    public SearchPanel(MainActivity a, OnSearchResultItemClickListener l){
        this.act = a;
        this.ocl = l;
        dlg = new Dialog(a);
        dlg.setContentView(R.layout.dlg_searchpanel);
        
        input = (EditText) dlg.findViewById(R.id.searchpanel_input);
        input.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
            {
                // TODO: Implement this method
            }

            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
            {
                // TODO: Implement this method
            }

            @Override
            public void afterTextChanged(Editable p1)
            {
                String q = input.getText().toString();
                if(!q.isEmpty()){
                    SearchHelper.get().search(q, songlist, SearchPanel.this);
                }else{
                    adaptor.clear();
                }
            }
        });
        list8 = (ListView) dlg.findViewById(R.id.searchpanel_list8);
        list8.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView av, View v, int pos, long p4){
                SearchResult res = adaptor.getItem(pos);
                if(ocl!=null){
                    ocl.onSearchResultItemClick(res.si);
                }
                
            }
        });
        adaptor = new SearchResutlAdapter(a);
        list8.setAdapter(adaptor);
        
    }
    
    public void setSongslist(List<SongItem> list){
        songlist = list;
    }

    @Override
    public void onSearchResult(List<SearchResult> list){
        adaptor.update(list);
    }
    
    public void show(List<SongItem> list){
        input.setText("");
        adaptor.clear();
        setSongslist(list);
        dlg.show();
        dlg.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        dlg.getWindow().setBackgroundDrawable(null);
        input.postDelayed(new Runnable(){
            public void run(){
                act.showKeyboard(input); 
            }
        },10);
        
    }
    
    public void hide(){
        input.setText("");
        adaptor.clear();
        dlg.dismiss();
    }
    
    //
    private class SearchResutlAdapter extends BaseAdapter{

        private LayoutInflater inf;
        private List<SearchResult> resultlist = new ArrayList<>();
        
        public SearchResutlAdapter(MainActivity ctx){
            this.inf = ctx.getLayoutInflater();
        }
        
        public void update(List<SearchResult> list){
            resultlist = list;
            notifyDataSetChanged();
        }
        
        public void clear(){
            resultlist.clear();
            notifyDataSetChanged();
        }
        
        @Override
        public int getCount(){
            return this.resultlist.size();
        }

        @Override
        public SearchResult getItem(int pos)
        {
            return this.resultlist.get(pos);
        }

        @Override
        public long getItemId(int p1)
        {
            // TODO: Implement this method
            return p1;
        }

        @Override
        public View getView(int pos, View v, ViewGroup p3)
        {
            SearchResult res = resultlist.get(pos);
            SongItem si = res.si;
            
            v= inf.inflate(R.layout.songlist_item, null, false);
            TextView t = (TextView) v.findViewById(R.id.songlist_itemTitle);
            t.setText(res.title);
            TextView a = (TextView) v.findViewById(R.id.songlist_itemArtist);
            a.setText(si.artist);//+" | "+si.album);
            
            return v;
        }        
        
    }
    
    public static interface OnSearchResultItemClickListener{
        public void onSearchResultItemClick(SongItem si);
    }
}
