package core.notifications;

/**
 * The interface must be implemented in order to receive notifications.
 * @author RŽgis
 */
public interface NotificationListener {
	public void notificationPosted(Notification notification);
}
