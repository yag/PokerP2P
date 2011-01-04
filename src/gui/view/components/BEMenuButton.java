package gui.view.components;

import gui.view.ButtonMenuPainter;
import gui.view.IPainter;

import java.awt.Color;

public class BEMenuButton extends BEButton {

	private static IPainter<BEButton> background = new ButtonMenuPainter<BEButton>();
	private static IPainter<BEButton> rollOver = new ButtonMenuPainter<BEButton>(0x323232, 0x2e2e2e, 0x131313, 0x181818);
	private static IPainter<BEButton> clickBackground = new ButtonMenuPainter<BEButton>(0x737373, 0x6e6e6e, 0x44444, 0x484848);;
	
	public BEMenuButton(String label) {
		super(label, background);
		setRolloverPainter(rollOver);
		setClickPainter(clickBackground);
		setForeground(Color.WHITE);
	}
}
