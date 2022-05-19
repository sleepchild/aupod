package sleepchild.aupod22.adapters;
import android.widget.*;
import android.view.*;
import java.util.*;

public class BasicAdaptor<T> extends BaseAdapter
{
    private List<T> mlist = new ArrayList<T>();
    
    public BasicAdaptor(){}
    
    public void update(List<T> list){}

    @Override
    public int getCount()
    {
        return mlist.size();
    }

    @Override
    public Object getItem(int p1)
    {
        // TODO: Implement this method
        return null;
    }

    @Override
    public long getItemId(int p1)
    {
        // TODO: Implement this method
        return 0;
    }

    @Override
    public View getView(int p1, View p2, ViewGroup p3)
    {
        // TODO: Implement this method
        return null;
    }
    
    
}
