// SineSprite subclasses NestSprite,
// and specifies how it is transformed every frame
// and how it is drawn to the screen.
// update() and then draw() will be
// called every frame by NestSpriteContainer.update().

public class SineSprite extends NestSprite {
  float sinCtr = 0;
  
  public SineSprite () {
    // automatically set width and height.
    super(50, 50);
  }
  
  void update (PApplet p) {
    // move up and down continuously.
    sinCtr += 0.05;
    y = 100 * sin(sinCtr);
  }
  
  // draw() is called second by NestSpriteContainer.update(),
  // and should be used to draw the NestSprite to the screen.
  void draw (PApplet p) {
    // draw an ellipse centered on this SineSprite's centerpoint (0,0).
    p.stroke(255);
    p.fill(0x99FFFFFF);
    p.ellipse(0.5*width, 0.5*height, width, height);
  }
}
