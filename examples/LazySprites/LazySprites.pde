/*
 * LazySprites
 * 
 * Rather than subclassing NestSprite to define custom
 * update() and draw() functionality, developers can use the
 * setUpdate() and setDraw() methods of NestSprite
 * to add and remove functionality.
 * 
 * Note that it is generally preferable to subclass NestSprite
 * rather than using these methods, as subclassing produces
 * more portable, readable code, and encapsulates complexity.
 * Developers should only use setUpdate() and setDraw()
 * to quickly add simple functionality.
 * 
 * See the SimpleSprites and CustomSprites examples for
 * more information on subclassing NestSprite.
 */

import com.transmote.nest.*;
import com.transmote.nest.events.*;

NestSpriteContainer spriteContainer;

void setup () {
  size(400, 400);
  setupSprites();
}

void draw () {
  background(0);
  
  // calling SpriteContainer.update() will automatically
  // call update() and draw() on all child sprites
  spriteContainer.update();
}

void setupSprites () {
  // set up a container to hold, update, and draw all child sprites
  spriteContainer = new NestSpriteContainer(this);
  
  float size;
  for (int i=0; i<500; i++) {
    // create a NestSprite instance of a random size
    size = random(5, 20);
    NestSprite sprite = new NestSprite(size, size);
    
    // add custom update functionality by passing the name
    // of the method with the desired functionality.
    sprite.setUpdate(this, "lazyUpdate");
    
    // same thing for custom draw functionality.
    sprite.setDraw(this, "lazyDraw");
    
    // start at a location near the screen center.
    sprite.x = 0.5*width + random(-40, 40);
    sprite.y = 0.5*height + random(-40, 40);
    
    // add to the display list.
    spriteContainer.addChild(sprite);
  }
}

/*
A method specified by setUpdate() must have a PApplet and
NestSprite parameter.

The scope of this method (the object referred
to by the 'this' keyword within this method) is the object
containing the method; in this case, the host PApplet.

To refer to the NestSprite instance calling this method,
use the NestSprite parameter passed into the method.
*/
void lazyUpdate (PApplet p, NestSprite sprite) {
  float dCenter = sqrt(sq(sprite.x - 0.5*width) + sq(sprite.y - 0.5*height));
  if (dCenter > 200) {
    // when sprite reaches 200px from screen center,
    // remove custom update (stop updating).
    sprite.setUpdate();
  }
  
  // move sprite away from screen center along
  // a direction determined by the sprite's size,
  // at a speed relative to distance from the screen center.
  float speed = 100 / pow(dCenter, 0.75);
  sprite.x += speed * Math.cos(sprite.width);
  sprite.y += speed * Math.sin(sprite.width);
}

/*
A method specified by setDraw() must have a PApplet and
NestSprite parameter.

The scope of this method (the object referred
to by the 'this' keyword within this method) is the object
containing the method; in this case, the host PApplet.

To refer to the NestSprite instance calling this method,
use the NestSprite parameter passed into the method.
*/
void lazyDraw (PApplet p, NestSprite sprite) {
  noStroke();
  fill(0x99FFFFFF);
  ellipse(0, 0, sprite.width, sprite.height);
}


