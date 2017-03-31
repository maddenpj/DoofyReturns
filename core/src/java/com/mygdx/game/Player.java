package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.Input;

// Realized after making this file, it'll probably end up as AnimatedEntity or similar
// just bc the right abstraction tree is like:
//   AnimatedEntity 
//     - Contains 1..n AnimatedSprite for each animation it supports (walk, dying, jumping, etc..)
//
//   Player
//     - Contains the protaganist's AnimatedSprite
//     - Uses keyboard/controller input to decide when to play each animation
//
//       (versus say AI controlled thing where code says if it's walking/jumping/etc)

public class Player {

  float posX = 100.0f;
  float posY = 100.0f;
  float velX = 0.0f;
  float walkSpeed = 1.0f;

  // We need several of these so we need a state machine to manage them
  // Only one should be active at a time, draw() renders only the active one
  // update manages which AnimatedSprite is active. ie:
  //     walk input = walk
  //     No input = standing
  //     dying, jumping, etc
  float fps  = 0.14f;
  AnimatedSprite walk;

  public Player(TextureAtlas atlas) {
    walk = new AnimatedSprite(atlas, "walk", fps);
  }

  public void update(float elapsedTime) {
    velX = 0.0f;

    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      velX = walkSpeed;
      walk.playAnimation(elapsedTime);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      velX = -walkSpeed;
      walk.playAnimation(elapsedTime);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
    }

    posX += velX;
    walk.setPosition(posX, posY);
  }

  public void draw (SpriteBatch s) {
    walk.draw(s);
  }


  // Fuck it, implemented MVP of this
  // TODO: change to 2d vector
  //       Actually we need 2 things, position and velocity
  //       on every update: pos + vel (vector aka posX += velX and so on)
  //       but velocity is only nonzero if move key is pressed
  // Terrible. walkSpeed is already member of class but have to pass to this func
  // because it needs to be negative if going left
  private void walk(float elapsedTime, float speed) {
      posX += speed;
  }
}
