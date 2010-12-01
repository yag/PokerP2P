package core.protocol;

public class ChatMessage implements java.io.Serializable {
	public ChatMessage(Client author, String text, int date) {
		this.author = author;
		this.text = text;
		this.date = date;
	}
	public Client getAuthor() {
		return author;
	}
	public String getText() {
		return text;
	}
	public int getDate() {
		return date;
	}
	private Client author;
	private String text;
	private int date;
	private static final long serialVersionUID = 1L;
}
