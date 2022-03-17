package sleepchild.aupod22.utils;

import android.app.*;
import android.content.*;
import android.media.session.*;
import android.graphics.*;
import android.widget.RemoteViews;
import android.widget.*;
import android.graphics.drawable.*;
import sleepchild.aupod22.activity.*;
import sleepchild.aupod22.models.*;
import sleepchild.aupod22.*;
import sleepchild.aupod22.service.*;

public class Notific{
    private Notific(){}
    
    public static Notification get(Context ctx, SongItem song, boolean isPlaying){
        Notification.Builder b = new Notification.Builder(ctx);
        b.setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(song.title)
            .setContentText(song.artist)
            .setOngoing(isPlaying)
            .setShowWhen(false)
            .setLargeIcon(song.icon)
            .setVisibility(Notification.VISIBILITY_PUBLIC)
            .setPriority(Notification.PRIORITY_MAX)
            .setContentIntent(getContentIntent(ctx));
            
        b.setColor(ctx.getResources().getColor(R.color.pluto));
            
        if(song.icon==null){
            b.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.fallback_cover));
        }
            
        b.addAction(R.drawable.ic_prev_24px,"prev",getActionIntent(ctx, AudioService.CMD_PLAY_PREV));
        if(isPlaying){
            b.addAction(R.drawable.ic_pause_24px,"pause",getActionIntent(ctx, AudioService.CMD_PAUSE));
        }else{
            b.addAction(R.drawable.ic_play_24px,"play", getActionIntent(ctx, AudioService.CMD_PLAY));
        }
        b.addAction(R.drawable.ic_next_24px, "next", getActionIntent(ctx, AudioService.CMD_PLAY_NEXT));
        b.setStyle(
            new Notification.MediaStyle()
                .setShowActionsInCompactView(new int[]{0,1,2})
        );
        
        return b.build();
    }
    
    private static PendingIntent getActionIntent(Context ctx, String action){
        Intent i = new Intent(ctx, AudioService.class);
        i.setAction(action);
        return PendingIntent.getService(ctx, App.REQUEST_CODE_AUDIOSERVICE, i ,0);
    }
    
    private static PendingIntent getContentIntent(Context ctx){
        return PendingIntent.getActivity(
            ctx,
            MainActivity.REQUEST_CODE,
            new Intent(ctx, PlayerActivity.class),
            Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}
