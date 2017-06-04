package com.mygdx.game;

import java.util.Map;
import java.util.HashMap;
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
  Map<String, AnimatedSprite> animations;
  String activeAnimation;


  public Player(TextureAtlas atlas) {

    animations = new HashMap<String, AnimatedSprite>();
    animations.put("walk", new AnimatedSprite(atlas, "walk", 0.14f));
    animations.put("death", new AnimatedSprite(atlas, "death", 0.25f, Animation.PlayMode.NORMAL));

    activeAnimation = "walk";

    // animations.forEach((k,v) -> Gdx.app.log("Player", k + " -> " + v.toString()));
  }

  public void update(float dt) {
    velX = 0.0f;

    if (activeAnimation == "walk") {
      if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        velX = walkSpeed;
        animations.get("walk").playAnimation(dt);
      }
      if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        velX = -walkSpeed;
        animations.get("walk").playAnimation(dt);
      }
    }

    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      activeAnimation = "death";
      animations.get("death").playAnimation(dt);
    }

    posX += velX;
    animations.forEach((k,v) -> v.setPosition(posX, posY));
  }

  public void draw (SpriteBatch s) {
    // walk.draw(s);
    animations.get(activeAnimation).draw(s);
  }

  // Fuck it, implemented MVP of this
  // TODO: change to 2d vector
  //       Actually we need 2 things, position and velocity
  //       on every update: pos + vel (vector aka posX += velX and so on)
  //       but velocity is only nonzero if move key is pressed
  // Terrible. walkSpeed is already member of class but have to pass to this func
  // because it needs to be negative if going left
  // private void walk(float speed) {
      // posX += speed;
  // }
}
