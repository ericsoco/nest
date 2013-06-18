// TriangleSprite subclasses NestSprite,
// and specifies how it is transformed every frame
// and how it is drawn to the screen.
// update() and then draw() will be
// called every frame by NestSpriteContainer.update().

class TriangleSprite extends NestSprite {
  
  public TriangleSprite () {}

  // update() is called first by NestSpriteContainer.update(),
  // and should be used to set the transformations
  // (position, scale, rotation) of the NestSprite before drawing it.
  void update (PApplet p) {
    // rotate continuously.
    rotationZ -= 0.05;
  }

  // draw() is called next by NestSpriteContainer.update(),
  // and should be used to draw the NestSprite to the screen.
  void draw (PApplet p) {
    p.stroke(255);
    p.fill(0x99FFFFFF);
    p.triangle(0, -55, 80/sqrt(2), 25, -80/sqrt(2), 25);
  }
}
