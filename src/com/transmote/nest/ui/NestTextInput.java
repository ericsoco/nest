package com.transmote.nest.ui;

import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;

import com.transmote.nest.NestSprite;
import com.transmote.nest.events.KeyEvent;
import com.transmote.nest.events.MouseEvent;

public class NestTextInput extends NestSprite {
	private static final float TEXT_MARGIN = 0.1f;
	private static final long CURSOR_BLINK_DELAY = 500l;
	
	/**
	 * ARGB color of text when textfield is focused.
	 */
	public int textColor_focused = 0xFFFFFFFF;
	
	/**
	 * ARGB color of text when textfield is not focused.
	 */
	public int textColor_unfocused = 0xFFEEEEEE;
	
	/**
	 * ARGB color of default text in textfield.
	 */
	public int textColor_defaultText = 0xFFEEEEEE;
	
	/**
	 * ARGB color of textfield background when focused.
	 */
	public int fillColor_focused = 0x33FFFFFF;
	
	/**
	 * ARGB color of textfield background when not focused.
	 */
	public int fillColor_unfocused = 0x33FFFFFF;
	
	/**
	 * ARGB color of textfield border when focused.
	 */
	public int strokeColor_focused = 0xFFFFFFFF;
	
	/**
	 * ARGB color of textfield border when not focused.
	 */
	public int strokeColor_unfocused = 0xFFEEEEEE;
	
	/**
	 * Stroke weight of textfield border when focused.
	 */
	public int strokeWeight_focused = 3;
	
	/**
	 * Stroke weight of textfield border not focused.
	 */
	public int strokeWeight_unfocused = 2;
	
	/**
	 * PFont used in textfield.
	 */
	public PFont font;
	
	/**
	 * Text that appears in textfield when no text has yet been entered.
	 */
	public String defaultText;
	
	/**
	 * Maximum number of characters that can be entered into the textfield.
	 */
	public int maxNumChars = Integer.MAX_VALUE;
	
	/**
	 * If specified, only the characters in this String can be entered into the textfield.
	 */
	public String allowedChars = null;
	
	/**
	 * The specified NestTextInput will gain focus when SHIFT+TAB is typed into this NestTextInput.
	 */
	public NestTextInput prevTabGroupInput = null;
	
	/**
	 * The specified NestTextInput will gain focus when TAB is typed into this NestTextInput.
	 */
	public NestTextInput nextTabGroupInput = null;
	
	/**
	 * The specified NestButton will be programmatically clicked when ENTER is typed into this NestTextInput.
	 */
	public NestButton submitButton = null;
	
	private String inputString = "";
	private boolean hasFocus = false;
	private boolean justGainedFocus = false;	// ensure focus can't propagate to other NextTestInputs along with the KeyEvent (\t) that granted it
	private int cursorLoc = 0;
	private float cursorX = 0;
	
	/**
	 * Pass a List of NestTextInputs in order through which they can be tabbed.
	 * @param	textInputs	Next/previous tab inputs will be assigned in this order,
	 * 						also linking the first and last inputs.
	 */
	public static void chainTabInputs (List<NestTextInput> textInputs) {
		int numInputs = textInputs.size();
		NestTextInput textInput;
		for (int i=0; i<numInputs; i++) {
			textInput = textInputs.get(i);
			textInput.prevTabGroupInput = textInputs.get(((i-1)%numInputs + numInputs) % numInputs);
			textInput.nextTabGroupInput = textInputs.get((i+1)%numInputs);
		}
	}
	
	public NestTextInput (float width, float height, PFont font, String defaultText) {
		super(width, height);
		this.font = font;
		this.defaultText = defaultText;
	}
	
	/**
	 * Set the string entered into the textfield.
	 */
	public String inputString () {
		return inputString;
	}
	
	/**
	 * Get the string entered into the textfield.
	 */
	public void inputString (String val) {
		inputString = val;
	}
	
	/**
	 * Programmatically give focus to this NestTextInput instance.
	 */
	public void gainFocus () {
		if (hasFocus) { return; }
		hasFocus = justGainedFocus = true;
		
		cursorLoc(inputString.length());
	}
	
	/**
	 * Programmatically remove focus to this NestTextInput instance.
	 */
	public void loseFocus () {
		if (!hasFocus) { return; }
		hasFocus = justGainedFocus = false;
	}
	
	@Override
	public String toString () {
		return ("NestTextInput { inputString:"+ inputString +" }");
	}
	
	
	@Override
	protected void update (PApplet p) {
		justGainedFocus = false;
	}
	
	@Override
	protected void draw (PApplet p) {
		if (hasFocus) {
			drawCursor(p);
		}
		
//		int textColor = hasFocus ? textColor_focused : textColor_unfocused;
		int fillColor = hasFocus ? fillColor_focused : fillColor_unfocused;
		int strokeColor = hasFocus ? strokeColor_focused : strokeColor_unfocused;
		int strokeWeight = hasFocus ? strokeWeight_focused : strokeWeight_unfocused;
		
		// field background
		p.fill(fillColor);
		p.stroke(strokeColor);
		p.strokeWeight(strokeWeight);
		p.rect(0, 0, width, height);
		
		// input text
		updateInputText();
	}
	
	@Override
	protected void mouseReleased (MouseEvent e) {
		gainFocus();
	}
	
	@Override
	protected void mouseDown (MouseEvent e) {
		if (hitTest(e.getPoint(), false)) {
			// looking only for mouse down outside
			return;
		}
		loseFocus();
	}
	
	@Override
	protected void keyReleased (KeyEvent e) {
		if (!hasFocus) { return; }
		
		int keyCode = e.pKeyEvent().getKeyCode();		
		if (keyCode == 37) {
			// left arrow
			cursorLoc(cursorLoc-1);
		} else if (keyCode == 39) {
			// right arrow
			cursorLoc(cursorLoc+1);
		} else if (keyCode == PApplet.ENTER || keyCode == PApplet.RETURN) {
			if (submitButton != null) {
				submitButton.triggerClick();
			}
		}
	}
	
	@Override
	protected void keyTyped (KeyEvent e) {
		if (!hasFocus || justGainedFocus) { return; }
		
		// BUG: edits are still not made at cursor location!
		if (cursorLoc != inputString.length()) {
			System.out.println("TODO: edits are still not made at cursor location. fix this.");
		}
		
		char key = e.pKeyEvent().getKey();
		if (key == '\b') {
			// backspace
			if (inputString.length() <= 0) { return; }
			inputString(inputString.substring(0, inputString.length()-1));
			cursorLoc(cursorLoc-1);
		} else if (key == '\n') {
			// ignore newline in single-line textfields
			// TODO: implement multiline textfields
		} else if (key == '\t') {
			// cycle through tab group
			java.awt.event.KeyEvent awtKeyEvent = (java.awt.event.KeyEvent)(e.pKeyEvent().getNative());
			if (awtKeyEvent.isShiftDown()) {
				if (prevTabGroupInput != null) {
					loseFocus();
					prevTabGroupInput.gainFocus();
				}
			} else {
				if (nextTabGroupInput != null) {
					loseFocus();
					nextTabGroupInput.gainFocus();
				}
			}
		} else {
			// anything else
			if (inputString.length() >= maxNumChars) { return; }
			if (allowedChars != null && !allowedChars.contains(Character.toString(key))) { return; }
			inputString(inputString + key);
			cursorLoc(cursorLoc+1);
		}
	}
	
	protected void cursorLoc (int val) {
		cursorLoc = Math.max(0, Math.min(inputString.length(), val));
		cursorX = TEXT_MARGIN*width + pApplet().textWidth(inputString.substring(0, cursorLoc));
	}
	
	private void updateInputText () {
		PApplet p = pApplet();
		p.textAlign(PApplet.LEFT, PApplet.CENTER);
		
		p.textFont(font);
		if (!hasFocus) {
			if (inputString == "") {
				p.fill(textColor_defaultText);
				p.text(defaultText, TEXT_MARGIN*width, 0.4f*height);
				return;
			} else {
				p.fill(textColor_focused);
			}
		} else {
			p.fill(textColor_unfocused);
		}
		p.text(inputString, TEXT_MARGIN*width, 0, (1f-2f*TEXT_MARGIN)*width, height);
	}
	
	private void drawCursor (PApplet p) {
		if (Math.floor(System.currentTimeMillis() / CURSOR_BLINK_DELAY) % 2 != 0) { return; }
		
		p.stroke(textColor_focused);
		p.strokeWeight(2);
		p.line(cursorX, 0.2f*height, cursorX, 0.85f*height);
	}
}