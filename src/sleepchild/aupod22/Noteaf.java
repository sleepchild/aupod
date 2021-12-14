package sleepchild.aupod22;
import android.app.*;
import android.content.*;
import android.media.session.*;
import android.graphics.*;
import android.widget.RemoteViews;
import android.widget.*;

public class Noteaf
{
    //Context ctx;
    
    //public Noteaf(Context ctx){
        //this.ctx = ctx;
    //}
    
    public static Notification get21(Context ctx, MediaSession ms, String title, String content, boolean isPlaying){
        Notification.Builder b = new Notification.Builder(ctx);
        b.setSmallIcon(R.drawable.ic_launcher);
        b.setContentTitle(title);
        b.setContentText(content);
        b.setOngoing(isPlaying);
        if(isPlaying){
            b.addAction(R.drawable.ic_pause,"pause",getPauseIntent(ctx));
        }else{
            b.addAction(R.drawable.ic_play,"play", getPlayIntent(ctx));
        }
        b.setStyle(
            new Notification.MediaStyle()
            .setShowActionsInCompactView(new int[]{0})
            .setMediaSession(ms.getSessionToken())
        );
        b.setPriority(Notification.PRIORITY_MAX);
        b.setVisibility(Notification.VISIBILITY_PUBLIC);
        b.setContentIntent(getContentIntent(ctx));
        return b.build();
    }
    
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
            
        b.addAction(R.drawable.ic_prev_24px,"prev",getPlayPrevIntent(ctx));
        if(isPlaying){
            b.addAction(R.drawable.ic_pause_24px,"pause",getPauseIntent(ctx));
        }else{
            b.addAction(R.drawable.ic_play_24px,"play", getPlayIntent(ctx));
        }
        b.addAction(R.drawable.ic_next_24px, "next", getplayNextIntent(ctx));
        //*
        b.setStyle(
            new Notification.MediaStyle()
                .setShowActionsInCompactView(new int[]{0,1,2})
        );
        //*/
        Notification n = b.build();
        /*
        n.contentView = getView(ctx);
        n.bigContentView = getView(ctx);
        ///*/
        return n;
    }
    
    public static void post(Context ctx, int nid, Notification n){
         NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
         nMgr.notify(nid, n);
    }
    
    public static RemoteViews getView(Context ctx){
        RemoteViews remote = new RemoteViews(ctx.getPackageName(), R.layout.notificationlayout);
        
        return remote;
    }
    
    public static PendingIntent getPlayIntent(Context ctx){
        Intent i = new Intent(ctx, AudioService.class);
        i.setAction(AudioService.CMD_PLAY);
        return PendingIntent.getService(
           ctx,
           AudioService.requestCode,
           i
           ,0);
    }
    
    public static PendingIntent getPauseIntent(Context ctx){
        Intent i = new Intent(ctx, AudioService.class);
        i.setAction(AudioService.CMD_PAUSE);
        return PendingIntent.getService(ctx, AudioService.requestCode, i ,0);
    }
    
    public static PendingIntent getplayNextIntent(Context ctx){
        Intent i = new Intent(ctx, AudioService.class);
        i.setAction(AudioService.CMD_PLAY_NEXT);
        return PendingIntent.getService(ctx, AudioService.requestCode, i ,0);
    }
    
    public static PendingIntent getPlayPrevIntent(Context ctx){
        Intent i = new Intent(ctx, AudioService.class);
        i.setAction(AudioService.CMD_PLAY_PREV);
        return PendingIntent.getService(ctx, AudioService.requestCode, i ,0);
    }
    
    public static PendingIntent getEndIntent(Context ctx){
        Intent i = new Intent(ctx, AudioService.class);
        return PendingIntent.getService(ctx, AudioService.requestCode, i ,0);
    }
    
    public static PendingIntent getContentIntent(Context ctx){
        return PendingIntent.getActivity(
            ctx,
            MainActivity.requestCode,
            new Intent(ctx, MainActivity.class),
            Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}
