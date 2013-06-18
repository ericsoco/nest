/*
 * CustomSprites
 * 
 * NestSprite provides display list functionality, but does not
 * implement any drawing of its own.  To create a custom object
 * that has an on-screen presence, developers must subclass
 * NestSprite and override its update() and draw() methods.
 * 
 * The example shape classes here each subclass NestSprite,
 * but implement the update() and draw() methods differently,
 * creating four different shapes on-screen, each moving differently.
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
  
  // attach NestSprite instances to the SpriteContainer
  SquareSprite square = new SquareSprite();
  square.x = 100;
  square.y = 100;
  spriteContainer.addChild(square);
  
  TriangleSprite triangle = new TriangleSprite();
  triangle.x = 300;
  triangle.y = 100;
  spriteContainer.addChild(triangle);
  
  PentaSprite pentagon = new PentaSprite();
  pentagon.x = 100;
  pentagon.y = 300;
  spriteContainer.addChild(pentagon);
  
  CircleSprite circle = new CircleSprite();
  circle.x = 300;
  circle.y = 300;
  spriteContainer.addChild(circle);
}
