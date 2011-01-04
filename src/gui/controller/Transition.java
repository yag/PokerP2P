package gui.controller;




import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.interpolation.SplineInterpolator;
import org.jdesktop.animation.timing.triggers.TimingTrigger;
import org.jdesktop.animation.timing.triggers.TimingTriggerEvent;

public class Transition implements TimingTarget {

//=================================================================== Components
	JFrame mainFrame;
	
	Container source;
	Container target;
	
	int targetWidth;
	int targetHeight;
	
	int targetX;
	int targetY;
	
	Animator animator;
	Animator fadeOutAnimator;
	Animator fadeInAnimator;
	PropertySetter resizeProperty;
	
	public Transition(JFrame mainFrame) {
		this.mainFrame = mainFrame;
		
		animator = new Animator(500, this);
		animator.setInterpolator(new SplineInterpolator(1f, 0f, 0f, 1f));
		
		fadeOutAnimator = new Animator(250);
		fadeInAnimator = new Animator(250);
		fadeOutAnimator.setInterpolator(new SplineInterpolator(1f, 0f, 0f, 1f));
		fadeInAnimator.setInterpolator(new SplineInterpolator(1f, 0f, 0f, 1f));
		
		fadeInAnimator.addTarget(new PropertySetter(mainFrame.getContentPane(), "alpha", 1.0f));
		fadeOutAnimator.addTarget(new PropertySetter(mainFrame.getContentPane(), "alpha", 0.0f));
		
		TimingTrigger
			.addTrigger(fadeOutAnimator, animator, TimingTriggerEvent.STOP);
		TimingTrigger
			.addTrigger(animator, fadeInAnimator, TimingTriggerEvent.STOP);
	}
	
	

//================================================================== Transitions
	@Override
	public void begin() {
		targetWidth = target.getPreferredSize().width;
		targetHeight = target.getPreferredSize().height;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		targetX = (screenSize.width - targetWidth) / 2; 
		targetY = (screenSize.height - targetHeight) / 2;
		
		if (source != null)
			mainFrame.remove(source);
		resizeProperty = new PropertySetter(mainFrame, "bounds", new Rectangle(targetX, targetY, targetWidth, targetHeight));
		animator.addTarget(resizeProperty);
	}

	@Override
	public void end() {
		if (source != null) {
			mainFrame.add(target);
			mainFrame.pack();
		}
		animator.removeTarget(resizeProperty);
		AppController.app().transitionComplete();
	}

	@Override
	public void repeat() {}

	@Override
	public void timingEvent(float f) { mainFrame.repaint(); }
	
	public void setTransition(Container source, Container target) {
		this.source = source;
		this.target = target;
		
		fadeOutAnimator.start();
		//animator.start();
	}
	
	public void setSkipFadeOutTransition(Container source, Container target) {
		this.source = source;
		this.target = target;
		
		animator.start();
	}
	
	
	
}
