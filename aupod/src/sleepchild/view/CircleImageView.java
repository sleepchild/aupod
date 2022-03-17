package sleepchild.view;
import android.view.*;
import android.content.*;
import android.util.*;

public class CircleImageView extends View
{
    public CircleImageView(Context ctx){
        this(ctx,null);
    }
    public CircleImageView(Context ctx, AttributeSet attrs){
        this(ctx, attrs,0);
    }
    
    public CircleImageView(Context ctx, AttributeSet attrs, int def){
        super(ctx, attrs, def);
        
    }
}
