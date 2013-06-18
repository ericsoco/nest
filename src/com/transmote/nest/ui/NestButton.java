package com.transmote.nest.ui;

import java.awt.geom.Point2D;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import com.transmote.nest.NestSprite;
import com.transmote.nest.events.Event;
import com.transmote.nest.events.MouseEvent;

/**
 * NestButton provides a simple button UI element for use within the Nest framework.
 * NestButton allows for a stroke+fill background, a text label, and a PImage to use as the background.
 *  
 * To handle a NestButton click, the NestButton's parent should add itself as an observer to the NestButton.
 * The MOUSE_CLICKED event will bubble up from the NestButton to its parent, where it can be handled within <tt>handleEvent()</tt>.
 * 
 * <tt>
 * NestSprite buttonContainer = new NestSprite();
 * NestButton button = new NestButton(width, height, label, font);
 * buttonContainer.addChild(button);
 * 
 * // ...within buttonContainer:
 * protected void handleEvent (Event event) {
 *     if (event.target() == button && event.type() == MouseEvent.MOUSE_CLICKED) {
 *         // respond to click here
 *     }
 * }
 * </tt>
 */
public class NestButton extends NestSprite {
	/**
	 * ARGB color of text when button is not pressed.
	 */
	public int textColor_up = 0xFFEEEEEE;
	
	/**
	 * ARGB color of text when button is pressed.
	 */
	public int textColor_down = 0xFFFFFFFF;
	
	/**
	 * ARGB color of button background when button is not pressed.
	 */
	public int fillColor_up = 0x33FFFFFF;
	
	/**
	 * ARGB color of button background when button is pressed.
	 */
	public int fillColor_down = 0x66FFFFFF;
	
	/**
	 * ARGB color of button outline when button is not pressed.
	 */
	public int strokeColor_up = 0xFFEEEEEE;
	
	/**
	 * ARGB color of button outline when button is pressed.
	 */
	public int strokeColor_down = 0xFFFFFFFF;
	
	/**
	 * Stroke weight of button outline.
	 */
	public int strokeWeight = 2;
	
	/**
	 * Radius of corners.
	 */
	public float cornerRadius = 0;
	
	/**
	 * As with PApplet.rect(), specifies whether to draw button from upper-left corner or from center.
	 */
	public int rectMode = PApplet.CORNER;
	
	private String label;
	private PFont font;
	private PImage icon;
	private boolean pressed;
	
	
	public NestButton (String label, PFont font) {
		this(0, 0, label, font, null);
		addObserver(this);
	}
	public NestButton (float width, float height, String label, PFont font) {
		this(width, height, label, font, null);
	}
	public NestButton (float width, float height, String label, PFont font, PImage icon) {
		super(width, height);
		
		this.label = label;
		this.font = font;
		this.icon = icon;
	}
	
	/**
	 * Text label that appears within button.
	 */
	public void setLabel (String label) {
		this.label = label;
		updateAutosize();
	}
	/**
	 * Text label that appears within button.
	 */
	public String getLabel () {
		return label;
	}
	
	/**
	 * Font to use for text label.
	 */
	public void setFont (PFont font) {
		this.font = font;
		updateAutosize();
	}
	/**
	 * Font to use for text label.
	 */
	public PFont getFont () {
		return font;
	}
	
	/**
	 * PImage to use as button.
	 * If specified, icon is drawn behind stroke and text.
	 */
	public void setIcon (PImage icon) {
		this.icon = icon;
		updateAutosize();
	}
	public PImage getIcon () {
		return icon;
	}
	
	/**
	 * Triggers a MouseEvent.MOUSE_CLICKED event, centered on the button.
	 */
	public void triggerClick () {
		triggerMouseEvent(0.5f*width, 0.5f*height, MouseEvent.MOUSE_CLICKED);
	}
	
	@Override
	protected void draw (PApplet p) {
		int textColor = pressed ? textColor_down : textColor_up;
		int fillColor = pressed ? fillColor_down : fillColor_up;
		int strokeColor = pressed ? strokeColor_down : strokeColor_up;
		
		// icon
		if (icon != null) {
			switch (rectMode) {
				case PApplet.CORNER:
					p.image(icon, 0, 0);
					break;
				case PApplet.CENTER:
					p.image(icon, (int)(0.5f*(width - icon.width)), (int)(0.5f*(height - icon.height)));
					break;
			}
		}
		
		// background
		p.strokeWeight(strokeWeight);
		p.stroke(strokeColor);
		p.fill(fillColor);
		
		p.rectMode(rectMode);
		p.rect(0, 0, width, height, cornerRadius);
		
		// label
		if (font == null) { return; }
		p.fill(textColor);
		p.textFont(font);
		p.textAlign(PApplet.CENTER, PApplet.CENTER);
		float pressedOffset = (pressed ? 0.025f : 0);
		switch (rectMode) {
			case PApplet.CORNER:
				p.text(label, (int)(0.5f*width), (int)((0.475f+pressedOffset) * height));
				break;
			case PApplet.CENTER:
				p.text(label, 0, (int)((-0.075f+pressedOffset) * height));
				break;
		}
	}
	
	@Override
	protected void handleEvent (Event evt) {
		if (evt.target() == this) {
			switch (evt.type()) {
				case Event.ADDED_TO_STAGE :
					deleteObserver(this);
					updateAutosize();
					break;
			}
		}
	}

	@Override
	protected void mousePressed (MouseEvent e) {
		pressed = true;
	}
	
	@Override
	protected void mouseUp (MouseEvent e) {
		if (!pressed) { return; }
		pressed = false;
		
		Point2D.Float localPt = globalToLocal(e.getPoint());
		if (hitTest(localPt, false)) {
			println("UP");
			// mouseUp inside bounds, so dispatch CLICKED event at that location
			triggerMouseEvent(localPt.x, localPt.y, MouseEvent.MOUSE_CLICKED);
		}
	}
	
	private void updateAutosize () {
		int iconWidth = (icon == null) ? icon.width : 0;
		int iconHeight = (icon == null) ? icon.height : 0;
		
		int textWidth = 0;
		int textHeight = 0;
		if (font != null && label != null) {
			PApplet p = pApplet();
			p.textFont(font);
			textWidth = (int)(1.1f * p.textWidth(label));
			textHeight = (int)(1.25f * (p.textAscent() + p.textDescent()));
		}
		
		width = Math.max(iconWidth, textWidth);
		height = Math.max(iconHeight, textHeight);
	}
}