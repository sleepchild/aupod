package sleepchild.view.tabview;

import android.view.View;
import sleepchild.aupod22.ThemeManager;

//todo: this should be an interface?
public abstract class Tab{
    
    public Tab(){}

    public abstract View getView();

    public void onTabShown(){}

    public void onTabHidden(){}

    public void onTabAlreadyVisible(){}
    
    public void onApplyTheme(ThemeManager.Theme theme){}

}
