package sleepchild.aupod22;

import android.app.Application;
import android.content.Context;
import sleepchild.aupod22.library.*;
import android.widget.*;
import java.util.*;
import java.io.*;
import sleepchild.aupod22.utils.*;

public class XApp extends Application{
    private static Context ctx;
    private static XApp inst;
    private static String logpathCrash = "/sdcard/.sleepchild/aupod/logs/crash/";
    private static final String logpathConsumable = "/sdcard/.sleepchild/aupod/logs/warning/";
    Thread.UncaughtExceptionHandler deftExceptionHandler;
    
    @Override
    public void onCreate(){
        super.onCreate();
        ctx = getApplicationContext();
        inst = this;
        
        deftExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            public void uncaughtException(Thread t, Throwable ex){
                dolog(t, ex);
                if(deftExceptionHandler!=null){
                    deftExceptionHandler.uncaughtException(t, ex);
                }else{
                    System.exit(1);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        });
        
    }
    
    void dolog(Thread t, Throwable ex){
        if(ex!=null){
            String date = new Date().toGMTString().toLowerCase().replace(" ","_");
            String msg = "aupod - crashlog - "+date+"\n\n";
            
            msg += ex.getMessage()+"\n";
            
            if(ex.getStackTrace()!=null){
                for( StackTraceElement tr : ex.getStackTrace()){
                    msg += ">>"+ tr.toString()+"\n";
                }
            }
            
            try
            {
                new File(logpathCrash).mkdirs();
                FileOutputStream o = new FileOutputStream(logpathCrash+"crashlog_"+date+".txt");
                o.write(msg.getBytes());
                o.flush();
                o.close();
            }
            catch (IOException e)
            {}

        }
    }
    
    //
    // todo: there should be an in-app option to turn this on/off; otherwise disable it in production
    //
    private static boolean lcexOFF=false;
    public static void logConsumableException(String tag, Exception e){
        if(lcexOFF){
            return;
        }
        new File(logpathConsumable).mkdirs();
        String date = new Date().toGMTString().toLowerCase().replace(" ","_");
        String path = logpathConsumable+"w_"+date+".txt";
        //
        String data = "AuPod - warning - "+date+"\n\n"
        //+"This is classified as a warning because the exception was caught and either handled or igonred;\nthe following are the details;\n\n"
        +"TAG: "+tag+":\n";
        
        data+= "class:: "+e.getClass().getName();
        data+="\nexception:: "+e.toString();
        data +="\nmessage:: "+e.getMessage()+"\n______\n";
        StackTraceElement[] trace = e.getStackTrace();
        if(trace!=null){
            data+="trace1::\n";
            for(StackTraceElement t : trace){
                data += ">>"+t+"\n";
            }
        }
        
        Throwable ex = e.getCause();
        if(ex!=null){
            data+= "\n\n\n________\ncause message:: "+ex.getMessage()+"\n";
            trace = ex.getStackTrace();
            if(trace!=null){
                for(StackTraceElement t : trace){
                    data += ">>"+t.toString()+"\n";
                }
            }
        }
        
        data+="\n\neof";
        //
        try
        {
            FileOutputStream o = new FileOutputStream(path);
            o.write(data.getBytes());
            o.flush();
            o.close();
        }
        catch (IOException xe)
        {}
    }
    
    public static XApp get(){
        return inst;
    }
    
    public static Context getContext(){
        return ctx;
    }
    
    public static void exit(){
        inst._end();
    }
    
    private void _end(){
        android.os.Process.killProcess(android.os.Process.myPid());
        Runtime.getRuntime().exit(1);
    }
    
}
