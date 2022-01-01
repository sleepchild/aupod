package sleepchild.aupod22;
import android.widget.*;
import android.view.*;
import java.util.*;
import android.text.*;

public class SearchPanel
{
    MainActivity act;
    LinearLayout searchPanel;
    EditText input;
    ListView list8;
    SearchResutlAdapter adaptor;
    
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
                // TODO: Implement this method
            }
        });
        list8 = (ListView) act.findViewById(R.id.activity_main_search_list8);
        adaptor = new SearchResutlAdapter(act);
        list8.setAdapter(adaptor);
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
    
    private class SearchResutlAdapter extends BaseAdapter{

        private LayoutInflater inf;
        private List<String> resultlist = new ArrayList<>();
        
        public SearchResutlAdapter(MainActivity ctx){
            this.inf = ctx.getLayoutInflater();
        }
        
        public void update(){
            //
        }
        
        public void clear(){
            //
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
            //v= inf.inflate(R.layout.songlist_item);
            return v;
        }
        
        
    }
}
