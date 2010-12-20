package core.notifications;

public final class ClientNotification {

	// Notification names
	public static final String clientLoggedIn = "clientLoggedIn";
	public static final String clientLoggedOut = "clientLoggedOut";
	public static final String clientBanned = "clientBanned";
	public static final String clientBecamePlayer = "clientBecamePlayer";
	public static final String clientBecameSpectator = "clientBecameSpectator";
	public static final String newChatMessage = "newChatMessage";
	public static final String playerActed = "playerActed";
	public static final String handBegan = "handBegan";
	public static final String handEnded = "handEnded";
	
	// Keys for userInfo
	public static final String kPlayerName = "kPlayerName";
	public static final String kWinnersList = "kWinnerName";
	
	public static final String kChatAuthor = "kChatAuthor";
	public static final String kChatMsg    = "kChatMsg";
}
