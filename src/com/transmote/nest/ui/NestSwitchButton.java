package com.transmote.nest.ui;

import processing.core.PApplet;
import processing.core.PFont;

import com.transmote.nest.NestSprite;
import com.transmote.nest.events.MouseEvent;

public class NestSwitchButton extends NestSprite {
	private int numStates;
	
	private int trackStrokeColor;
	private int trackFillColor;
	private int thumbStrokeColor;
	private int thumbFillColor;
	
	private String label;
	private String[] switchLabels;
	private PFont fontMain;
	private PFont fontSub;
	private int textColor;
	
	private float thumbVal;		// value from 0 to 1
	private float thumbPos;		// actual thumb coordinate, calculated in setThumbVal()
	private float switchLabelMargin;
	private float switchLabelSpacing;
	
	
	public NestSwitchButton () {
		this(2);
	}
	public NestSwitchButton (int numStates) {
		this(120, 24, numStates, 0xFFEEEEEE, 0x33EEEEEE, 0xFFFFFFFF, 0xFFCCCCCC, "", null, null, null, 0xFFFFFFFF);
	}
	public NestSwitchButton (float width, float height, int numStates, int trackStrokeColor, int trackFillColor, int thumbStrokeColor, int thumbFillColor, String label, String[] switchLabels, PFont fontMain, PFont fontSub, int textColor) {
		super(width, height);
		this.numStates = numStates;
		this.trackStrokeColor = trackStrokeColor;
		this.trackFillColor = trackFillColor;
		this.thumbStrokeColor = thumbStrokeColor;
		this.thumbFillColor = thumbFillColor;
		this.label = label;
		this.switchLabels = switchLabels;
		this.fontMain = fontMain;
		this.fontSub = fontSub;
		this.textColor = textColor;
		
		init();
	}
	
	public int getTrackStrokeColor () {
		return trackStrokeColor;
	}
	public void setTrackStrokeColor (int val) {
		trackStrokeColor = val;
	}
	
	public int getTrackFillColor () {
		return trackFillColor;
	}
	public void setTrackFillColor (int val) {
		trackFillColor = val;
	}
	
	public int getThumbStrokeColor () {
		return thumbStrokeColor;
	}
	public void setThumbStrokeColor (int val) {
		thumbStrokeColor = val;
	}
	
	public int getThumbFillColor () {
		return thumbFillColor;
	}
	public void setThumbFillColor (int val) {
		thumbFillColor = val;
	}
	
	public String getLabel () {
		return label;
	}
	public void setLabel (String val) {
		label = val;
	}
	
	public String[] getSwitchLabels () {
		return switchLabels;
	}
	public void setSwitchLabels (String[] val) {
		switchLabels = val;
	}
	
	public PFont getFontMain () {
		return fontMain;
	}
	public void setFontMain (PFont val) {
		fontMain = val;
	}
	
	public PFont getFontSub () {
		return fontSub;
	}
	public void setFontSub (PFont val) {
		fontSub = val;
	}
	
	public int getTextColor () {
		return textColor;
	}
	public void setTextColor (int val) {
		textColor = val;
	}
	
	/**
	 * The current position of the <tt>NestSwitchButton<tt>.
	 * @return		Zero-indexed int value (from 0 to <tt>numStates - 1</tt>).
	 */
	public int getState () {
		return (int)(thumbVal * (numStates-1));
	}
	public void setState (int val) {
		val = Math.max(0, Math.min(val, numStates-1));
		thumbVal = (float)val / (numStates-1);
		thumbPos = 0.5f*height + (width-height) * thumbVal;			// actual thumb position is inset from left/right edges
	}
	
	@Override
	protected void draw(PApplet p) {
		// draw track
		p.ellipseMode(PApplet.CORNER);
		p.stroke(trackStrokeColor);
		p.strokeWeight(2);
		p.fill(trackFillColor);
		p.arc(0f, 0f, height, height, 0.5f*PApplet.PI, 1.5f*PApplet.PI);
		p.arc(width-height, 0f, height, height, 1.5f*PApplet.PI, 2.5f*PApplet.PI);
		p.line(0.5f*height, 0, width-0.5f*height, 0);
		p.line(0.5f*height, height, width-0.5f*height, height);
		p.noStroke();
		p.rect(0.5f*height, 0, width-height, height);
		
		// draw thumb
		p.ellipseMode(PApplet.CENTER);
		p.noStroke();
		p.fill(thumbStrokeColor);
		p.ellipse(thumbPos, 0.5f*height, height-4, height-4);
		p.fill(thumbFillColor);
		p.ellipse(thumbPos, 0.5f*height, height-8, height-8);
		
		// draw main label
		if (label != "") {
			p.textAlign(PApplet.LEFT, PApplet.BASELINE);
			p.textFont(fontMain);
			p.fill(textColor);
			p.text(label, 0, -10);
			
			// underline
			p.line(0, -20, p.textWidth(label), -20);
		}
		
		// draw switch labels
		if (switchLabels == null) { return; }
		p.textAlign(PApplet.CENTER);
		String switchLabel;
		for (int i=0; i<numStates; i++) {
			if (switchLabels.length <= i) { break; }
			switchLabel = switchLabels[i];
			if (switchLabel == null) { continue; }
			
			p.textAlign(PApplet.CENTER, PApplet.TOP);
			p.textFont(fontSub);
			p.fill(textColor);
			p.text(switchLabel, i*switchLabelSpacing+switchLabelMargin, height+5);
			p.stroke(textColor);
		}
	}

	@Override
	protected void update (PApplet p) {}
	
	@Override
	protected void mouseReleased (MouseEvent e) {
		float clickLocation = mouseX() / width;
		int newState = Math.round(clickLocation * (numStates-1));
		if (newState != getState()) {
			setState(newState);
			dispatchEvent(e);
		}
	}
	
	private void init () {
		setState(0);
		switchLabelMargin = 0.5f * height;
		switchLabelSpacing = (width - 2*switchLabelMargin) / (numStates-1);
	}
}
