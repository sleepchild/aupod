package sleepchild.aupod22.recievers;

import android.content.*;
import android.view.*;
import sleepchild.aupod22.*;
import sleepchild.aupod22.service.*;

public class ButtonReciever extends BroadcastReceiver {    
    @Override
    public void onReceive(final Context ctx, Intent intent)
    {
        String act = intent.getAction();
        switch(act){
            case Intent.ACTION_MEDIA_BUTTON:
                KeyEvent evt = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if(evt!=null){
                    int action = evt.getAction();
                    switch(evt.getKeyCode()){
                        case KeyEvent.KEYCODE_HEADSETHOOK:
                            if(action == KeyEvent.ACTION_DOWN){
                                Intent i = new Intent(ctx, AudioService.class);
                                i.setAction(AudioService.CMD_MEDIA_BUTTON);
                                ctx.startService(i);
                            }
                            break;
                    }
                }
                break;
        }
    } 
}
