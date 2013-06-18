/*
 * DisplayList
 * 
 * Nest provides a simple display list architecture
 * modeled on the ActionScript 3.0 display list.
 * 
 * A NestSpriteContainer lives at the top of a display list.
 * Adding NestSprite children to it will subject them to the
 * same transformations (translation, rotation, scale)
 * applied to their parents.
 * 
 * Press the up and down arrow keys and the backspace key
 * to add and remove NestSprite children.
 */

import com.transmote.nest.*;
import com.transmote.nest.events.*;

NestSpriteContainer spriteContainer;

void setup () {
  size(400, 400);
  
  // create a NestSpriteContainer to hold all other NestSprites,
  // and position it at the vertical center of the screen.
  spriteContainer = new NestSpriteContainer(this);
  spriteContainer.y = 0.5 * height;
}

void draw () {
  background(0);
  
  // calling NestSpriteContainer.update() will automatically
  // loop through all its descendant NestSprite instances,
  // call update() on each, and then call draw() on each.
  spriteContainer.update();
}

void keyPressed () {
  switch (keyCode) {
    case 38:
      // UP pressed
      addSprite();
      break;
    case 40:
      // DOWN pressed
      removeSprite();
      break;
    case 8:
      // delete pressed
      removeAllSprites();
      break;
  }
}

void addSprite () {
  // add a new SineSprite to the front of the display list.
  SineSprite sprite = new SineSprite();
  sprite.x = 10 + spriteContainer.numChildren() * 10;
  spriteContainer.addChild(sprite);
}

void removeSprite () {
  // remove the front-most SineSprite from the display list.
  if (spriteContainer.numChildren() > 0) {
    spriteContainer.removeChild(spriteContainer.numChildren()-1);
  }
}

void removeAllSprites () {
  // remove all SineSprites from the display list.
  while (spriteContainer.numChildren() > 0) {
    spriteContainer.removeChild(spriteContainer.numChildren()-1);
  }
}
