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

import com.transmote.nest.events.KeyEvent;
import com.transmote.nest.events.MouseEvent;

import processing.core.PApplet;

/**
 * <p>
 * A NestSpriteContainer sits at the top of the Nest display list.
 * All NestSprite instances visible on-screen are descendants of a NestSpriteContainer.
 * This means that traversal up the display list from a NestSprite instance's parent,
 * to its parent, and so on, will ultimately reach the NestSpriteContainer.
 * </p><p>
 * {@link #updateDisplayList()} should be called every frame by application code; <tt>updateDisplayList()</tt>
 * then travels down the display list, from NestSprite to NestSprite, calling {@link NestSprite#update(PApplet)}
 * on each, and then {@link NestSprite#draw(PApplet)} on each.
 * </p><p>
 * NestSpriteContainer acts as the conduit for input events (such as MouseEvents and KeyEvents)
 * between PApplet and application code (NestSprite instances).  Input events are dispatched
 * by PApplet and handled by NestSpriteContainer, then sent down through the display list.
 * See {@link com.transmote.nest.events.Event} for more detail on the event flow.
 * </p><p>
 * If the developer wishes to eliminate the display list at runtime,
 * call {@link #dispose()}.
 * </p>
 * 
 * @author		Eric Socolofsky
 * @example		DisplayList
 */
public class NestSpriteContainer extends NestSprite {
	private boolean updateDeprecatedWarned = false;
	
	/**
	 * The NestSpriteContainer to be the top of the display list.
	 * @param 	p	A reference to the PApplet running this Processing sketch.
	 */
	public NestSpriteContainer (PApplet p) {
		this.p = p;
		this.g = p.g;
		init();
	}
	
	/**
	 * Updates the Nest display list:
	 * Calls update() on self and all NestSprite descendants,
	 * then calls draw() on self and all NestSprite descendants.
	 */
	/*
	 * Note: NestSpriteContainer could instead register with PApplet for pre and draw (via registerPre/Draw),
	 * and call NestSprite.prerender and NestSprite.render from those handlers,
	 * but this design gives developers freedom to update the display list as desired.
	 */
	public void updateDisplayList () {
		updateDisplayList(false);
	}
	
	@Deprecated
	public void update () {
		if (!updateDeprecatedWarned) {
			System.err.println("NestSpriteContainer.update() is deprecated. Use NestSpriteContainer.updateDisplayList() instead.");
			updateDeprecatedWarned = true;
		}
		updateDisplayList();
	}
	
	private void updateDisplayList (boolean bSuppressRender) {
		screenX = 0;
		screenY = 0;
		mouseX = p.mouseX;
		mouseY = p.mouseY;
		
		try {
			prerender(bSuppressRender);
			if (!bSuppressRender) { render(p); }
		} catch (Exception e) {
			System.out.println("NestSpriteContainer.updateDisplayList Exception:");
			e.printStackTrace();
		}
	}
	
	/**
	 * Frees up this NestSpriteContainer, and all child NestSprites, for garbage collection.
	 * NOTE: not yet tested, nor verified in a profiler.
	 */
	public void dispose () {
		super.dispose(true);
	}
	
	/**
	 * @exclude
	 * Implementation of PApplet MouseEvent registration.
	 * Developers should not call this method directly.
	 */
	public void mouseEvent (processing.event.MouseEvent pEvent) {
		// Processing updates mouseX/Y within its handlers for MOUSE_MOVED and MOUSE_DRAGGED events;
		// this happens after the rest of the draw() phase.  Since NestSpriteContainer.updateDisplayList() is generally
		// called from within the draw() phase, MOUSE_MOVED/DRAGGED events will result in the
		// display list having outdated mouseX/Y values.  To fix this, MOUSE_MOVED/DRAGGED events
		// trigger a transform update on the whole display list.
		
		// TODO: perhaps better to update display list transforms *only* on MOVED/DRAGGED events?
		//		 but this might cause problems in other ways...
		if (pEvent.getAction() == MouseEvent.MOUSE_MOVED || pEvent.getAction() == MouseEvent.MOUSE_DRAGGED) {
			updateDisplayList(true);
		}
		
		processDisplayListEvent(new MouseEvent(pEvent));
	}
	
	/**
	 * @exclude
	 * Implementation of PApplet KeyEvent registration.
	 * Developers should not call this method directly.
	 */
	public void keyEvent (processing.event.KeyEvent pEvent) {
		processDisplayListEvent(new KeyEvent(pEvent));
	}
	
	private void init () {
		p.registerMethod("dispose", this);
		p.registerMethod("mouseEvent", this);
		p.registerMethod("keyEvent", this);
	}
}
