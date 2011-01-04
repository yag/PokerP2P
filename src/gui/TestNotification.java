package gui;

import static core.notifications.ClientNotification.clientBecamePlayer;
import static core.notifications.ClientNotification.clientLoggedIn;
import static core.notifications.ClientNotification.clientLoggedOut;
import static core.notifications.ClientNotification.handBegan;
import static core.notifications.ClientNotification.handEnded;
import static core.notifications.ClientNotification.playerActed;
import static core.notifications.NotificationCenter.defaultCenter;
import core.notifications.Notification;
import core.notifications.NotificationListener;

public class TestNotification {
	public NotificationListener listener = new NotificationListener() {
		@Override
		public void notificationPosted(Notification notification) {
			//System.out.println("[Notification]" + notification.getName() + " successfully received !");
		}
	};
	
	public TestNotification() {
		defaultCenter().addObserver(clientLoggedIn, listener);
		defaultCenter().addObserver(clientLoggedOut, listener);
		defaultCenter().addObserver(clientBecamePlayer, listener);
		defaultCenter().addObserver(playerActed, listener);
		defaultCenter().addObserver(handBegan, listener);
		defaultCenter().addObserver(handEnded, listener);
		defaultCenter().addObserver(playerActed, listener);
	}
}
