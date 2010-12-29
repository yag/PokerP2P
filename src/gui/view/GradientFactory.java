package gui.view;

import java.awt.Color;
import java.awt.GradientPaint;

public class GradientFactory {
	public static GradientPaint createGradient(int hexColor1, int hexColor2, int height) {
		return new GradientPaint(0f, 0f, new Color(hexColor1), 0f, height, new Color(hexColor2));
	}
}
