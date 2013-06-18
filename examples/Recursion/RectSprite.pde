// RectSprite subclasses NestSprite,
// and specifies how it is transformed every frame
// and how it is drawn to the screen.
// update() and then draw() will be
// called every frame by NestSpriteContainer.update().

class RectSprite extends NestSprite {
  
  public RectSprite () {
    // automatically set width and height.
    super(100, 10);
  }

  // update() is called first by NestSpriteContainer.update(),
  // and should be used to set the transformations
  // (position, scale, rotation) of the NestSprite before drawing it.
  void update (PApplet p) {
    // rotate continuously.
    rotationZ += 0.01;
  }
  
  // draw() is called second by NestSpriteContainer.update(),
  // and should be used to draw the NestSprite to the screen.
  void draw (PApplet p) {
    // draw a rectangle centered on this RectSprite's centerpoint (0,0).
    p.stroke(255);
    p.fill(0x99FFFFFF);
    p.rect(-0.5*width, -0.5*height, width, height);
  }
}
