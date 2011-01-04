package gui.view;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;

public class BEBoxLayoutUtils {

	private BEBoxLayoutUtils() {}
	
	public static Box makeColumn(Component...components) {
		Box b = Box.createHorizontalBox();
		for (Component component : components)
			b.add(component);
		
		return b;
	}
	
	public static Box makeLine(Component...components) {
		Box b = Box.createVerticalBox();
		for (Component component : components)
			b.add(component);
		
		return b;
	}
	
	public static Component vg() {return Box.createVerticalGlue();}
	public static Component hg() {return Box.createHorizontalGlue();}
	public static Component vs(int i) {
		return Box.createRigidArea(new Dimension(0, i));
	}
	public static Component hs(int i) {
		return Box.createRigidArea(new Dimension(i, 0));
	}
	
}
