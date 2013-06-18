/*
 * AddChild
 * 
 * Nest provides a simple display list architecture
 * modeled on the ActionScript 3.0 display list.
 * 
 * A NestSpriteContainer lives at the top of a display list.
 * Adding NestSprite children to it will subject them to the
 * same transformations (translation, rotation, scale)
 * applied to their parents.
 */

import com.transmote.nest.*;
import com.transmote.nest.events.*;

NestSpriteContainer spriteContainer;
RectSprite sprite1;
RectSprite sprite2;

void setup () {
  size(400, 400);
  setupSprites();
}

void draw () {
  background(0);
  
  // calling NestSpriteContainer.update() will automatically
  // loop through all its descendant NestSprite instances,
  // call update() on each, and then call draw() on each.
  spriteContainer.update();
}

void setupSprites () {
  // create an instance of RectSprite, a NestSprite subclass.
  sprite1 = new RectSprite(200, 200);
  
  // position the RectSprite in the center of the screen.
  sprite1.x = 0.5f * width;
  sprite1.y = 0.5f * height;
  
  // create another instance of RectSprite,
  // and set its properties.
  sprite2 = new RectSprite(100, 100);
  sprite2.x = 50;
  sprite2.y = 50;
  
  // create a NestSpriteContainer to hold all other NestSprites.
  spriteContainer = new NestSpriteContainer(this);
  
  // add sprite1 to the NestSpriteContainer to make it appear on screen.
  spriteContainer.addChild(sprite1);
  
  // add sprite2 as a child of sprite1.
  // children draw in front of their parents,
  // and are subject to all transformations
  // (position, scale, rotation) applied to
  // their parents and ancestors.
  sprite1.addChild(sprite2);
}
