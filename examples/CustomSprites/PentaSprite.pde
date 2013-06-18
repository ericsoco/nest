// PentaSprite subclasses NestSprite,
// and specifies how it is transformed every frame
// and how it is drawn to the screen.
// update() and then draw() will be
// called every frame by NestSpriteContainer.update().

class PentaSprite extends NestSprite {
  
  public PentaSprite () {}

  // update() is called first by NestSpriteContainer.update(),
  // and should be used to set the transformations
  // (position, scale, rotation) of the NestSprite before drawing it.
  void update (PApplet p) {
    // rotate continuously.
    rotationZ += 0.02;
  }

  // draw() is called next by NestSpriteContainer.update(),
  // and should be used to draw the NestSprite to the screen.
  void draw (PApplet p) {
    p.stroke(255);
    p.fill(0x99FFFFFF);
    p.beginShape();
    for (int i=0; i<5; i++) {
      p.vertex(50*cos(i*TWO_PI/5), 50*sin(i*TWO_PI/5));
    }
    p.endShape(CLOSE);
  }
}
