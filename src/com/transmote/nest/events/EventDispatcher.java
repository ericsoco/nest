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

package com.transmote.nest.events;

import java.util.Observable;
import java.util.Observer;

/**
 * <p>
 * EventDispatcher instances wrap Java's <tt>Observable</tt> implementation of the
 * Observer interface.  EventDispatcher is modeled on ActionScript's
 * <a href="http://livedocs.adobe.com/flash/9.0/ActionScriptLangRefV3/flash/events/EventDispatcher.html">EventDispatcher</a>
 * class, and offers much of the same functionality.
 * Event instances flow through EventDispatcher instances
 * in what is referred to as 'the event flow'.
 * </p><p>
 * The Nest event flow is modeled on the
 * <a href="http://help.adobe.com/en_US/ActionScript/3.0_ProgrammingAS3/WS5b3ccc516d4fbf351e63e3d118a9b90204-7e4f.html">event flow implemented by Adobe ActionScript 3.0</a>.
 * Input events enter into the event flow through NestSpriteContainer,
 * at the top of the display list.  Each input event then proceeds down through
 * the display list, in the 'capture' phase.  The event ultimately reaches the 'target' phase,
 * at the NestSprite instance farthest down the display list within whose bounds the event was generated.
 * Once the event reaches its target, it moves back up the display list in the 'bubbling' phase.
 * </p><p>
 * how to use: addObserver, dispatchEvent, override handleEvent.
 * </p>
 * 
 * wraps Observable, to allow use via composition instead of inheritance.
 * 
 * @author		Eric Socolofsky
 * @see			Event
 * @example		MouseEvents_Simple
 */
/*
 * @example		MouseEvents_Bubbling
 * @example		MouseEvents_Bounds
 * @example		KeyEvents
 */
public class EventDispatcher extends Observable implements Observer {
	private Object owner;
	
	public EventDispatcher () {
		this(null);
	}
	public EventDispatcher (Object owner) {
		super();
		
		if (owner == null) {
			this.owner = this;
		} else {
			this.owner = owner;
		}
	}
	
	/**
	 * A reference to an object that contains this EventDispatcher instance.
	 * Useful when using EventDispatcher with composition rather than inheritance.
	 * Set this property via the constructor.
	 * If this property is not set, it returns a reference to this EventDispatcher instance.
	 */
	public Object owner () {
		return owner;
	}
	
	/**
	 * Add an Observer to be notified asynchronously of events (which can be defined as either Objects or Events).
	 * @param	o	The Observer instance that will be notified.
	 * 				If the Observer is an EventDispatcher, the Event will be handled by either
	 * 				<tt>handleEvent(Event)</tt> or <tt>handleEvent(Observable, Object)</tt>.
	 * @see		#handleEvent(Event)
	 * @see		#handleEvent(Observable, Object)
	 */
	@Override
	public void addObserver (Observer o) {
		super.addObserver(o);
	}
	
	/**
	 * Remove an Observer from the event notification list of this EventDispatcher instance.
	 * @param	o	The Observer instance that will no longer be notified.
	 */
	@Override
	public void deleteObserver (Observer o) {
		super.deleteObserver(o);
	}
	
	/**
	 * Dispatch a Nest Event to all registered Observers.
	 * Dispatching an event via this method will store a reference to the owner of
	 * this EventDispatcher instance in the event, as Event.target.
	 * 
	 * This is the preferred method for event-based communication
	 * within the NestSprite framework.  However, since this method is
	 * wrapping the Java Observable pattern, a generic Object can be
	 * dispatched instead.
	 * 
	 * @param	evt		The Event to dispatch to all Observers.
	 * 
	 * @see		Event#target()
	 * @see		#owner()
	 * @see		#handleEvent(Event)
	 * @see		#dispatchEvent(Object)
	 */
	public void dispatchEvent (Event evt) {
		evt.target = owner;
		setChanged();
		super.notifyObservers(evt);
	}
	
	/**
	 * Dispatch a generic Object to all registered Observers.
	 * 
	 * The preferred method for event-based communication
	 * within the NestSprite framework is <tt>dispatchEvent(Event)</tt>.
	 * However, since this method is wrapping the Java Observable pattern,
	 * an Object can be dispatched instead.
	 * 
	 * @param	arg		The Object to dispatch to all Observers.
	 * 
	 * @see		#dispatchEvent(Event)
	 * @see		#handleEvent(Observable, Object)
	 */
	public void dispatchEvent (Object arg) {
		setChanged();
		super.notifyObservers(arg);
	}
	
	/**
	 * Clean up all references within this EventDispatcher instance,
	 * to prepare for garbage collection.
	 */
	public void dispose () {
		notifyObservers(new Event(Event.DISPATCHER_DISPOSE));
		deleteObservers();
	}
	
	
	/**
	 * @exclude
	 * Implementation of Observer interface.
	 * Developers should not call this method directly.
	 * To handle Events, subclasses must override handleEvent().
	 */
	public final void update (Observable o, Object arg) {
		if (arg instanceof Event) {
			handleEvent((Event)arg);
		} else {
			handleEvent(o, arg);
		}
	}
	
	/**
	 * Handle an Event dispatched by an Observable observed by this EventDispatcher instance.
	 * This method is used to handle non-input events,
	 * i.e. Event instances that do not traverse the display list. 
	 * 
	 * This is the preferred method for event-based communication
	 * within the NestSprite framework.  However, since this method is
	 * wrapping the Java Observable pattern, an Object can be
	 * dispatched instead, and handled with {@link #handleEvent(Observable, Object)}.
	 * 
	 * Subclasses that wish to handle Nest Events
	 * should implement this method.
	 *
	 * @param	evt		The Event dispatched by the Observable object.
	 * 
	 * @see		#dispatchEvent(Event)
	 * @see		#handleEvent(Observable, Object)
	 * @see		Event
	 */
	protected void handleEvent (Event evt) {
		// optionally implemented by subclasses
	}

	/**
	 * Handle a generic Object dispatched by an Observable observed by this NestSprite.
	 * 
	 * The preferred method for event-based communication
	 * within the NestSprite framework is {@link #handleEvent(Event)}.
	 * However, since this method is wrapping the Java Observable pattern,
	 * an Object can be dispatched instead.
	 * 
	 * Subclasses that wish to handle events as Objects
	 * should implement this method.
	 *
	 * @param   o     The Observable instance that dispatched the event.
	 * @param   arg   The event argument (as a generic Object) dispatched by the observed Observable.
	 * 
	 * @see		#handleEvent(Event)
	 * @see		#dispatchEvent(Object)
	 * 
	 */
	protected void handleEvent (Observable o, Object arg) {
		// optionally implemented by subclasses
	}
	
	/**
	 * EventDispatcher.handleDisplayListEvent() is used internally by the
	 * Nest framework, and need not be called directly by developers.
	 */
	protected boolean handleDisplayListEvent (Event evt, boolean inChildBounds) {
		evt.currentTarget = this;
		return true;
	}
	
	/**
	 * Set Event.phase as the Event instance travels through the display list.
	 * This method is used internally within the Nest framework,
	 * and should not be called directly by developers.
	 * 
	 * @param	evt		The Event instance whose phase to set.
	 * @param	phase	The enumerated phase.
	 */
	protected final void setEventPhase (Event evt, Event.PHASE phase) {
		evt.phase = phase;
		if (phase == Event.PHASE.AT_TARGET) {
			evt.target = this;
		}
	}
}