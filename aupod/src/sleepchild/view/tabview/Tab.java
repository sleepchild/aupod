package sleepchild.view.tabview;

import android.view.View;

public abstract class Tab{

    public Tab(){}

    public abstract View getView();

    public void onTabShown(){}

    public void onTabHidden(){}

    public void onTabAlreadyVisible(){}

}
