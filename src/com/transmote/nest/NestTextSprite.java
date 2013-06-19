package com.transmote.nest;

import processing.core.PApplet;
import processing.core.PFont;

import com.transmote.nest.NestSprite;

/**
 * A simple NestSprite wrapper for Processing's text() method.
 * Set color, font, and align once, and manipulate as with other NestSprites.
 * Also supports alpha.
 * 
 * If width or height is set for this instance,
 * they will both be used as bounds for the text field, and the text will wrap accordingly;
 * otherwise, the text will all be printed to a single, unbounded line.
 */
public class NestTextSprite extends NestSprite {
	public String text;
	public int color;
	public PFont font;
	public int align;
	public float alpha = 1.0f;
	public int rectMode = PApplet.CORNER;
	
	public NestTextSprite (String text) {
		this(text, 0, null, PApplet.LEFT);
	}
	public NestTextSprite (String text, int color) {
		this(text, color, null, PApplet.LEFT);
	}
	public NestTextSprite (String text, int color, PFont font) {
		this(text, color, font, PApplet.LEFT);
	}
	public NestTextSprite (String text, int color, PFont font, int align) {
		super();
		this.text = text;
		this.color = color;
		this.font = font;
		this.align = align;
	}
	
	@Override
	public void draw (PApplet p) {
		p.fill((color & 0x00FFFFFF) | ((int)(alpha*255) << 24));
		p.textFont(font);
		p.textAlign(align);
		p.rectMode(rectMode);
		if (width > 0 || height > 0) {
			p.text(text, 0, 0, width, height);
		} else {
			p.text(text, 0, 0);
		}
	}
}