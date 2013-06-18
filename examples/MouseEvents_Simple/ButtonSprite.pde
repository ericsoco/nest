// ButtonSprite subclasses NestSprite,
// and specifies how it is transformed every frame
// and how it is drawn to the screen.
// update() and then draw() will be
// called every frame by NestSpriteContainer.update().
// 
// ButtonSprite responds to mouse clicks by dispatching
// the mouse event to any registered observers.

class ButtonSprite extends NestSprite {
  
  public ButtonSprite () {
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
  
  // handle a click (mouse down, then mouse up,
  // with no mouse movement in between) by dispatching
  // the received MouseEvent to any registered observers.
  // 
  // note that in the Processing IDE, a fully-qualified path name
  // must be used to avoid namespace collision with java.awt.KeyEvent.
  void mouseClicked (com.transmote.nest.events.MouseEvent e) {
    println("mouseClicked");
    dispatchEvent(e);
  }
  
  // handle an event dispatched from another object.
  // note that addObserver() must be called to establish
  // the handler-dispatcher relationship; else,
  // this event will not be called.
  //
  // this example rotates every time it receives a
  // mouse click event from another object.
  void handleEvent (Event e) {
    // first, check if the event Object is a MouseEvent.
    if (e instanceof com.transmote.nest.events.MouseEvent) {
      // if it is, switch() on the event id...
      int type = e.type();
      switch (type) {
        case com.transmote.nest.events.MouseEvent.MOUSE_CLICKED:
          // handle a mouse click event.
          rotationZ += 0.1;
          break;
      }
    }
  }
}
