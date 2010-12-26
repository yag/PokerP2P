package core.notifications;

import java.util.HashMap;

/**
 * <tt>Notification</tt> objects encapsulate information so that it can be
 * broadcast to other objects via the <tt>NotificationCenter</tt>.
 * @author RŽgis
 */

public class Notification {

	private UserInfo userInfo = null;
	private Object sender = null;
	private String name = null;
	
	/**
	 * @return Dictionary containing informations about the notification
	 */
	public HashMap<String, Object> getUserInfo() {
		return userInfo;
	}

	/**
	 * @param userInfo Dictionary containing informations about the notification
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * @return The object associated with the notification
	 */
	public Object getSender() {
		return sender;
	}

	/**
	 * @param sender The object associated with the notification
	 */
	public void setSender(Object sender) {
		this.sender = sender;
	}

	/**
	 * @return The name of the notification
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name of the notification
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Notification(String name, Object sender) {
		this.name = name;
		this.sender = sender;
		userInfo = null;
	}
	
	public Notification(String name, Object sender, 
			            UserInfo userInfo) {
		this.name = name;
		this.sender = sender;
		this.userInfo = userInfo;
	}
}
