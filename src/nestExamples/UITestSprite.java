package nestExamples;

import processing.core.PApplet;

import com.transmote.nest.NestSprite;
import com.transmote.nest.events.Event;
import com.transmote.nest.events.MouseEvent;

public class UITestSprite extends NestSprite {
	int fill = 0x99FFFFFF;
	
	public UITestSprite () {
		this.width = 50;
		this.height = 50;
		setBounds(0, 0, width, height);
	}
	
	@Override
	protected void update (PApplet p) {
		//
	}
	
	@Override
	protected void draw (PApplet p) {
		p.stroke(255);
		p.fill(fill);
		p.rect(0, 0, width, height);
	}
	
	@Override
	protected void handleEvent (Event evt) {
		println("evt target:"+evt.target());
		if (evt instanceof MouseEvent) {
			switch (evt.type()) {
				case MouseEvent.MOUSE_CLICKED :
					println("clicked");
					break;
			}
		}
	}
	
	@Override
	protected void mousePressed (MouseEvent evt) {
//		println("press");
	}
	
	@Override
	protected void mouseReleased (MouseEvent evt) {
//		println("release");
	}
	
	@Override
	protected void mouseDragged (MouseEvent evt) {
//		println("drag");
		fill = 0xDDFFFFFF;
	}
	
	@Override
	protected void mouseUp (MouseEvent evt) {
//		println("up");
		fill = 0x66FFFFFF;
	}
}