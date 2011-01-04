package gui.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class BEUtils {
	private BEUtils() {}
	
	public static void centerOnScreen(JFrame frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int w = frame.getWidth();
		int h = frame.getHeight();
		int x = (screenSize.width - w) / 2;
		int y = (screenSize.height - h) / 2;
		
		frame.setLocation(x, y);
	}
	
	public static Point getCenterOnScreen(Component frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int w = frame.getWidth();
		int h = frame.getHeight();
		int x = (screenSize.width - w) / 2;
		int y = (screenSize.height - h) / 2;
		
		return new Point(x, y);
		
	}
}
