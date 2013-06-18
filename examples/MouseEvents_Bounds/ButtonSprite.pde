// RectSprite subclasses NestSprite,
// and specifies how it is transformed every frame
// and how it is drawn to the screen.
// update() and then draw() will be
// called every frame by NestSpriteContainer.update().

class ButtonSprite extends NestSprite {
  
  public ButtonSprite () {
    // set the bounds rect for this instance manually.
    // MouseEvents that respect the bounds of a NestSprite
    // use these values to describe the bounds rect.
    width = 100;
    height = 100;
    boundsLeft = -50;
    boundsTop = -50;
    
    // note, the above could instead be expressed in one line:
    // super(-50, -50, 100, 100);
  }

  // draw() is called second by NestSpriteContainer.update(),
  // and should be used to draw the NestSprite to the screen.
  void draw (PApplet p) {
    // draw the bounds of the ButtonSprite instance as a red rectangle 
    p.stroke(255, 0, 0);
    p.noFill();
    p.rect(boundsLeft, boundsTop, width, height);
    
    //drawGraphicsAlignedWithBounds(p);
    drawGraphicsNotAlignedWithBounds(p);
  }
  
  // to align graphics with bounds, draw using
  // boundsLeft and boundsTop as the left and top edges
  // of the drawn graphics.
  void drawGraphicsAlignedWithBounds (PApplet p) {
    // draw an ellipse from the (0,0) point
    // (e.g. the centerpoint of this ButtonSprite instance)
    p.stroke(255);
    p.fill(0x99FFFFFF);
    p.ellipseMode(CORNER);
    p.ellipse(boundsLeft, boundsTop, width, height);
  }
  
  // just using width and height, without using
  // boundsLeft and boundsTop, can result in misalignment
  // between the drawn graphics and the NestSprite bounds.
  void drawGraphicsNotAlignedWithBounds (PApplet p) {
    // draw an ellipse from the (0,0) point
    // (e.g. the centerpoint of this ButtonSprite instance)
    p.stroke(255);
    p.fill(0x99FFFFFF);
    p.ellipseMode(CORNER);
    p.ellipse(0, 0, width, height);
  }
  
  // print to the console on a mouse release (the mouse button is
  // released while the cursor is within the bounds of this NestSprite).
  // 
  // note that clicking within the red bounds rect,
  // as specified in the constructor, generates the clicked event;
  // clicking on the graphic content (the ellipse) only does so
  // if the area clicked is also within the red rect.
  // 
  // to respond to mouse clicks, a NestSprite instance
  // must manually set its bounds rect.
  // 
  // note that in the Processing IDE, a fully-qualified path name
  // must be used to avoid namespace collision with java.awt.KeyEvent.
  void mouseReleased (com.transmote.nest.events.MouseEvent e) {
    println("released!");
  }
}
