package gui.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class BEEntity {
	
	//------------------------------------------------- Java Beans Specification
	protected PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		support.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		support.removePropertyChangeListener(l);
	}
	//-------------------------------------------------------------------------

}
