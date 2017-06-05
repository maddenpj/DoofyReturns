package com.mygdx.game;

import java.util.Map;
import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.Input;


public class Alucard {

  float posX = 100.0f;
  float posY = 100.0f;
  float velX = 0.0f;
  float walkSpeed = 0.5f;

  Map<String, AnimatedSprite> animations;
  String activeAnimation;


  public Alucard(TextureAtlas atlas) {
    animations = new HashMap<String, AnimatedSprite>();
    animations.put("idle", new AnimatedSprite(atlas, "idle", 0.12f, Animation.PlayMode.LOOP_PINGPONG));
    animations.put("swalk", new AnimatedSprite(atlas, "swalk", 0.08f));

    activeAnimation = "idle";
  }

  public void update(float dt) {
    velX = 0.0f;
    boolean moveRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    boolean moveLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT);

    if (moveRight || moveLeft) {
      activeAnimation = "swalk";
      velX = (moveRight) ? walkSpeed : -walkSpeed;
    } else {
      activeAnimation = "idle";
    }

    posX += velX;
    animations.forEach((k,v) -> v.setPosition(posX, posY));
    animations.get(activeAnimation).playAnimation(dt);
  }

  public void draw (SpriteBatch s) {
    // walk.draw(s);
    animations.get(activeAnimation).draw(s);
  }

}
