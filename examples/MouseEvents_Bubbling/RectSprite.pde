// RectSprite subclasses NestSprite,
// and specifies how it is transformed every frame
// and how it is drawn to the screen.
// update() and then draw() will be
// called every frame by NestSpriteContainer.update().

class RectSprite extends NestSprite {
  int highlightCounter = 0;
  
  public RectSprite () {
    // automatically set width and height.
    super(40, 40);
  }

  // draw() is called second by NestSpriteContainer.update(),
  // and should be used to draw the NestSprite to the screen.
  void draw (PApplet p) {
    if (highlightCounter-- > 0) {
      p.fill(0xCCFFFF99);
    } else {
      p.fill(0x99FFFFFF);
    }
    
    // draw a rectangle down and to the right
    // from this RectSprite's centerpoint (0,0).
    p.stroke(255);
    p.rect(0, 0, width, height);
  }
  
  // mouse events that respect the bounds rect of a NestSprite instance
  // bubble up the display list, and can be handled by any of the
  // ancestors (parent, parent.parent, etc) of the NestSprite instance
  // that captured the event.
  // 
  // note that in the Processing IDE, a fully-qualified path name
  // must be used to avoid namespace collision with java.awt.KeyEvent.
  void mousePressed (com.transmote.nest.events.MouseEvent e) {
    // flash on mouse pressed
    // (mouse down within bounds rect)
    highlightCounter = 60;
    
    // count how many steps up to the top
    int numLevelsDeep = 0;
    NestSprite p = parent();
    while (p != null) {
      numLevelsDeep++;
      p = p.parent();
    }
    println("i am "+ numLevelsDeep +" levels deep.");
  }
}
