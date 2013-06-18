package nestExamples;

import processing.core.PApplet;

import com.transmote.nest.NestSprite;

public class NonLazySprite extends NestSprite {
	@Override
	protected void update (PApplet p) {
		x += 1;
		y += 1;
		rotationZ += 0.01f;
	}
	
	protected void draw (PApplet p) {
		p.stroke(0xFFFFFFFF);
		p.fill(0xFFFFCC66);
		p.rect(-50, -50, 100, 100);
	}
}