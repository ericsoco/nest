/*
 * This file is part of Nest.
 * Nest - http://transmote.com/nest/ is a
 * derivative of Processing - http://processing.org/ 
 * Copyright (C) 2011, Eric Socolofsky
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.transmote.nest;
import com.transmote.nest.events.Event;

import com.transmote.nest.events.EventDispatcher;
import com.transmote.nest.events.KeyEvent;
import com.transmote.nest.events.MouseEvent;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * <p>
 * A NestSprite instance is a single element in the Nest display list.
 * Any on-screen element using the Nest framework is an instance of NestSprite,
 * or of a subclass of NestSprite.  Nest is modeled on the display list
 * at the core of Adobe's <a href="http://www.adobe.com/devnet/actionscript.html">ActionScript</a>
 * programming language; NestSprite is in turn modeled on ActionScript's
 * <a href="http://livedocs.adobe.com/flash/9.0/ActionScriptLangRefV3/flash/display/Sprite.html">Sprite</a>
 * class, and offers much of the same functionality.
 * </p><p>
 * NestSprite instances can be translated, rotated, and scaled via public properties
 * such as {@link #x}, {@link #rotationZ}, and {@link #scaleY}.
 * NestSprite methods provide for addition to ({@link #addChild(NestSprite)}),
 * removal from ({@link #removeChild(NestSprite)}), and manipulation of
 * (e.g. {@link #swapChildren(NestSprite, NestSprite)}), the display list.
 * NestSprite also allows for comparing between coordinate spaces,
 * with methods such as {@link #mousePt()} and {@link #screenPt()}.
 * </p><p>
 * Every frame, each NestSprite instance on the display list will automatically call
 * {@link #update(PApplet)}, then each NestSprite instance on the display list will call
 * {@link #draw(PApplet)}.  To implement specific functionality, such as animation
 * or rendering, developers should subclass NestSprite and implement these methods as desired.
 * </p><p>
 * NestSprite instances in the display list transport events through the display list.
 * See {@link com.transmote.nest.events.Event} for more detail on the event flow.
 * To handle a mouse or key event, a NestSprite subclass must implement the event handler
 * corresponding to the type of mouse event to handle.
 * For mouse events to respect the bounds rect of the NestSprite,
 * the {@link #bounds} rect must be set and updated manually.
 * </p><p>
 * Note that NestSprite instances will not call {@link #update(PApplet)} or {@link #draw(PApplet)},
 * nor receive mouse or key events, until they are added to the display list.
 * </p>
 * 
 * @author		Eric Socolofsky
 */
public class NestSprite extends EventDispatcher {
	/**
	 * Signifies that this NestSprite does not set its own blendMode,
	 * but lets display list ancestors do so.  If no ancestors set blendMode,
	 * the default Processing blend mode will be used.
	 */
	public static final int NO_BLEND_MODE = -1;
	
	/**
	 * The position of the centerpoint (aka 'registration point')
	 * of this NestSprite instance on the x-axis of its parent. 
	 */
	public float x = 0.0f;
	
	/**
	 * The position of the centerpoint (aka 'registration point')
	 * of this NestSprite instance on the y-axis of its parent. 
	 */
	public float y = 0.0f;
	
	/**
	 * The position of the centerpoint (aka 'registration point')
	 * of this NestSprite instance on the z-axis of its parent. 
	 */
	public float z = 0.0f;
	
	/**
	 * <p>
	 * The width of the graphic content of this NestSprite instance.
	 * </p><p>
	 * NestSprite calculates its {@link #bounds()} using
	 * <tt>width</tt>, <tt>height</tt>, <tt>boundsLeft</tt>, and <tt>boundsRight</tt>.
	 * Setting any of these properties will change the bounds rect
	 * of this NestSprite instance, and setting the bounds rect
	 * via {@link #setBounds(java.awt.geom.Rectangle2D.Float)} will, in turn, update those properties.
	 * </p><p>
	 * In order for this NestSprite instance to handle mouse events that require the cursor
	 * be over its graphic content, <tt>width</tt> and <tt>height</tt> must both be set manually.
	 * Additionally, if the upper-left corner of the graphic content is not at (0,0),
	 * {@link #boundsLeft} and {@link #boundsTop} must be set to indicate this;
	 * <tt>width</tt> and <tt>height</tt> should be measured from the upper-left corner
	 * of the graphic content.
	 * </p><p>
	 * Perhaps, someday, Nest will be smart enough to update its bounds rect
	 * automatically as its graphic content changes, but for now, it's up to the
	 * application developer to manually update the bounds rect.
	 * </p>
	 * 
	 * @see #height
	 * @see #boundsLeft
	 * @see #boundsTop
	 * @see #bounds()
	 * @see #setBounds(java.awt.geom.Rectangle2D.Float)
	 * 
 	 * @example		MouseEvents_Bounds
	 */
	public float width;
	
	/**
	 * <p>
	 * The height of the graphic content of this NestSprite instance.
	 * </p><p>
	 * NestSprite calculates its {@link #bounds()} using
	 * <tt>width</tt>, <tt>height</tt>, <tt>boundsLeft</tt>, and <tt>boundsRight</tt>.
	 * Setting any of these properties will change the bounds rect
	 * of this NestSprite instance, and setting the bounds rect
	 * via {@link #setBounds(java.awt.geom.Rectangle2D.Float)} will, in turn, update those properties.
	 * </p><p>
	 * In order for this NestSprite instance to handle mouse events that require the cursor
	 * be over its graphic content, <tt>width</tt> and <tt>height</tt> must both be set manually.
	 * Additionally, if the upper-left corner of the graphic content is not at (0,0),
	 * {@link #boundsLeft} and {@link #boundsTop} must be set to indicate this;
	 * <tt>width</tt> and <tt>height</tt> should be measured from the upper-left corner
	 * of the graphic content.
	 * </p><p>
	 * Perhaps, someday, Nest will be smart enough to update its bounds rect
	 * automatically as its graphic content changes, but for now, it's up to the
	 * application developer to manually update the bounds rect.
	 * </p>
	 * 
	 * @see #width
	 * @see #boundsLeft
	 * @see #boundsTop
	 * @see #bounds()
	 * @see #setBounds(java.awt.geom.Rectangle2D.Float)
	 * 
 	 * @example		MouseEvents_Bounds
	 */
	public float height;
	
	/**
	 * <p>
	 * The left edge of the graphic content of this NestSprite instance.
	 * </p><p>
	 * NestSprite calculates its {@link #bounds()} using
	 * <tt>width</tt>, <tt>height</tt>, <tt>boundsLeft</tt>, and <tt>boundsRight</tt>.
	 * Setting any of these properties will change the bounds rect
	 * of this NestSprite instance, and setting the bounds rect
	 * via {@link #setBounds(java.awt.geom.Rectangle2D.Float)} will, in turn, update those properties.
	 * </p><p>
	 * In order for this NestSprite instance to handle mouse events that require the cursor
	 * be over its graphic content, <tt>width</tt> and <tt>height</tt> must both be set manually.
	 * Additionally, if the upper-left corner of the graphic content is not at (0,0),
	 * {@link #boundsLeft} and {@link #boundsTop} must be set to indicate this;
	 * <tt>width</tt> and <tt>height</tt> should be measured from the upper-left corner
	 * of the graphic content.
	 * </p><p>
	 * Perhaps, someday, Nest will be smart enough to update its bounds rect
	 * automatically as its graphic content changes, but for now, it's up to the
	 * application developer to manually update the bounds rect.
	 * </p>
	 * 
	 * @see #width
	 * @see #height
	 * @see #boundsTop
	 * @see #bounds()
	 * @see #setBounds(java.awt.geom.Rectangle2D.Float)
	 * 
 	 * @example		MouseEvents_Bounds
	 */
	public float boundsLeft;
	
	/**
	 * <p>
	 * The top edge of the graphic content of this NestSprite instance.
	 * </p><p>
	 * NestSprite calculates its {@link #bounds()} using
	 * <tt>width</tt>, <tt>height</tt>, <tt>boundsLeft</tt>, and <tt>boundsRight</tt>.
	 * Setting any of these properties will change the bounds rect
	 * of this NestSprite instance, and setting the bounds rect
	 * via {@link #setBounds(java.awt.geom.Rectangle2D.Float)} will, in turn, update those properties.
	 * </p><p>
	 * In order for this NestSprite instance to handle mouse events that require the cursor
	 * be over its graphic content, <tt>width</tt> and <tt>height</tt> must both be set manually.
	 * Additionally, if the upper-left corner of the graphic content is not at (0,0),
	 * {@link #boundsLeft} and {@link #boundsTop} must be set to indicate this;
	 * <tt>width</tt> and <tt>height</tt> should be measured from the upper-left corner
	 * of the graphic content.
	 * </p><p>
	 * Perhaps, someday, Nest will be smart enough to update its bounds rect
	 * automatically as its graphic content changes, but for now, it's up to the
	 * application developer to manually update the bounds rect.
	 * </p>
	 * 
	 * @see #width
	 * @see #height
	 * @see #boundsLeft
	 * @see #bounds()
	 * @see #setBounds(java.awt.geom.Rectangle2D.Float)
	 * 
 	 * @example		MouseEvents_Bounds
	 */
	public float boundsTop;
	
	/**
	 * The scale of this NestSprite instance along the x-axis. 
	 */
	public float scaleX = 1.0f;
	
	/**
	 * The scale of this NestSprite instance along the y-axis. 
	 */
	public float scaleY = 1.0f;
	
	/**
	 * The scale of this NestSprite instance along the z-axis. 
	 */
	public float scaleZ = 1.0f;
	
	/**
	 * The rotation of this NestSprite instance along the x-axis, in radians. 
	 */
	public float rotationX = 0.0f;
	
	/**
	 * The rotation of this NestSprite instance along the y-axis, in radians. 
	 */
	public float rotationY = 0.0f;
	
	/**
	 * The rotation of this NestSprite instance along the z-axis, in radians. 
	 */
	public float rotationZ = 0.0f;
	
	/**
	 * For best performance, set blendMode as high up the display list as possible.
	 * For example, if all of the children of a NestSprite have the same non-default blendMode,
	 * rather than setting the blendMode on each of those children, it's best to
	 * set the blendMode only on the parent NestSprite.
	 * 
	 * To reset the blendMode for this NestSprite instance (to let ancestor NestSprites
	 * determine how this NestSprite will be drawn), set it to {@link #NO_BLEND_MODE}.
	 * Defaults to {@link #NO_BLEND_MODE}.
	 * 
	 * TODO: set back to whatever blendmode was used before NestSpriteContainer.update,
	 * 		 once the processing API exposes it (currently resets to PROCESSING_DEFAULT_BLEND_MODE)
	 */
	public int blendMode = NO_BLEND_MODE;
	
	/**
	 * The on-screen visibility of this NestSprite instance (and its descendants).
	 * If visible == false, neither this NestSprite instance nor its descendants
	 * will receive input events. 
	 */
	public boolean visible = true;
	
	/**
	 * Disables GL depth testing for this NestSprite instance and all its descendants.
	 * Setting <tt>inFront = true</tt> will eliminate flickering caused by other shapes
	 * drawn at the same plane (z-coordinate).
	 * This does <b>not</b> bring a NestSprite instance to the front of the stack;
	 * it is used only to eliminate GL-induced flickering.
	 * If not using a GL renderer, this field is ignored.
	 */
	public boolean inFront = false;
	
	/**
	 * <p>
	 * Specifies whether this NestSprite instance receives input events,
	 * such as MouseEvents and KeyEvents.  The default value is <tt>true</tt>,
	 * which means that by default any NestSprite on the display list receives input events.
	 * </p><p>
	 * Any children of this instance on the display list are not affected.
	 * To prevent or enable input events reaching children of this instance,
	 * use {@link #inputChildren}.
	 * </p>
	 */
	public boolean inputEnabled = true;
	
	/**
	 * Specifies whether children of this NestSprite instance receive input events,
	 * such as MouseEvents and KeyEvents.  The default value is <tt>true</tt>,
	 * which means that by default all children of this instance receive input events. 
	 */
	public boolean inputChildren = true;
	
	/**
	 * <p>
	 * Specifies whether input events (e.g. MouseEvent, KeyEvent) in the CAPTURE phase
	 * will be handled by input event handlers.  Setting to <tt>true</tt> does not affect
	 * handling of input events in the AT_TARGET or CAPTURE phases.
	 * </p>
	 * <p>
	 * Note that due to the implementation used in Nest,
	 * which entails running a hitTest() on all descendants of this NestSprite instance,
	 * setting to <tt>true</tt> may result in decreased performance.
	 * The implementation can probably use some optimization.
	 * </p>
	 * <p> 
	 * Defaults to <tt>false</tt>.
	 * </p>
	 */
	public boolean handleCaptureEvents = false;
	
	protected PGraphics g;
	
	NestSprite parent;
	ArrayList<NestSprite> childList;
	PApplet p;
	float screenX;
	float screenY;
	float mouseX;
	float mouseY;
	
	private static final int PROCESSING_DEFAULT_BLEND_MODE = PApplet.BLEND;
	private static int currentBlendMode = NO_BLEND_MODE;
	
	private AffineTransform transform;
	private Point2D.Float localPt;
	private Point2D.Float screenPt;
	private Point2D.Float mousePt;
	private Rectangle2D.Float bounds = new Rectangle2D.Float();
	
	private boolean rendererIs3D = false;
	private boolean rendererIsGL = false;
	private boolean rendererSupportsUpdatePixels = true;
	private boolean isDisposed = false;
	private int previousBlendMode = NO_BLEND_MODE;
	
	private Method updateOverride = null;
	private Object updateOverrideTarget = null;
	private Method drawOverride = null;
	private Object drawOverrideTarget = null;

	
	//-----<CONSTRUCTORS>--------------------------------------------//
	/**
	 * Creates a new NestSprite instance with a zero bounds rect.
	 */
	public NestSprite () {
		this(new Rectangle2D.Float());
	}
	
	/**
	 * Creates a new NestSprite instance with a bounds rect positioned at (0,0),
	 * of a specified width and height.
	 * 
	 * @param	width		Width of the bounds rect.
	 * @param	height		Height of the bounds rect.
	 */
	public NestSprite (float width, float height) {
		this(new Rectangle2D.Float(0, 0, width, height));
	}
	
	/**
	 * Creates a new NestSprite instance with a bounds rect with its upper-left corner
	 * at a specified location, with a specified width and height.
	 * 
	 * @param	boundsLeft	Left edge of the bounds rect.
	 * @param	boundsTop	Top edge of the bounds rect.
	 * @param	width		Width of the bounds rect.
	 * @param	height		Height of the bounds rect.
	 */
	public NestSprite (float boundsLeft, float boundsTop, float width, float height) {
		this(new Rectangle2D.Float(boundsLeft, boundsTop, width, height));
	}
	
	/**
	 * Creates a new NestSprite instance with a specified bounds rect.
	 * 
	 * @param	bounds		The specified bounds rect.
	 */
	public NestSprite (Rectangle2D.Float bounds) {
		setBounds(bounds);
		init();
	}
	//-----</CONSTRUCTORS>-------------------------------------------//
	
	
	
	//-----<DISPLAY LIST>--------------------------------------------//
	/**
	 * Adds a NestSprite as a child to this NestSprite instance.
	 * The child is added to the front (top) of all other children of this NestSprite instance.
	 * @param	sprite	The NestSprite instance to add as a child.
	 * @example			AddChild
	 */
	public void addChild (NestSprite sprite) {
		addChild(Integer.MAX_VALUE, sprite);
	}
	
	/**
	 * Adds a NestSprite as a child to this NestSprite instance.
	 * The child is added at the index specified.
	 * @param	index	The index in the child list at which the child is added.
	 * @param	sprite	The NestSprite instance to add as a child.
	 * @example			AddChild
	 */
	public void addChild (int index, NestSprite sprite) {
		if (sprite == this) {
			// NestSprite cannot be a child of itself.
			return;
		}
		
		if (sprite.parent != null && sprite.parent != this) {
			// if sprite already belongs to another child list, remove it from that parent
			sprite.parent.removeChild(sprite);
		}
		
		if (!childList.contains(sprite)) {
			childList.add(Math.min(index, childList.size()), sprite);
		}
		
		sprite.onAdded(this);
	}
	
	/**
	 * Removes the specified NestSprite from the child list of this NestSprite instance.
	 * @param	sprite	The NestSprite instance to remove.
	 * @return			The removed NestSprite instance.
	 */
	public NestSprite removeChild (NestSprite sprite) {
		childList.remove(sprite);
		sprite.onRemoved();
		return sprite;
	}
	
	/**
	 * Removes the NestSprite at the specified index from the child list of this NestSprite instance.
	 * @param	index	The index in the child list of this NestSprite instance of the child to remove.
	 * @return			The removed NestSprite instance.
	 */
	public NestSprite removeChild (int index) {
		if (index >= childList.size()) {
			throw new IndexOutOfBoundsException("Index "+ index +" is out of range "+ childList.size() +" for NestSprite child list.");
		}
		
		return childList.remove(index);
	}
	
	/**
	 * Returns the NestSprite at the specified index in the child list of this NestSprite instance.
	 * @param	index	The index in the child list of this NestSprite instance of the child to return.
	 */
	public NestSprite getChildAt (int index) {
		if (index >= childList.size()) {
			throw new IndexOutOfBoundsException("Index "+ index +" is out of range "+ childList.size() +" for NestSprite child list.");
		}
		
		return childList.get(index);
	}
	
	/**
	 * Return the index of the specified NestSprite in the child list of this NestSprite instance. 
	 * @param		sprite	The NestSprite instance whose index will be returned.
	 * @return		index	The index of the specified NestSprite instance.
	 */
	public int getChildIndex (NestSprite sprite) {
		if (!childList.contains(sprite)) {
			return -1;
		}
		return childList.indexOf(sprite);
	}
	
	/**
	 * Move a child of this NestSprite instance to a specified index in the child list.
	 * @param	index	The index to which to move the specified NestSprite instance.
	 * @param 	sprite	The NestSprite child to move.
	 * @throws	IndexOutOfBoundsException	Thrown if the specified index position does not exist in the child list.
	 * @throws	IllegalArgumentException	Thrown if the specified NestSprite is not a child of this NestSprite instance.
	 */
	public void setChildIndex (int index, NestSprite sprite) throws IndexOutOfBoundsException, IllegalArgumentException {
		if (!childList.contains(sprite)) {
			throw new IllegalArgumentException("Passed NestSprite must be a child of this NestSprite.");
		}
		if (index >= childList.size()) {
			throw new IndexOutOfBoundsException("Index "+ index +" is out of range "+ childList.size() +" for NestSprite child list.");
		}
		
		childList.remove(sprite);
		childList.add(index, sprite);
	}
	
	/**
	 * Swap the index positions of two child NestSprites, given the two children.
	 * @param	sprite1		One child to swap.
	 * @param	sprite2		The other child to swap.
	 * @throws	IllegalArgumentException	Thrown if either specified NestSprite is not a child of this NestSprite instance.
	 */
	public void swapChildren (NestSprite sprite1, NestSprite sprite2) throws IllegalArgumentException {
		if (!childList.contains(sprite1)) {
			throw new IllegalArgumentException("@sprite1 must be a child of this NestSprite.");
		}
		if (!childList.contains(sprite2)) {
			throw new IllegalArgumentException("@sprite2 must be a child of this NestSprite.");
		}
		
		Collections.swap(childList, childList.indexOf(sprite1), childList.indexOf(sprite2));
	}
	
	/**
	 * Swap the index positions of two child NestSprites, given the indices of the two children in the child list.
	 * @param	index1		The index of one child to swap.
	 * @param	index2		The index of the other child to swap.
	 * @throws	IllegalArgumentException	Thrown if either specified index position does not exist in the child list.
	 */
	public void swapChildren (int index1, int index2) throws IndexOutOfBoundsException {
		if (index1 >= childList.size() || index1 < 0) {
			throw new IndexOutOfBoundsException("Index "+ index1 +" is out of range "+ childList.size() +" for NestSprite child list.");
		}
		if (index2 >= childList.size() || index2 < 0) {
			throw new IndexOutOfBoundsException("Index "+ index2 +" is out of range "+ childList.size() +" for NestSprite child list.");
		}
		
		Collections.swap(childList, index1, index2);
	}
	
	/**
	 * Returns the number of children in the child list of this NestSprite instance.
	 */
	public int numChildren () {
		return childList.size();
	}
	//-----</DISPLAY LIST>-------------------------------------------//
	
	
	
	//-----<ACCESSORS>-----------------------------------------------//
	/**
	 * <p>
	 * The bounds rect of the graphic content of this NestSprite instance,
	 * in the coordinate system of this NestSprite.
	 * </p><p>
	 * NestSprite calculates its {@link #bounds()} using
	 * <tt>width</tt>, <tt>height</tt>, <tt>boundsLeft</tt>, and <tt>boundsRight</tt>.
	 * Setting any of these properties will change the bounds rect
	 * of this NestSprite instance, and setting the bounds rect
	 * via {@link #setBounds(java.awt.geom.Rectangle2D.Float)} will, in turn, update those properties.
	 * </p><p>
	 * In order for this NestSprite instance to handle mouse events that require the cursor
	 * be over its graphic content, <tt>width</tt> and <tt>height</tt> must both be set manually.
	 * Additionally, if the upper-left corner of the graphic content is not at (0,0),
	 * {@link #boundsLeft} and {@link #boundsTop} must be set to indicate this;
	 * <tt>width</tt> and <tt>height</tt> should be measured from the upper-left corner
	 * of the graphic content.
	 * </p><p>
	 * Perhaps, someday, Nest will be smart enough to update its bounds rect
	 * automatically as its graphic content changes, but for now, it's up to the
	 * application developer to manually update the bounds rect.
	 * </p>
	 * 
	 * @see #width
	 * @see #height
	 * @see #boundsLeft
	 * @see #boundsTop
	 * @see #bounds()
	 * @see #setBounds(java.awt.geom.Rectangle2D.Float)
	 */
	public Rectangle2D.Float bounds () {
		bounds.x = boundsLeft;
		bounds.y = boundsTop;
		bounds.width = width;
		bounds.height = height;
		return bounds;
	}
	
	/**
	 * <p>
	 * Manually update the bounds rect of the graphic content of this NestSprite instance.
	 * </p><p>
	 * See {@link #setBounds(Rectangle2D.Float)} for more details.
	 * </p>
 	 * 
	 * @param x			Bounds left edge.
	 * @param y			Bounds top edge.
	 * @param width		Bounds width.
	 * @param height	Bounds height.
	 */
	public void setBounds (float x, float y, float width, float height) {
		this.boundsLeft = x;
		this.boundsTop = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * <p>
	 * Manually update the bounds rect of the graphic content of this NestSprite instance.
	 * </p><p>
	 * In order for this NestSprite instance to handle mouse events that require the cursor
	 * be over its graphic content, its bounds rect must be set manually.
	 * This can be done by setting <tt>width</tt>, <tt>height</tt>, <tt>boundsLeft</tt>, and <tt>boundsTop</tt>,
	 * or using the <tt>setBounds()</tt> method.
	 * </p><p>
	 * Perhaps, someday, Nest will be smart enough to update its bounds rect
	 * automatically as its graphic content changes, but for now, it's up to the
	 * application developer to manually update the bounds rect.
	 * </p>
	 * 
	 * @see #width
	 * @see #height
	 * @see #boundsLeft
	 * @see #boundsTop
	 * @see #bounds()
	 * @see #setBounds(float, float, float, float)
	 * 
 	 * @example		MouseEvents_Bounds
 	 * 
	 * @param bounds	Rectangle to use as bounds.
	 */
	public void setBounds (Rectangle2D.Float bounds) {
		this.boundsLeft = bounds.x;
		this.boundsTop = bounds.y;
		this.width = bounds.width;
		this.height = bounds.height;
	}
	
	/**
	 * Returns the PApplet instance used in this application.
	 */
	public PApplet pApplet () {
		if (p == null) {
			System.err.println("NestSprite.pApplet() will return null if the NestSprite instance is not on the display list.  Try calling this method once the NestSprite instance is added to the display list.");
		}
		return p;
	}
	
	/**
	 * The NestSprite that contains this NestSprite instance.
	 * The parent is the NestSprite immediately above (behind) this NestSprite instance in the display list.
	 */
	public NestSprite parent () {
		return parent;
	}
	
	/**
	 * Returns true if <tt>dispose()</tt> has been called,
	 * and this NestSprite instance is prepared for garbage collection.
	 */
	public boolean isDisposed () {
		return isDisposed;
	}
	
	/**
	 * Returns the x coordinate of the mouse relative to the coordinate system of this NestSprite instance.
	 */
	public float mouseX () {
		return mouseX;
	}
	
	/**
	 * Returns the y coordinate of the mouse relative to the coordinate system of this NestSprite instance.
	 */
	public float mouseY () {
		return mouseY;
	}
	
	/**
	 * Returns the x and y coordinates of the mouse relative to the coordinate system of this NestSprite instance.
	 */
	public Point2D.Float mousePt () {
		return new Point2D.Float(mouseX, mouseY);
	}
	
	/**
	 * Returns the x coordinate of this NestSprite instance relative to the coordinate system of the application.
	 */
	public float screenX () {
		return screenX;
	}
	
	/**
	 * Returns the y coordinate of this NestSprite instance relative to the coordinate system of the application.
	 */
	public float screenY () {
		return screenY;
	}
	
	/**
	 * Returns the x and y coordinates of this NestSprite instance relative to the coordinate system of the application.
	 */
	public Point2D.Float screenPt () {
		return new Point2D.Float(screenX, screenY);
	}
	
	/**
	 * When <tt>setUpdate()</tt> is called without parameters,
	 * it removes the update functionality set by any previous setUpdate() call.
	 */
	public void setUpdate () {
		setUpdate(null, null);
	}
	
	/**
	 * Specify behavior to be performed during this instance's update() phase.
	 * <tt>methodName</tt> is the name of a public method
	 * that is a member of the <tt>caller</tt> object.
	 * This method must receive two arguments:
	 * a PApplet and a NestSprite instance.
	 * 
	 * e.g.:
	 * <pre>
	 * public void lazyUpdate (PApplet p, NestSprite sprite) {
	 *     // update() implementation here
	 * }
	 * // ...
	 * NestSprite lazySprite = new NestSprite();
	 * lazySprite.setUpdate(this, "lazyUpdate");
	 * </pre>
	 * 
	 * Pass <tt>null</tt> for <tt>methodName</tt> to remove the custom update() behavior.
	 * 
	 * <b>Note:</b> Scope within the specified method will be the
	 * <tt>caller</tt> object; i.e. within the specified method,
	 * the <tt>this</tt> keyword will refer to the <tt>caller</tt> object.
	 * To access this NestSprite instance, use the NestSprite parameter
	 * passed into the specified method.
	 * 
	 * @param	caller		The object whose member method will be assigned as
	 * 						the new update() behavior for this NestSprite instance.
	 * @param	methodName	The name of the method to use for the new update() behavior.
	 */
	public void setUpdate (Object caller, String methodName) {
		if (methodName == null) {
			updateOverride = null;
			return;
		}
		
		Class<?> C = caller.getClass();
		try {
			updateOverride = C.getMethod(methodName, new Class[] { PApplet.class, NestSprite.class });
			updateOverrideTarget = caller;
		} catch (NoSuchMethodException e) {
			System.err.println("No public method "+ methodName +"() found in class "+ C.getName() +".  This NestSprite instance will retain its default update() method.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * When <tt>setDraw()</tt> is called without parameters,
	 * it removes the draw functionality set by any previous setDraw() call.
	 */
	public void setDraw () {
		setDraw(null, null);
	}
	
	/**
	 * Specify behavior to be performed during this instance's draw() phase.
	 * <tt>methodName</tt> is the name of a public method
	 * that is a member of the <tt>caller</tt> object.
	 * This method must receive two arguments:
	 * a PApplet and a NestSprite instance.
	 * 
	 * e.g.:
	 * <pre>
	 * public void lazyDraw (PApplet p, NestSprite sprite) {
	 *     // draw() implementation here
	 * }
	 * // ...
	 * NestSprite lazySprite = new NestSprite();
	 * lazySprite.setDraw(this, "lazyDraw");
	 * </pre>
	 * 
	 * Pass <tt>null</tt> for <tt>methodName</tt> to remove the custom draw() behavior.
	 * 
	 * <b>Note:</b> Scope within the specified method will be the
	 * <tt>caller</tt> object; i.e. within the specified method,
	 * the <tt>this</tt> keyword will refer to the <tt>caller</tt> object.
	 * To access this NestSprite instance, use the NestSprite parameter
	 * passed into the specified method.
	 * 
	 * @param	caller		The object whose member method will be assigned as
	 * 						the new draw() behavior for this NestSprite instance.
	 * @param	methodName	The name of the method to use for the new draw() behavior.
	 */
	public void setDraw (Object caller, String methodName) {
		if (methodName == null) {
			drawOverride = null;
			return;
		}
		
		Class<?> C = caller.getClass();
		try {
			drawOverride = C.getMethod(methodName, new Class[] { PApplet.class, NestSprite.class });
			drawOverrideTarget = caller;
		} catch (NoSuchMethodException e) {
			System.err.println("No public method "+ methodName +"() found in class "+ C.getName() +".  This NestSprite instance will retain its default draw() method.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * PGraphics renderer used to manipulate and draw this NestSprite instance.
	 * Will return <tt>null</tt> until this NestSprite is added to the display list.
	 * Unless explicitly set, will use the parent NestSprite's value;
	 * by default, this is the PGraphics property of the main PApplet.
	 * 
	 * Note: If using a PGraphics buffer, NestSprite subclasses and update()/draw()
	 * 		 implementations should use methods of PGraphics rather than PApplet.
	 * 		 e.g. Instead of p.line(), call g.line().
	 * 
	 * TODO: changing during an update()/render() (e.g. within an update() or draw() body)
	 * 		 will break things horribly. postpone change until next cycle.
	 * TODO: example of rendering into a buffer, printing to PDF.
	 */
	public PGraphics renderer () {
		return g;
	}
	
	/**
	 * Set PGraphics renderer used to manipulate and draw this NestSprite instance.
	 * Equivalent to {@link #setRenderer(PGraphics, boolean)} with <tt>true</tt> for <tt>setChildren</tt>. 
	 * @param	renderer	PGraphics renderer.
	 */
	public void setRenderer (PGraphics renderer) {
		setRenderer(renderer, true);
	}
	
	/**
	 * Set PGraphics renderer used to manipulate and draw this NestSprite instance.
	 * @param	renderer		PGraphics renderer.
	 * @param	setChildren		If true, the PGraphics renderer for all descendants will also be set. 
	 */
	public void setRenderer (PGraphics renderer, boolean setChildren) {
		g = renderer;
		
		try {
			g.updatePixels();
			rendererSupportsUpdatePixels = true;
		} catch (RuntimeException re) {
			rendererSupportsUpdatePixels = false;
		}
		
		setChildrenRenderer(renderer, rendererSupportsUpdatePixels);
	}
	//-----</ACCESSORS>----------------------------------------------//
	
	
	
	//-----<MISC PUBLIC METHODS>-------------------------------------//
	/**
	 * Frees up this NestSprite for garbage collection.
	 * Does not call <tt>dispose()</tt> on children.
	 * NOTE: not yet tested, nor verified in a profiler.
	 */
	public void dispose () {
		dispose(false);
	}
	
	/**
	 * Frees up this NestSprite for garbage collection.
	 * NOTE: not yet tested, nor verified in a profiler.
	 * 
	 * @param	disposeChildren		Pass <tt>true</tt> to also dispose of all children. 
	 */
	public void dispose (boolean disposeChildren) {
		if (disposeChildren) {
			if (childList != null) {
				int i = childList.size();
				while (i-- > 0) {
					childList.get(i).dispose(true);
				}
			}
		}
		
		if (childList != null) {
			childList.clear();
			childList = null;
		}
		if (parent != null) {
			parent.removeChild(this);
		}
		
		parent = null;
		p = null;
		g = null;
		deleteObservers();
		
		updateOverride = null;
		updateOverrideTarget = null;
		drawOverride = null;
		drawOverrideTarget = null;
		
		isDisposed = true;
		
		super.dispose();
	}
	
	/**
	 * Transform a point from the local coordinate space of this NestSprite instance
	 * to the coordinate space of the top-level NestSpriteContainer.
	 * 
	 * @param	localPt		A point, in the local coordinate space, to transform.
	 */
	public Point2D.Float localToGlobal (Point2D localPt) {
		Point2D.Float globalPt = new Point2D.Float();
		
		NestSprite ns = this;
		transform.setToIdentity();
		ArrayList<NestSprite> ancestors = new ArrayList<NestSprite>();
		while (ns != null) {
			ancestors.add(ns);
			ns = ns.parent;
		}
		
		ListIterator<NestSprite> i = ancestors.listIterator(ancestors.size());
		while (i.hasPrevious()) {
			ns = i.previous();
			transform.translate(ns.x, ns.y);
			transform.scale(ns.scaleX, ns.scaleY);
			transform.rotate(ns.rotationZ);
		}
		transform.transform(localPt, globalPt);
		
		return globalPt;
	}
	
	/**
	 * Transform a point from the coordinate space of the top-level NestSpriteContainer
	 * to the local coordinate space of this NestSprite instance.
	 * 
	 * @param	globalPt		A point, in the coordinate space of the
	 * 						top-level NestSpriteContainer, to transform.
	 */
	public Point2D.Float globalToLocal (Point2D globalPt) {
		Point2D.Float localPt = new Point2D.Float();
		
		NestSprite ns = this;
		transform.setToIdentity();
		while (ns != null) {
			transform.rotate(-ns.rotationZ);
			transform.scale(1/ns.scaleX, 1/ns.scaleY);
			transform.translate(-ns.x, -ns.y);
			ns = ns.parent;
		}
		transform.transform(globalPt, localPt);
		
		return localPt;
	}
	
	/**
	 * Check if a point is within the bounds rect of this NestSprite instance
	 * or the bounds rect of any of its children.
	 * 
	 * @param	pt	Point, in the coordinate system of this NestSprite,
	 * 				to check against bounds of this NestSprite instance
	 * 				and all descendants of this NestSprite instance.
	 * @return	Returns <tt>true</tt> if the Point is within the bounds rect
	 * 			of this NestSprite instance or any of its descendants.
	 */
	public boolean hitTest (Point2D pt) {
		return hitTest(pt, true);
	}
	
	/**
	 * Check if a point is within the bounds rect of this NestSprite instance
	 * or the bounds rect of any of its children.
	 * 
	 * @param	pt				Point, in the coordinate system of this NestSprite,
	 * 							to check against bounds of this NestSprite instance.
	 * @param	testChildren	If true, the hit test will be performed against
	 * 							all descendants of this NestSprite instance.
	 * @return	Returns <tt>true</tt> if the Point is within the bounds rect
	 * 			of this NestSprite instance or, if <tt>testChildren</tt> is <tt>true</tt>,
	 * 			the bounds rect of any of its descendants.
	 */
	public boolean hitTest (Point2D pt, boolean testChildren) {
		if (bounds().contains(pt)) {
			return true;
		}
		
		if (testChildren) {
			NestSprite child;
			Point2D.Float childPt = (Point2D.Float)(pt.clone());
			for (int i=0; i<childList.size(); i++) {
				child = childList.get(i);
				transform.setToIdentity();
				transform.rotate(-child.rotationZ);
				transform.scale(1/child.scaleX, 1/child.scaleY);
				transform.translate(-child.x, -child.y);
				transform.transform(pt, childPt);
				
				if (child.hitTest(childPt)) {
					return true;
				}
			}
		}
		
		return false;
	}
	//-----</MISC PUBLIC METHODS>------------------------------------//
	
	
	
	//-----<MOUSE EVENTS>--------------------------------------------//	
	/**
	 * Dispatched when the mouse is pressed and released within the bounds rect of this NestSprite instance,
	 * with no mouse movement in between.
	 * @param e		A MouseEvent instance that describes the event.
	 */
	protected void mouseClicked (MouseEvent e) {
		// optionally implemented by subclasses
	}
	
	/**
	 * Dispatched when the mouse travels into the bounds rect of this NestSprite instance.
	 * NOTE: Not yet implemented (correctly).
	 * @param e		A MouseEvent instance that describes the event.
	 */
	protected void mouseEntered (MouseEvent e) {
		// optionally implemented by subclasses
	}
	
	/**
	 * Dispatched when the mouse travels out of the bounds rect of this NestSprite instance.
	 * NOTE: Not yet implemented (correctly).
	 * @param e		A MouseEvent instance that describes the event.
	 */
	protected void mouseExited (MouseEvent e) {
		// optionally implemented by subclasses
	}
	
	/**
	 * Dispatched when the mouse is pressed within the bounds rect of this NestSprite instance.
	 * @param e		A MouseEvent instance that describes the event.
	 */
	protected void mousePressed (MouseEvent e) {
		// optionally implemented by subclasses
	}
	
	/**
	 * Dispatched when the mouse is released within the bounds rect of this NestSprite instance.
	 * @param e		A MouseEvent instance that describes the event.
	 */
	protected void mouseReleased (MouseEvent e) {
		// optionally implemented by subclasses
	}
	
	/**
	 * Dispatched when the mouse is pressed,
	 * whether within the bounds rect of this NestSprite instance or not.
	 * @param e		A MouseEvent instance that describes the event.
	 */
	protected void mouseDown (MouseEvent e) {
		// optionally implemented by subclasses
	}
	
	/**
	 * Dispatched when the mouse is released,
	 * whether within the bounds rect of this NestSprite instance or not.
	 * @param e		A MouseEvent instance that describes the event.
	 */
	protected void mouseUp (MouseEvent e) {
		// optionally implemented by subclasses
	}
	
	/**
	 * Dispatched when the mouse button is pressed and the mouse is moved,
	 * whether outside of or within the bounds rect of this NestSprite instance.
	 * @param e		A MouseEvent instance that describes the event.
	 */
	protected void mouseDragged (MouseEvent e) {
		// optionally implemented by subclasses
	}
	
	/**
	 * Dispatched when the mouse is moved,
	 * whether outside of or within the bounds rect of this NestSprite instance.
	 * @param e		A MouseEvent instance that describes the event.
	 */
	protected void mouseMoved (MouseEvent e) {
		// optionally implemented by subclasses
	}
	//-----</MOUSE EVENTS>-------------------------------------------//
	
	
	
	//-----<KEY EVENTS>----------------------------------------------//	
	/**
	 * Dispatched when a key is pressed.
	 * @param e		A KeyEvent instance that describes the event.
	 */
	protected void keyPressed (KeyEvent e) {
		// optionally implemented by subclasses
	}
	
	/**
	 * Dispatched when a key is released.
	 * @param e		A KeyEvent instance that describes the event.
	 */
	protected void keyReleased (KeyEvent e) {
		// optionally implemented by subclasses
	}
	
	/**
	 * Dispatched when a key is pressed and released.
	 * @param e		A KeyEvent instance that describes the event.
	 */
	protected void keyTyped (KeyEvent e) {
		// optionally implemented by subclasses
	}
	//-----</KEY EVENTS>---------------------------------------------//	
	
	
	
	//-----<MISC PROTECTED METHODS>----------------------------------//	
	@Override
	public void dispatchEvent (Event evt) {
		if (!evt.bubbles()) {
			super.dispatchEvent(evt);
			return;
		}
		
		super.setEventPhase(evt, Event.PHASE.BUBBLING);
		super.dispatchEvent(evt);
		if (parent != null && !evt.isPropagationStopped()) {
			parent.dispatchEvent(evt);
		}
	}
	
	/**
	 * Shortcut for <tt>System.out.println(Object)</tt>.
	 * @param o
	 */
	protected void println (Object o) {
		PApplet.println(o);
	}
	
	/**
	 * Perform any pre-rendering updates,
	 * such as frame-based animation.
	 * 
	 * @param	p	The PApplet application instance.
	 * @example		CustomSprites
	 */
	protected void update (PApplet p) {
		// optionally implemented by subclasses
	}
	
	/**
	 * All drawing to screen should go in this method of subclasses.
	 * draw() is called after applying all translation and rotation.
	 * 
	 * @param	p	The PApplet application instance.
	 * @example		CustomSprites
	 */
	protected void draw (PApplet p) {
		// optionally implemented by subclasses
	}
	//-----</MISC PROTECTED METHODS>---------------------------------//
	
	
	
	//-----<PACKAGE-PRIVATE DISPLAY LIST>----------------------------//
	void onAdded (NestSprite _parent) {
		parent = _parent;
		
		// traverse up the display list up to NestSpriteContainer.
		// if this NestSprite is not on-screen (if a NestSpriteContainer is not an ancestor),
		// do not call onAddedToStage until it is.
		if (parent == null) { return; }
		
		try {
			updateTransforms();
		} catch (NoninvertibleTransformException e) {
			System.out.println("NestSprite.onAdded Exception:");
			e.printStackTrace();
		}
		
		p = parent.p;
		g = parent.g;
		this.dispatchEvent(new Event(Event.ADDED));
		
		NestSprite parentSprite = parent;
		while (parentSprite.parent != null) {
			parentSprite = parentSprite.parent;
		}
		if (!(parentSprite instanceof NestSpriteContainer)) {
			return;
		}
		
		// traverse down the display list,
		// ensuring that all descendants have called onAddedToStage.
		// for event dispatching, children are iterated from
		// the front of the display list to the back.
		NestSprite child;
		int i=childList.size();
		while (i-- > 0) {
			child = childList.get(i);
			child.onAdded(this);
		}
		
		onAddedToStage();
	}
	
	void onAddedToStage () {
		try {
			rendererIs3D = p.g.is3D();
			rendererIsGL = p.g.isGL();
		} catch (Exception e) {
			System.out.println("is3D() or isGL() not supported in this version of Processing. Processing 2.0+ is required for Nest.");
		}
		
		this.dispatchEvent(new Event(Event.ADDED_TO_STAGE));
	}
	
	void onRemoved () {
		parent = null;
		p = null;
		this.dispatchEvent(new Event(Event.REMOVED));
	}
	//-----</PACKAGE-PRIVATE DISPLAY LIST>---------------------------//
	
	
	
	//-----<PACKAGE-PRIVATE FRAME UPDATES>---------------------------//
	/**
	 * update() self, then prerender() all descendants.
	 * @param	bSuppressUpdateCalls		Passing true will suppress (possibly overridden) calls to update(),
	 * 										and will only perform geometric transforms on all descendants.
	 * 										Used for internal display list traversal, e.g. event propagation.
	 * @throws	NoninvertibleTransformException 
	 */
	void prerender (boolean bSuppressUpdateCalls) throws NoninvertibleTransformException {
		if (!bSuppressUpdateCalls) {
			if (updateOverride != null) {
				try {
					updateOverride.invoke(updateOverrideTarget, new Object[] { p, this });
				} catch (Exception e) {
					System.err.println("Error invoking update() override:");
					e.printStackTrace();
				}
			} else {
				update(p);
			}
		}
		
		// dispose() called from within update(), so bail
		if (isDisposed) { return; }
		
		updateTransforms();
		
		// for rendering, children are iterated from back of the display list to the front.
		// TODO: is there a fail-fast way to iterate through childList so i don't have to rewrite this iteration logic multiple times?
		NestSprite child;
		for (int i=0; i<childList.size(); i++) {
			child = childList.get(i);
			child.prerender(bSuppressUpdateCalls);
			if (i < childList.size() && childList.get(i) != child) {
				// child moved to different index or removed from childList entirely;
				// update iterator index to ensure no elements are skipped.
				i--;
			}
		}
	}
	
	/**
	 * Translate and rotate,
	 * draw() self, then repeat process recursively through all children.
	 * 
	 * @param	p	The PApplet application instance.
	 */
	void render (PApplet p) {
		// dispose() called between update() and draw(), so bail
		if (isDisposed) { return; }
		
		// don't draw() if visible == false
		if (!visible) { return; }
		
		// local reference to graphics context, in case dispose()
		// is called during draw(), sending this.g out of scope.
		PGraphics _g = g;
		
		if (_g != p.g) {
			_g.beginDraw();
		}
		
		// apply changes for this leaf, and all descendants:
		// matrix transforms, depth hints, blend modes
		if (inFront && rendererIsGL) {
			_g.hint(PGraphics.ENABLE_DEPTH_TEST);
		}
		
		// if a blendMode is specified, set blendMode only if
		// different than blendMode at this point in the display list.
		// if not specified, currentBlendMode will be retained (no blendMode change).
		if (blendMode != NO_BLEND_MODE && blendMode != NestSprite.currentBlendMode) {
			previousBlendMode = NestSprite.currentBlendMode;
			_g.blendMode(blendMode);
			NestSprite.currentBlendMode = blendMode;
		}
		
		_g.pushMatrix();
			
			if (rendererIs3D) {
				_g.translate(x, y, z);
				
				_g.scale(scaleX, scaleY, scaleZ);
				
				// TODO: better to calculate rotation around x/y/z vector?
				_g.rotateX(rotationX);
				_g.rotateY(rotationY);
				_g.rotateZ(rotationZ);
			} else {
				_g.translate(x, y);
				
				_g.scale(scaleX, scaleY);
				
				_g.rotate(rotationZ);
			}
			
			// draw self below children
			if (drawOverride != null) {
				try {
					drawOverride.invoke(drawOverrideTarget, new Object[] { p, this });
				} catch (Exception e) {
					System.err.println("Error invoking draw() override:");
					e.printStackTrace();
				}
			} else {
				draw(p);
			}
			
			// if dispose() called from within draw(), don't attempt to draw children
			if (!isDisposed) {
				// draw children above self
				renderChildren();
			}
		
		// revert changes applied to this leaf, and all descendants:
		// matrix transforms, depth hints, blend modes
		_g.popMatrix();
		
		// reset blendMode if it was changed
		if (blendMode != NO_BLEND_MODE && previousBlendMode != NestSprite.currentBlendMode) {
			_g.blendMode((previousBlendMode == NO_BLEND_MODE) ? PROCESSING_DEFAULT_BLEND_MODE : previousBlendMode);
			NestSprite.currentBlendMode = previousBlendMode;
			previousBlendMode = NO_BLEND_MODE;
		}
		
		if (inFront && rendererIsGL) {
			_g.hint(PGraphics.DISABLE_DEPTH_TEST);
		}
		
		if (!isDisposed) {
			if (_g != p.g) {
				_g.endDraw();
				if (rendererSupportsUpdatePixels) {
					p.image(_g, 0, 0);
				}
			}
		}
	}
	//-----</PACKAGE-PRIVATE FRAME UPDATES>--------------------------//
	
	
	
	/**
	 * Utility method to set renderer on descendants, with pre-calculated supportsUpdatePixels.
	 */
	void setChildrenRenderer (PGraphics renderer, boolean supportsUpdatePixels) {
		g = renderer;
		rendererSupportsUpdatePixels = supportsUpdatePixels;
		for (NestSprite child : childList) {
			child.setChildrenRenderer(renderer, supportsUpdatePixels);
		}
	}
	
	/**
	 * Send events through the display list,
	 * starting at NestSpriteContainer and progressing
	 * down through the display list (capture phase),
	 * to the front-most NestSprite.
	 * 
	 * @return	Returns true if event is a bounded MouseEvent and
	 * 			occurred within the bounds of this NestSprite instance.
	 */
	boolean processDisplayListEvent (Event evt) {
		if (handleCaptureEvents) {
			// while traversing down through the display list,
			// the event is in the CAPTURE phase.
			handleDisplayListEvent(evt, hitTest(mousePt));
		}
		
		if (evt.isPropagationStopped()) {
			return false;
		}
		
		boolean inChildBounds = false;
		
		if (visible && inputChildren) {
			// propagate down through children (continue CAPTURE phase)
			// for event dispatching, children are iterated from
			// the front of the display list to the back.
			NestSprite child;
			int i=childList.size();
			while (i-- > 0) {
				super.setEventPhase(evt, Event.PHASE.CAPTURE);
				
				child = childList.get(i);
				if (child.processDisplayListEvent(evt)) {
					inChildBounds = true;
				}
				
				if (evt.isPropagationStopped()) {
					return false;
				}
			}
		}
		
		if (visible && inputEnabled) {
			// at this point, all descendants have processed the event.
			if (inChildBounds) {
				// already handled by child; handle as BUBBLING event
				super.setEventPhase(evt, Event.PHASE.BUBBLING);
			} else {
				// TODO: redundant to check bounds here and then immediately again within handleDisplayListEvent().
				//		 may want to overload handleDisplayListEvent() to accept bounds-already-checked?
				if (bounds.contains(mousePt)) {
					// at the bottom of display list; handle as AT_TARGET event
					super.setEventPhase(evt, Event.PHASE.AT_TARGET);
				}
			}
			return handleDisplayListEvent(evt, inChildBounds);
		} else {
			if (inChildBounds) {
				// allow child events to bubble up past this instance
				return bounds().contains(mousePt);
			} else {
				// if not a bubbling child event, and inputEnabled == false,
				// do not allow event to bubble up (treat as if never captured).
				return false;
			}
		}
	}
	
	/**
	 * <p>
	 * Implementation of PApplet MouseEvent and KeyEvent registration.
	 * Handles MouseEvents and KeyEvents sent from NestSpriteContainer
	 * down through the display list, and back up again.
	 * </p><p>
	 * To handle MouseEvents, NestSprite subclasses can override the MouseEvent handler methods:
	 * <ul>
	 * 	<li><tt>mouseClicked</tt></li>
	 * 	<li><tt>mouseEntered</tt></li>
	 * 	<li><tt>mouseExited</tt></li>
	 * 	<li><tt>mousePressed</tt></li>
	 * 	<li><tt>mouseReleased</tt></li>
	 * 	<li><tt>mouseDown</tt></li>
	 * 	<li><tt>mouseUp</tt></li>
	 * 	<li><tt>mouseDragged</tt></li>
	 * 	<li><tt>mouseMoved</tt></li>
	 * </ul>
	 * </p><p>
	 * To handle KeyEvents, NestSprite subclasses can override the KeyEvent handler methods:
	 * <ul>
	 * 	<li><tt>keyPressed</tt></li>
	 * 	<li><tt>keyReleased</tt></li>
	 * 	<li><tt>keyTyped</tt></li>
	 * </ul>
	 * </p>
	 */
	protected final boolean handleDisplayListEvent (Event evt, boolean inChildBounds) {
		super.handleDisplayListEvent(evt, inChildBounds);
		
		boolean inBounds = inChildBounds ? true : bounds().contains(mousePt);
		
		if (evt instanceof MouseEvent) {
		
			switch (evt.type()) {
				case MouseEvent.MOUSE_CLICKED :
					if (inBounds) {
						mouseClicked((MouseEvent)evt);
					}
					break;
				case MouseEvent.MOUSE_ENTERED :
					if (inBounds) {
						mouseEntered((MouseEvent)evt);
					}
					break;
				case MouseEvent.MOUSE_EXITED :
					if (!inBounds) {
						mouseExited((MouseEvent)evt);
					}
					break;
				case MouseEvent.MOUSE_PRESSED :
					mouseDown((MouseEvent)evt);
					if (inBounds) {
						mousePressed((MouseEvent)evt);
					}
					break;
				case MouseEvent.MOUSE_RELEASED :
					mouseUp((MouseEvent)evt);
					if (inBounds) {
						mouseReleased((MouseEvent)evt);
					}
					break;
				case MouseEvent.MOUSE_DRAGGED :
					mouseDragged((MouseEvent)evt);
					break;
				case MouseEvent.MOUSE_MOVED :
					mouseMoved((MouseEvent)evt);
					break;
			}
		} else if (evt instanceof KeyEvent) {
			switch (evt.type()) {
				case KeyEvent.KEY_PRESSED :
					keyPressed((KeyEvent)evt);
					break;
				case KeyEvent.KEY_RELEASED :
					keyReleased((KeyEvent)evt);
					break;
				case KeyEvent.KEY_TYPED :
					keyTyped((KeyEvent)evt);
					break;
			}
		}
		
		return inBounds;
	}
	
	/**
	 * Trigger a MouseEvent as if it were generated by an actual mouse event.
	 * Dispatches a MouseEvent into the top of display list, from where it
	 * will proceed through bubble, target, and capture phases as usual.
	 * 
	 * @param	localX		X-coordinate of the event, in local coordinates.
	 * @param	localY		Y-coordinate of the event, in local coordinates.
	 * @param	eventType	Event type from MouseEvent, e.g. MouseEvent.MOUSE_CLICKED.
	 * 						<b>NOTE:</b> For now, only MouseEvent.MOUSE_CLICKED is supported.
	 * 						Passing other values for @eventType will fail with an error message.
	 */
	protected void triggerMouseEvent (float localX, float localY, int eventType) {
		if (eventType != MouseEvent.MOUSE_CLICKED) {
			System.err.println("Event types other than MouseEvent.MOUSE_CLICKED not currently supported by NestSprite.triggerMouseEvent.");
			return;
		}
		
		// NestSpriteContainer expects a MouseEvent with absolute coordinates
		Point2D.Float evtLoc_global = localToGlobal(new Point2D.Float((int)(0.5f*width), (int)(0.5f*height)));
		
		java.awt.event.MouseEvent awtMouseEvent = new java.awt.event.MouseEvent(
				pApplet(), java.awt.event.MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), java.awt.event.InputEvent.BUTTON1_DOWN_MASK,
				(int)evtLoc_global.x, (int)evtLoc_global.y, 1, false, java.awt.event.MouseEvent.BUTTON1);
		
		processing.event.MouseEvent pMouseEvent = new processing.event.MouseEvent(
				awtMouseEvent, awtMouseEvent.getWhen(), processing.event.MouseEvent.CLICK, PApplet.LEFT,
				awtMouseEvent.getX(), awtMouseEvent.getY(), PApplet.LEFT, 1);
		
		// send event into display list from the top
		NestSprite parentSprite = parent;
		while (parentSprite.parent != null) {
			parentSprite = parentSprite.parent;
		}
		if (!(parentSprite instanceof NestSpriteContainer)) {
			System.out.println("NestSprite.triggerMouseEvent() works only if the NestSprite instance is on the display list (via addChild()).");
			return;
		}
		
		// KLUDGE: temporarily set mousePt to location of triggered event,
		// to satisfy bounds checks in processDisplayListEvent().
		// maybe better to design Events and subclasses to have a location property,
		// default=null, that is checked in processDisplayListEvent() instead of mousePt.
		float currMouseX = mousePt.x;
		float currMouseY = mousePt.y;
		mousePt.x = localX;
		mousePt.y = localY;
		((NestSpriteContainer)parentSprite).mouseEvent(pMouseEvent);
		mousePt.x = currMouseX;
		mousePt.y = currMouseY;
	}
	
	private void init () {
		childList = new ArrayList<NestSprite>();
		
		transform = new AffineTransform();
		localPt = new Point2D.Float();
		screenPt = new Point2D.Float();
		mousePt = new Point2D.Float();
	}
	
	private void updateTransforms () throws NoninvertibleTransformException {
		if (parent == null) { return; }
		
		// TODO: account for rotation in all three axes; start with just rotZ.
		
		localPt.x = x;
		localPt.y = y;
		screenPt.x = screenPt.y = 0;
		mousePt.x = parent.mouseX;
		mousePt.y = parent.mouseY;
		
		transform.setToIdentity();
		transform.translate(parent.x, parent.y);
		transform.scale(parent.scaleX, parent.scaleY);
		transform.rotate(parent.rotationZ);
		transform.transform(localPt, screenPt);
		
		transform.setToIdentity();
		transform.translate(x, y);
		transform.scale(scaleX, scaleY);
		transform.rotate(rotationZ);
		transform.inverseTransform(new Point2D.Float(parent.mouseX, parent.mouseY), mousePt);
		
		screenX = screenPt.x;
		screenY = screenPt.y;
		mouseX = mousePt.x;
		mouseY = mousePt.y;
	}
	
	private void renderChildren () {
		// for rendering, children are iterated from back of the display list to the front.
		// TODO: is there a fail-fast way to iterate through childList so i don't have to rewrite this iteration logic multiple times?
		NestSprite child;
		for (int i=0; i<childList.size(); i++) {
			child = childList.get(i);
			child.render(p);
			if (i < childList.size() && childList.get(i) != child) {
				// child moved to different index or removed from childList entirely;
				// update iterator index to ensure no elements are skipped.
				i--;
			}
		}
	}
}