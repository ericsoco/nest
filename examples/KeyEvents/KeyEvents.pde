/*
 * KeyEvents
 * 
 * NestSprite provides a simple API for handling key events.
 * Subclasses can override the following NestSprite methods
 * to handle key events:
 * - keyPressed()
 * - keyReleased()
 * - keyTyped()
 * 
 * A NestSprite instance will only receive key events if it is
 * currently on the display list, so be sure to addChild() it
 * to NestSpriteContainer or to an other NestSprite that is already
 * on the display list before attempting to handle key events.
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
  spriteContainer.update();
}

void setupSprites () {
  // create a NestSpriteContainer to hold all other NestSprites
  spriteContainer = new NestSpriteContainer(this);
  
  // attach ButtonSprite instances to the SpriteContainer
  MoverSprite mover = new MoverSprite();
  mover.x = 150;
  mover.y = 150;
  spriteContainer.addChild(mover);
}
