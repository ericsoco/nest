/*
 * MouseEvents_Bounds
 * 
 * NestSprite provides a simple API for handling mouse events.
 * See MouseEvents_Simple for an introduction.
 * 
 * Many mouse events are only handled if they are within the bounds
 * of the NestSprite instance.  Unlike ActionScript 3.0,
 * Nest requires that developers manually
 * update the bounds of NestSprite instances using bounds(),
 * or boundsLeft, boundsTop, width, and height.
 * 
 * If a NestSprite instance has not set its bounds via these
 * properties or methods, it will not respond properly to
 * mouse events that respond to the NestSprite's bounds.
 * 
 * If a future version of Nest updates a
 * NestSprite's bounds rect automatically as it changes, I will be happy.
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
  
  // attach a ButtonSprite instance to the SpriteContainer
  ButtonSprite spriteLeft = new ButtonSprite();
  spriteLeft.x = 200;
  spriteLeft.y = 200;
  spriteContainer.addChild(spriteLeft);
}
