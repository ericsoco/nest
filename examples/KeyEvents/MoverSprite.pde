// MoverSprite subclasses NestSprite,
// and specifies how it is transformed every frame
// and how it is drawn to the screen.
// update() and then draw() will be
// called every frame by NestSpriteContainer.update().
//
// MoverSprite handles arrow key presses with keyPressed().

class MoverSprite extends NestSprite {
  
  public MoverSprite () {
    // automatically set width and height.
    super(100, 100);
  }

  // draw() is called second by NestSpriteContainer.update(),
  // and should be used to draw the NestSprite to the screen.
  void draw (PApplet p) {
    // draw a rectangle down and to the right
    // from this RectSprite's centerpoint (0,0).
    p.stroke(255);
    p.fill(0x99FFFFFF);
    p.rect(0, 0, width, height);
  }
  
  // respond to arrow key presses.
  // note that in the Processing IDE, a fully-qualified path name
  // must be used to avoid namespace collision with java.awt.KeyEvent.
  void keyPressed (com.transmote.nest.events.KeyEvent e) {
    switch (e.keyCode()) {
      case LEFT :
        x -= 5;
        break;
      case UP :
        y -= 5;
        break;
      case RIGHT :
        x += 5;
        break;
      case DOWN :
        y += 5;
        break;
    }
  }
}
