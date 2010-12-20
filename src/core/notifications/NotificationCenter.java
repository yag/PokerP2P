package core.notifications;
import java.util.HashMap;
import java.util.HashSet;


/**
 * <tt>NotificationCenter</tt> is a Singleton class to provide a mechanism for
 * broadcasting information within a program.
 * 
 * <p>
 * In order to register yourself as an observer you must implement the
 * <tt>NotificationListener</tt> interface. Then, you can become an observer
 * by calling the <tt>addObserver()</tt> method of <tt>NotificationCenter</tt>.
 * </p>
 * 
 * <p>
 * In order to post a notification, you can invoke the <tt>postNotification</tt>
 * method with a notification name and a <tt>Notification</tt> object.
 * </p>
 * 
 * <p>
 * The implementation of <tt>NotificationCenter</tt> is synchronous and 
 * thread-safe so it is fine to call methods on it from multiple threads.
 * </p>
 * @author RŽgis
 *
 */

/* FIXME: It could be interesting to make notification calls asynchronous,
 *        while retaining thread safety properties.
 */
public class NotificationCenter {
	
	/**
	 * Contains for each notification the list of observers that should be
	 * notified when a new notification is posted.
	 */
	private HashMap<String, HashSet<NotificationListener>> observers;
	
	/**
	 * Singleton instance variable
	 */
	private static NotificationCenter defaultCenter = null;
	
	private NotificationCenter() {
		observers = new HashMap<String, HashSet<NotificationListener>>();
	}
	
	/**
	 * Get the singleton instance variable
	 * @return the shared <tt>NotificationCenter</tt> instance
	 */
	public static final synchronized NotificationCenter defaultCenter() {
		if (defaultCenter == null)
			defaultCenter = new NotificationCenter();
		
		return defaultCenter;
	}
	
	/**
	 * This method is used by client who wished to be notified when an event
	 * occured in the application.
	 * @param notificationName The name of the notification
	 * @param listener The callback method called when <tt>notificationName</tt>
	 * is posted.
	 */
	public synchronized void addObserver(String notificationName, NotificationListener listener) {
		HashSet<NotificationListener> set = observers.get(notificationName);
		if (set == null) {
			set = new HashSet<NotificationListener>();
			observers.put(notificationName, set);
		}
		set.add(listener);
	}
	
	/**
	 * Remove observer for a particular notification
	 * @param notificationName The name of the notification
	 * @param listener the observer
	 */
	public synchronized void removeObserver(String notificationName, NotificationListener listener) {
		HashSet<NotificationListener> set = observers.get(notificationName);
		if (set == null)
			return;
		
		set.remove(listener);
	}
	
	/**
	 * Creates a notification with a given name and sender and posts it to the
	 * receivers
	 * @param notificationName The name of the notification being posted
	 * @param sender The object posting the notification
	 */
	public synchronized void postNotification(String notificationName, Object sender) {
		HashSet<NotificationListener> set = observers.get(notificationName);
		if (set == null)
			return;
		Notification notification = new Notification(notificationName, sender);
		for (NotificationListener listener : set) {
			listener.notificationPosted(notification);
		}
	}
	
	/**
	 * Creates a notification with a given name, sender, and information and 
	 * posts it to the receivers
	 * @param notificationName The name of the notification
	 * @param sender The object posting the notification
	 * @param userInfo Information about the notification
	 */
	public synchronized void postNotification(String notificationName, Object sender, UserInfo userInfo) {
		HashSet<NotificationListener> set = observers.get(notificationName);
		if (set == null)
			return;
		Notification notification = new Notification(notificationName, sender, userInfo);
		for (NotificationListener listener : set) {
			listener.notificationPosted(notification);
		}
	}
}
