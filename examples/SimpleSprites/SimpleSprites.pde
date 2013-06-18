/*
 * SimpleSprites
 * 
 * NestSprite provides display list functionality, but does not
 * implement any drawing of its own.  To create a custom object
 * that has an on-screen presence, developers must subclass
 * NestSprite and override its update() and draw() methods.
 * 
 * Simple NestSprite subclasses (RectSprite, CircleSprite,
 * and MoverSprite) show how to quickly set up sprites
 * each with their own on-screen presence.
 * 
 * For more detail on subclassing NestSprite to create your own
 * sprite classes, see the CustomSprites example.
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
  
  // RectSprite is a kind of NestSprite that draws a rectangle
  RectSprite rectSprite = new RectSprite();
  rectSprite.x = 100;
  rectSprite.y = 50;
  spriteContainer.addChild(rectSprite);
  
  // CircleSprite is a kind of NestSprite that draws a circle
  CircleSprite circleSprite = new CircleSprite();
  circleSprite.x = 200;
  circleSprite.y = 200;
  spriteContainer.addChild(circleSprite);
  
  // MoverSprite is a kind of NestSprite that moves around
  MoverSprite moverSprite = new MoverSprite();
  moverSprite.y = 325;
  spriteContainer.addChild(moverSprite);
}

class RectSprite extends NestSprite {
  // override NestSprite's draw() method to define how
  // RectSprite instances draw themselves to the screen
  void draw (PApplet p) {
    p.stroke(255);
    p.fill(0x99FFFFFF);
    p.rect(0, 0, 200, 50);
  }
}

class CircleSprite extends NestSprite {
  // override NestSprite's draw() method to define how
  // CircleSprite instances draw themselves to the screen
  void draw (PApplet p) {
    p.stroke(255);
    p.fill(0x99FFFFFF);
    p.ellipse(0, 0, 100, 100);
  }
}

class MoverSprite extends NestSprite {
  float moveCounter = 0;
  
  // override NestSprite's update() method to define how
  // MoverSprite instances do things other than drawing,
  // such as changing their position, rotation, and scale
  void update (PApplet p) {
    this.x = 200 + 100*sin(moveCounter);
    moveCounter += 0.01;
    this.rotationZ = moveCounter;
  }
  
  // override NestSprite's draw() method to define how
  // MoverSprite instances draw themselves to the screen
  void draw (PApplet p) {
    p.stroke(255);
    p.fill(0x99FFFFFF);
    p.rect(-40, -10, 80, 20);
  }
}
