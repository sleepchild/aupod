package mrtubbs.postman;
import java.util.*;
import java.lang.reflect.*;
import android.content.*;
import android.widget.*;
import java.util.concurrent.*;


public class PostMan{
	//
	private volatile static PostMan deftInstance;
	/*
	 / holds a list of subscribers and ther subscribed subscriptions ;)
	 / the subscription is basicly an object with info abt the type and method of the subscription
	 */
	Map<Object, List<SubInfo>> subscriberMap = new HashMap<Object, List<SubInfo>>();


	// infomation abt a single subscription
	// the subscriber is currently subscribed to
	class SubInfo{
		public Class<?> type; // the param type of the sub
		public Method method; // the method to which the type is registered
	}

	// recomendation: use {@link #getInstance} instead
	PostMan(){
		this("DEFAULTS");
	}
	PostMan(String options){
		//do sth w/ the options
		//subscriberMap = new HashMap<Object, List<SubInfo>>();
	}

	// singleton for process-wide access
	public static PostMan getInstance(){
		PostMan instance = deftInstance;
		if(instance==null){
			synchronized (PostMan.class){
				instance = PostMan.deftInstance;
				if(instance==null){
					instance = PostMan.deftInstance = new PostMan();
				}
			}
		}
		return instance;
	}

	public void register(Object subscriber){
		List<Method> subscriberMethods = getQualifyngMethods(subscriber);
		if(subscriberMethods!=null){
			synchronized(this){
				for(Method method : subscriberMethods){
					Class<?>[] paramtype = method.getParameterTypes();
					if(paramtype.length==1){
						addSubsBySubscriber(subscriber,paramtype[0], method);
					}
				}
			}
		}
	}
    
    //public static void postt(){
        //getInstance().post("");
    //}

	public void post(Object event){
        //
		Class<?> eventType = event.getClass();
		Set<Object> sublist = subscriberMap.keySet();
		// loop through each subscriber's subscriptions and 
		// find a matching handler method for the event class
		for(Object sub : sublist){
			List<SubInfo> subinfos = subscriberMap.get(sub);
			for(SubInfo si : subinfos ){
				if(eventType.equals(si.type)){
					postSingleEvent(sub,si.method,event);
				}
			}
		}
	}
	

	public synchronized void unregister(Object subscriber){
		List<SubInfo> subinfo = subscriberMap.remove(subscriber);
		// TODO: lwhat to do with subinfo ?
	}

	///////////
	/// private Methods
	///////////

	/*
	 | 
	 */
	private List<Method> getQualifyngMethods(Object subscriber){
        List<Method> collect = new ArrayList<Method>();
		for(Method sm : subscriber.getClass().getMethods()){
			if(sm.getAnnotation(PostEvent.class) != null){
				collect.add(sm);
			}
		}
		if (collect.isEmpty()){
			return null;
		}
		return collect;
    }

	/*
	 / to be called in syncronised
	 */
	private void addSubsBySubscriber(Object subscriber, Class<?> type, Method method){
		//
		List<SubInfo> subscriptions = subscriberMap.get(subscriber);
		if(subscriptions==null){
			subscriptions = new ArrayList<SubInfo>();
			subscriberMap.put(subscriber, subscriptions);
		}
		SubInfo subinfo = new SubInfo();// aka subscription
		subinfo.type = type;
		subinfo.method = method;
		//
		subscriptions.add(subinfo);
		//
	}

	/*
	 |
	 */
	 
	private void postSingleEvent(final Object subscriber, final Method method, final Object event){
		// TODO:
		//*
		try
		{
			method.invoke(subscriber, event);
		}
		catch (InvocationTargetException e)
		{}
		catch (IllegalAccessException e)
		{}
		catch (IllegalArgumentException e)
		{}
		//*/
	}	


}
