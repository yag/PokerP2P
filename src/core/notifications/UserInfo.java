package core.notifications;

import java.util.HashMap;


/**
 * This class should be used in order to pass additional informations in 
 * <tt>Notification</tt> objects.
 * @author RŽgis
 *
 */
public class UserInfo extends HashMap<String, Object> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * We return a reference to the current object so that we can chain
	 * add() method calls.
	 */
	public UserInfo add(String key, Object value) {
		this.put(key, value);
		return this;
	}
}
