package nestExamples;

import java.awt.geom.Point2D;

import com.transmote.nest.NestSprite;
import com.transmote.nest.NestSpriteContainer;
import com.transmote.nest.ui.NestButton;

import processing.core.PApplet;
import processing.core.PFont;

@SuppressWarnings("unused")
public class NestExamples extends PApplet {
	private static final long serialVersionUID = 5234264381753823054L;
	
	private NestSpriteContainer spriteContainer;
	private RectSprite rs1 = null;
	private RectSprite rs2 = null;
	private RectSprite rs3 = null;
	private RectSprite rs4 = null;
	private RectSprite rs5 = null;
	private RectSprite rs6 = null;
	
	private TestSprite parentSprite = null;
	private TestSprite sprite1 = null;
	private TestSprite sprite2 = null;
	private TestSprite sprite3 = null;
	private TestSprite sprite4 = null;
	private TestSprite sprite5 = null;
	
	
	public void setup() {
		size(800, 800);
		frameRate(60);
		
		testUI();
//		testBlendModes();
//		setupCoordConversion();
//		testLazySprites();
//		testIterationOrder();
//		testStopPropagation();
//		setupSprites();
//		testAddAt();
	}

	public void draw() {
//		println("-----");
		background(0);
		spriteContainer.updateDisplayList();
		
		stroke(0x9900FFFF);
		line(0, 0, 200, 200);
		
//		testCoordConversion();
	}
	
	public void mousePressed () {
//		sprite1.dispatchEvent(new TestEvent(TestEvent.TEST_EVENT_TYPE));
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { nestExamples.NestExamples.class.getName() });
	}
	
	private void testUI () {
		spriteContainer = new NestSpriteContainer(this);
		
		UITestSprite s1 = new UITestSprite();
		s1.x = 200;
		s1.y = 200;
		spriteContainer.addChild(s1);
		
		PFont font = createFont("Georgia", 24);
		NestButton button = new NestButton(150, 35, "nest button", font, null);
		button.x = 200;
		button.y = 100;
		button.cornerRadius = 10;
		spriteContainer.addChild(button);
		button.addObserver(s1);
	}
	
	private void testBlendModes () {
		spriteContainer = new NestSpriteContainer(this);
		
		rs1 = new RectSprite(0f);
		rs1.setBounds(-50, -50, 100, 100);
		rs1.x = 200;
		rs1.y = 200;
		rs1.initBlendMode(PApplet.ADD);
		
		rs2 = new RectSprite(0f);
		rs2.setBounds(-50, -50, 100, 100);
		rs2.x = 50;
		rs2.y = 100;
		
		rs3 = new RectSprite(0f);
		rs3.setBounds(-50, -50, 100, 100);
		rs3.x = 50;
		rs3.y = 100;
		
		rs4 = new RectSprite(0f);
		rs4.setBounds(-50, -50, 100, 100);
		rs4.x = 50;
		rs4.y = 100;
		
		spriteContainer.addChild(rs1);
		rs1.addChild(rs2);
//		rs2.addChild(rs3);
//		rs3.addChild(rs4);
	}
	
	private void setupCoordConversion () {
		spriteContainer = new NestSpriteContainer(this);
		
		rs1 = new RectSprite(0);
		rs1.setBounds(-100, -100, 200, 200);
		rs1.x = 400;
		rs1.y = 400;
		
		rs2 = new RectSprite(1);
		rs2.setBounds(-50, -50, 100, 100);
		rs2.x = 50;
		rs2.y = 100;
		
		rs3 = new RectSprite(2);
		rs3.setBounds(-50, -50, 100, 100);
		rs3.x = -50;
		rs3.y = -100;
		
		spriteContainer.addChild(rs1);
		rs1.addChild(rs2);
		rs2.addChild(rs3);
	}
	
	private void testCoordConversion () {
		Point2D.Float ptA = new Point2D.Float(0, 0);
		ptA = rs1.localToGlobal(ptA);
		rect(ptA.x-5, ptA.y-5, 10, 10);
		
		Point2D.Float ptB = new Point2D.Float(0, 0);
		ptB = rs2.localToGlobal(ptB);
		rect(ptB.x-5, ptB.y-5, 10, 10);
		
		Point2D.Float ptC = new Point2D.Float(50, 0);
		ptC = rs3.localToGlobal(ptC);
		rect(ptC.x-5, ptC.y-5, 10, 10);
	}
	
	private void testLazySprites () {
		spriteContainer = new NestSpriteContainer(this);
		
		/*
		NestSprite lazySprite = new NestSprite();
		lazySprite.setUpdate(this, "lazyUpdate");
		lazySprite.setDraw(this, "lazyDraw");
		spriteContainer.addChild(lazySprite);
		*/
		
		
		for (int i=0; i<500; i++) {
			NestSprite sprite = new NestSprite();
			sprite.setUpdate(this, "lazyUpdate");
			sprite.setDraw(this, "lazyDraw");
			sprite.x = (i%100)*5;
			sprite.y = 100 * (float)Math.floor(i/100);
			spriteContainer.addChild(sprite);
		}
		
		/*
		for (int i=0; i<500; i++) {
			NonLazySprite sprite = new NonLazySprite();
			sprite.x = (i%100)*5;
			sprite.y = 100 * (float)Math.floor(i/100);
			spriteContainer.addChild(sprite);
		}
		*/
	}
	
	public void lazyUpdate (PApplet p, NestSprite sprite) {
		sprite.x += 1;
		sprite.y += 1;
		sprite.rotationZ += 0.01f;
	}
	
	public void lazyDraw (PApplet p, NestSprite sprite) {
		p.stroke(0xFFFFFFFF);
//		p.fill(0x99FFFFFF);
		p.fill(0xFFFFCC66);
		p.rect(-50, -50, 100, 100);
	}
	
	private void testIterationOrder () {
		spriteContainer = new NestSpriteContainer(this);
		parentSprite = new TestSprite(-1);
//		parentSprite.handleCaptureEvents = true;
//		parentSprite.bParentSprite = true;
		spriteContainer.addChild(parentSprite);
		TestSprite[] sprites = {sprite1, sprite2, sprite3, sprite4, sprite5};
		
		for (int i=sprites.length-1; i>=0; i--) {
			sprites[i] = new TestSprite(i);
			sprites[i].setBounds(0, 0, 50, 50);
			sprites[i].x = 50 + 60*i;
			sprites[i].y = 50 + 60*i;
			parentSprite.addChild(sprites[i]);
			sprites[i].bRemoveOnClick = true;
			if (i < sprites.length-1) {
				sprites[i].otherSprite = sprites[i+1];
			}
		}
	}
	
	private void testStopPropagation () {
		sprite1 = new TestSprite(1);
		sprite1.setBounds(-100, -100, 200, 200);
		sprite1.x = 150;
		sprite1.y = 150;
		
		sprite2 = new TestSprite(2);
		sprite2.setBounds(-100, -100, 200, 200);
		sprite2.x = 50;
		sprite2.y = 100;
		
		sprite3 = new TestSprite(3);
		sprite3.setBounds(-50, -50, 100, 100);
		sprite3.x = 150;
		sprite3.y = 200;
		
		spriteContainer = new NestSpriteContainer(this);
		spriteContainer.addChild(sprite1);
		sprite1.addChild(sprite2);
		sprite1.addChild(sprite3);
	}
	
	private void setupSprites () {
		sprite1 = new TestSprite(1);
		sprite1.setBounds(-100, -100, 200, 200);
		sprite1.x = 0.5f * width;
		sprite1.y = 0.5f * height;
		sprite1.rotationZ = -0.5f;
		
		sprite2 = new TestSprite(2);
		sprite2.setBounds(-50, -50, 100, 100);
		sprite2.x = 75;//50;
		sprite2.y = 75;//50;
//		sprite2.rotationZ = 0.5f;
		sprite2.addObserver(sprite1);
//		sprite2.inputChildren = false;
		sprite2.testEventPropagation();
//		sprite2.handleCaptureEvents = true;
		
		
		sprite3 = new TestSprite(3);
		sprite3.setBounds(-50, -50, 100, 100);
		sprite3.x = 50;
		sprite3.y = 50;
		
		spriteContainer = new NestSpriteContainer(this);
		spriteContainer.addChild(sprite1);
		sprite1.addChild(sprite2);
		sprite2.addChild(sprite3);
	}
	
	private void testAddAt () {
		sprite3 = new TestSprite(3);
		sprite3.setBounds(-150, -150, 300, 300);
		sprite3.speed = -0.005f;
		sprite3.color = 0xFFFFCC33;
		sprite3.x = sprite1.x;
		sprite3.y = sprite1.y;
		spriteContainer.addChild(0, sprite3);
	}
	
	/*
	public void keyPressed () {
		if (sprite2 == null || sprite3 == null) { return; }
		
		if (key == 's') {
			// reparenting
			if (sprite2.parent() == sprite1) {
				sprite3.addChild(sprite2);
			} else {
				sprite1.addChild(sprite2);
			}
		}
	}
	*/
}
