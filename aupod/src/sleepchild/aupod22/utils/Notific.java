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
import android.content.res.*;

public class Notific{
    private Notific(){}
    
    public static Notification get(Context ctx, SongItem song, boolean isplaying){
        Notification.Builder b = new Notification.Builder(ctx);
        b.setSmallIcon(R.drawable.ic_launcher);
        b.setVisibility(Notification.VISIBILITY_PUBLIC);
        b.setPriority(Notification.PRIORITY_MAX);
        Resources res = ctx.getResources();
        //ThemeManager.Theme tm = ThemeManager.getTheme();
        
        RemoteViews rem = new RemoteViews(ctx.getPackageName(), R.layout.notification);
        
        rem.setOnClickPendingIntent(R.id.ant_btn_prev, getActionIntent(ctx, AudioService.CMD_PLAY_PREV));
        rem.setOnClickPendingIntent(R.id.ant_btn_next, getActionIntent(ctx, AudioService.CMD_PLAY_NEXT));
        rem.setOnClickPendingIntent(R.id.ant_btn_exit, getActionIntent(ctx, AudioService.CMD_END));
        rem.setOnClickPendingIntent(R.id.ant_btn_playpause, getActionIntent(ctx, AudioService.CMD_PLAY));
        //
        if(song.artist!=null && !song.artist.equalsIgnoreCase("<unknown>")){
            rem.setTextViewText(R.id.ant_title, tunc(song.title, 20));
            rem.setTextViewText(R.id.ant_artist, "  -  "+ tunc(song.artist,20));
        }else{
            rem.setTextViewText(R.id.ant_title, tunc(song.title, 40));
            rem.setTextViewText(R.id.ant_artist, "");
        }
        
        if(song.icon!=null){
            rem.setImageViewBitmap(R.id.ant_icon ,song.icon);
        }else{
            rem.setImageViewResource(R.id.ant_icon, R.drawable.cover_f);
        }
        //*
        if(isplaying){
            rem.setImageViewResource(R.id.ant_btn_playpause_icon, R.drawable.ic_pause);
            rem.setOnClickPendingIntent(R.id.ant_btn_playpause, getActionIntent(ctx, AudioService.CMD_PAUSE));
        }else{
            rem.setImageViewResource(R.id.ant_btn_playpause_icon, R.drawable.ic_play);
            rem.setOnClickPendingIntent(R.id.ant_btn_playpause, getActionIntent(ctx, AudioService.CMD_PLAY));
        }
        //*/
        b.setContentIntent(getContentIntent(ctx));
        b.setContent(rem);
        
        return b.build();
    }
    
    
    static Bitmap tint(Resources res, int id, int color){
        return BitmapUtils.tint(res, id, color);
    }
    
    private static String tunc(String text, int limit){
        if(text.length()>limit){
            text = text.substring(0,(limit-2)) + "...";
        }
        return text;
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
