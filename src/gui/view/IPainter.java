package gui.view;

import java.awt.Graphics2D;

/**
 * This interface is used to delegate painting of some components 
 */
public interface IPainter<T> {
	
	/**
	 * Paint to the given gContext. The graphic's state need not be cleaned since you are
	 * given a fresh context to work with. 
	 * @param gContext the graphics context to paint into.
	 * @param objectToPaint the object to be painted
	 * @param width the width within the object to paint
	 * @param height the height within the object to paint
	 */
	public void paint(Graphics2D gContext, T objectToPaint, int width, int height);
}
