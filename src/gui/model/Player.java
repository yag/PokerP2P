package gui.model;


import core.model.Hand;

public class Player extends BEEntity {
	
	private String name;
	private int stack;
	private Hand hand;
	private boolean isPlaying;
	private boolean isDealer;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		String old = this.name;
		this.name = name;
		support.firePropertyChange("name", old, name);
	}
	
	public int getStack() {
		return stack;
	}
	public void setStack(int stack) {
		int old = this.stack;
		this.stack = stack;
		support.firePropertyChange("stack", old, stack);
	}
	
	public Hand getHand() {
		return hand;
	}
	public void setHand(Hand hand) {
		Hand old = this.hand;
		this.hand = hand;
		support.firePropertyChange("hand", old, hand);
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		boolean old = this.isPlaying;
		this.isPlaying = isPlaying;
		support.firePropertyChange("isPlaying", old, isPlaying);
	}
	
	public boolean isDealer() {
		return isDealer;
	}
	public void setDealer(boolean isDealer) {
		boolean old = this.isDealer;
		this.isDealer = isDealer;
		support.firePropertyChange("isDealer", old, isDealer);
	}
	
	
	
}
