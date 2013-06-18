package nestExamples;

import processing.core.PApplet;

import com.transmote.nest.*;
import com.transmote.nest.events.*;


public class TestSprite extends NestSprite {
	public int color = 0x99FFFFFF;
	public float speed = 0.01f;
	
	public int id;
	
	// properties used for testing different functionality
	public boolean bParentSprite = false;
	public boolean bRemoveOnClick = false;
	public TestSprite otherSprite;
	private boolean bTestingEventPropagation = false;
	
	public TestSprite (int id) {
		this.id = id;
	}
	/*
	public void testEventHandling (TestSprite otherSprite) {
		otherSprite.addObserver(this);
	}
	
	public void testEventDispatching () {
		dispatchEvent(new TestEvent(TestEvent.TEST_EVENT_TYPE));
	}
	*/
	public void testEventPropagation () {
		bTestingEventPropagation = true;
	}
	
	
	@Override
	protected void handleEvent (Event evt) {
		System.out.print("handling... ");
		if (evt instanceof TestEvent) {
			System.out.print("TestEvent[");
			switch (evt.type()) {
				case TestEvent.TEST_EVENT_TYPE :
					System.out.print("TEST_EVENT_TYPE");
					break;
			}
		} else {
			System.out.print("Event[");
			switch (evt.type()) {
				case Event.DISPATCHER_DISPOSE :
					System.out.print("DISPATCHER_DISPOSE");
					break;
				case Event.ADDED :
					System.out.print("ADDED");
					break;
				case Event.ADDED_TO_STAGE :
					System.out.print("ADDED_TO_STAGE");
					break;
				case Event.ERROR :
					System.out.print("ERROR");
					break;
				case Event.INIT :
					System.out.print("INIT");
					break;
			}
		}
		
		println("]");
	}
	
	@Override
	protected void mouseClicked (MouseEvent e) {
		if (bParentSprite && e.phase() == Event.PHASE.CAPTURE) {
			removeChild(this.getChildAt(2));
		}
		if (bRemoveOnClick) {
//			parent().removeChild(otherSprite);
			parent().removeChild(this);
		}
	}
	
	@Override
	protected void mousePressed (MouseEvent e) {
		// testing Event.stopPropagation
		if (bTestingEventPropagation) {
//			e.stopPropagation();
		}
		
		// testing event phase and targets
		println("["+id+"] MOUSE PRESSED; phase:"+ e.phase());
		if (e.phase() != Event.PHASE.CAPTURE) {
			println("target:"+((TestSprite)(e.target())).id+"; currentTarget:"+((TestSprite)(e.currentTarget())).id);
		}
		
		// testing NestSprite.localToGlobal()/globalToLocal.
//		println("l2g:"+localToGlobal(mousePt())+"; local:"+new Point2D.Float(pApplet().mouseX, pApplet().mouseY));
//		println("g2l:"+globalToLocal(new Point2D.Float(pApplet().mouseX, pApplet().mouseY))+"; local:"+mousePt());
	}
	/*
	@Override
	protected void mouseDown (MouseEvent e) {
		if (id != 1) { return; }
		println("hitTest on mousePt:"+hitTest(mousePt()));
	}
	*/
	
	/*
	@Override
	protected void mouseMoved (MouseEvent e) {
		println("["+id+"] MOUSE MOVED; phase:"+ e.phase());
	}
	*/
	/*
	@Override
	protected void keyPressed (KeyEvent e) {
		println("["+id+"] KEY PRESSED: "+ e.keyCode());
	}
	*/
	@Override
	protected void update (PApplet p) {
//		rotationZ += speed;
		setBounds(-0.5f*width, -0.5f*height, width, height);
//		println("mouse: "+this.mousePt());
	}
	
	@Override
	protected void draw (PApplet p) {
		p.stroke(255);
		p.fill(color);
		
		p.rect(boundsLeft, boundsTop, width, height);
	}
}