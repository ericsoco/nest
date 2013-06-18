package com.transmote.nest;

import processing.core.PApplet;
import processing.core.PImage;

import com.transmote.nest.NestSprite;
import com.transmote.nest.events.Event;

/**
 * A simple NestSprite wrapper for Processing's image() method.
 * Set path and call loadImage, and manipulate as with other NestSprites.
 * Also supports alpha.
 */
public class NestImageSprite extends NestSprite {
	public String path;
	public int tintR = 255;
	public int tintG = 255;
	public int tintB = 255;
	public float alpha = 1.0f;
	private PImage image;
	
	
	public NestImageSprite (String path) {
		this(path, true);
	}
	public NestImageSprite (String path, boolean autoload) {
		super();
		this.path = path;
		
		if (autoload) {
			addObserver(this);
		}
	}
	
	public void loadImage () {
		if (pApplet() == null) {
			System.err.println("NestImageSprite.loadImage requires a reference to PApplet.\n" +
					"Either provide one by calling loadImage(PApplet) or by attaching this" +
					"NestImageSprite instance to the display list before calling loadImage().");
		}
		loadImage(pApplet());
	}
	
	public void loadImage (PApplet p) {
		image = p.loadImage(path);
		if (width == 0) {
			width = image.width;
		}
		if (height == 0) {
			height = image.height;
		}
	}
	
	@Override
	public void draw (PApplet p) {
		if (image != null) {
			boolean tinted = alpha != 1.0 || tintR != 255 || tintG != 255 || tintB != 255;
			if (tinted) {
				p.tint(tintR, tintG, tintB, alpha*255);
			}
			
			if (width != image.width || height != image.height) {
				p.image(image, 0, 0, width, height);
			} else {
				p.image(image, 0, 0);
			}
			
			if (tinted) {
				p.noTint();
			}
		}
	}
	
	@Override
	protected void handleEvent (Event evt) {
		if (evt.target() == this) {
			switch (evt.type()) {
				case Event.ADDED_TO_STAGE :
					deleteObserver(this);
					loadImage(pApplet());
			}
		}
	}
}