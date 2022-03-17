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

public class SearchPanel implements SearchHelper.ResultCallback
{
    MainActivity act;
    LinearLayout searchPanel;
    EditText input;
    ListView list8;
    SearchResutlAdapter adaptor;
    List<SongItem> songlist=new ArrayList<>();
    
    public SearchPanel(MainActivity act){
        this.act = act;
        searchPanel = (LinearLayout) act.findViewById(R.id.activity_main_searchpanel);
        input = (EditText) act.vid(R.id.activity_main_search_input);
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
                    SearchHelper.serch(q, songlist, SearchPanel.this);
                }else{
                    adaptor.clear();
                }
            }
        });
        list8 = (ListView) act.findViewById(R.id.activity_main_search_list8);
        adaptor = new SearchResutlAdapter(act);
        list8.setAdapter(adaptor);
    }
    
    public void setSongslist(List<SongItem> list){
        songlist = list;
    }

    @Override
    public void onSearchResult(List<SearchResult> list){
        adaptor.update(list);
    }
    
    public void show(){
        searchPanel.setVisibility(View.VISIBLE);
        this.act.showKeyboard(input);
    }
    
    public void hide(){
        searchPanel.setVisibility(View.GONE);
        adaptor.clear();
        input.setText("");
    }
    
    public boolean isVisible(){
        return searchPanel.getVisibility()==View.VISIBLE;
    }
    
    //
    private class SearchResutlAdapter extends BaseAdapter implements View.OnClickListener{

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
        public Object getItem(int pos)
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
            SongItem si = (SongItem) res.obj;
            
            v= inf.inflate(R.layout.songlist_item, null, false);
            TextView t = (TextView) v.findViewById(R.id.songlist_itemTitle);
            t.setText(res.title);
            TextView a = (TextView) v.findViewById(R.id.songlist_itemArtist);
            a.setText(si.artist+" | "+si.album);
            
            switch(res.type){
                case 0: //title
                    //
                    //SongItem si = (SongItem) res.obj;
                    ImageView ic = (ImageView) v.findViewById(R.id.songlist_item_albumart);
                    if(si.icon!=null){
                        ic.setBackground(new BitmapDrawable(si.icon));
                    }
                    break;
                case 1:// artist
                    v.setBackgroundColor(Color.parseColor("#a000ff99"));
                    break;
                case 2: // album
                    v.setBackgroundColor(Color.parseColor("#ab4500"));
                    //a.setText(si.artist +" - "+si.album);
                    
                    break;
            }
            
            
            v.setTag(res);
            v.setOnClickListener(SearchResutlAdapter.this);
            return v;
        }

        @Override
        public void onClick(View v){
            SearchResult sr = (SearchResult) v.getTag();
            switch(sr.type){
                case 0:
                    //
                    break;
                case 1:
                    //
                    break;
                case 2:
                    //
                    break;
            }
           // SongItem si = (SongItem) sr.obj;
            //aupod.playSong(si);
        }
        
        
    }
}
