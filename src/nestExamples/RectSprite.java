package nestExamples;

import processing.core.PApplet;

import com.transmote.nest.NestSprite;
import com.transmote.nest.events.MouseEvent;

public class RectSprite extends NestSprite {
	private static int idCtr = 0;
	private int id;
	private float rotPerFrame = 0f;
	private int initBlendMode = NestSprite.NO_BLEND_MODE;
	
	public RectSprite () {
		this(0.005f);
	}
	
	public RectSprite (float rotPerFrame) {
		super();
		this.rotPerFrame = rotPerFrame;
		this.id = idCtr++;
	}
	
	public void initBlendMode (int val) {
		initBlendMode = val;
		blendMode = val;
	}

	@Override
	protected void update (PApplet p) {
		rotationZ += (float)(id+1) * rotPerFrame;
//		setBounds(-0.5f*width, -0.5f*height, width, height);
	}
	
	@Override
	protected void draw (PApplet p) {
		p.stroke(255);
		p.fill(255, 128);
		
		p.rect(boundsLeft, boundsTop, width, height);
	}
	
	@Override
	protected void mousePressed (MouseEvent evt) {
		blendMode = NestSprite.NO_BLEND_MODE;
		evt.stopPropagation();
	}
	
	@Override
	protected void mouseReleased (MouseEvent evt) {
		blendMode = initBlendMode;
		evt.stopPropagation();
	}
}