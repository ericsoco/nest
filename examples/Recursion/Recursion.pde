/*
 * Recursion
 *
 * The display list offers a little bit of old school net art magic:
 * easy recursion.
 * 
 * Simply set up a loop that adds a child to a child to a child
 * (and so on), and tell each one to rotate a set amount each frame.
 * Et voila.
 */

import com.transmote.nest.*;
import com.transmote.nest.events.*;

NestSpriteContainer spriteContainer;
int numSprites = 30;

void setup () {
  size(400, 400);
  setupSprites();
}

void draw () {
  background(0);
  spriteContainer.update();
}

void setupSprites () {
  // create a NestSpriteContainer to hold all other NestSprites,
  // and position it in the center of the screen.
  spriteContainer = new NestSpriteContainer(this);
  spriteContainer.x = 0.5 * width;
  spriteContainer.y = 0.5 * height;
  
  // recursively create and add a new sprite to the
  // previously-created sprite.
  RectSprite sprite = createRectSprite(spriteContainer);
  for (int i=0; i<numSprites; i++) {
    sprite = createRectSprite(sprite);
  }
}

RectSprite createRectSprite (NestSprite parentSprite) {
  // create a new RectSprite and add it
  // to the parentSprite's display list.
  RectSprite sprite = new RectSprite();
  parentSprite.addChild(sprite);
  
  // make each new child sprite slightly smaller than its parent...
  sprite.scaleX = sprite.parent().scaleX * 0.995;
  sprite.scaleY = sprite.parent().scaleY * 0.995;
  
  // ...and offset x and y slightly.
  sprite.x = 15;
  sprite.y = 5;
  
  return sprite;
}
