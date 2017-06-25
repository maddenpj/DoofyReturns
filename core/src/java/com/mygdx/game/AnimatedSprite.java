package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

/*********************************
 * TODO:
 *  Need to figure out interruptible/non-interuptible animations
 *  Incorporate animation playtime into input handling
 *    ie: can't start walking again until punch finishes
 *
 *********************************/


public class AnimatedSprite {

  Sprite sprite;
  Texture sheet;
  int sheetCols, sheetRows;
  Animation<TextureRegion> animation;
  float fps;
  float elapsedTime;
  boolean interruptible = true;
  boolean animationStarted = false;
  String name = "NONE";


  public AnimatedSprite (Texture sheet, int sRows, int sCols, float sFps, Animation.PlayMode mode) {
    this.sheet = sheet;
    this.fps = sFps;
    this.sheetCols = sCols;
    this.sheetRows = sRows;
    animation = fromSpritesheet(sheet, sheetCols, sheetRows, fps);
    animation.setPlayMode(mode);

    // Sprite and Animation both use the same Texture
    // This way spritesheet.png gets loaded into gpu once and remains there
    // TextureRegions are just a rectangle (x,y,w,h) and a pointer to a texture
    // So Sprite/Animation only have to pass around TextureRegions
    sprite = new Sprite(animation.getKeyFrame(0));

    // This is player/enemy/etc. position, makes it super easy to wire input to a sprite
    sprite.setPosition(0, 0);
  }

  public AnimatedSprite (Texture sheet, int sRows, int sCols, float sFps) {
    this(sheet, sRows, sCols, sFps, Animation.PlayMode.LOOP);
  }

  public AnimatedSprite (TextureAtlas atlas, String name, float fps, Animation.PlayMode mode, boolean interrupt) {
    animation = new Animation<TextureRegion>(fps, atlas.findRegions(name), mode);
    // sprite = new Sprite(animation.getKeyFrame(0));
    sprite = atlas.createSprite(name);
    this.interruptible = interrupt;
    this.name = name;
  }

  public AnimatedSprite (TextureAtlas atlas, String name, float fps, Animation.PlayMode mode) {
    this(atlas, name, fps, mode, true);
  }

  public AnimatedSprite (TextureAtlas atlas, String name, float fps) {
    this(atlas, name, fps, Animation.PlayMode.LOOP);
  }


  public void playAnimation(float deltaTime) {
    if(!animationStarted) {
      animationStarted = true;
      elapsedTime = 0.0f;
    }
    elapsedTime += deltaTime;
  }

  public void stop() {
    animationStarted = false;
  }

  public boolean isFinished() {
    // Animation docs:
    // Whether the animation would be finished if played without looping (PlayMode#NORMAL)
    return animation.isAnimationFinished(elapsedTime);
  }

  public float getElapsedTime() {
    return elapsedTime;
  }

  public float getDuration() {
    return animation.getAnimationDuration();
  }

  public String getName() {
    return name;
  }

  public boolean canInterrupt() {
    // if (!interruptible) return isFinished();
    return interruptible;
  }

  public void draw(SpriteBatch s) {
    sprite.setRegion(animation.getKeyFrame(elapsedTime));
    sprite.draw(s);
  }

  public void setPosition(float x, float y) {
    sprite.setPosition(x,y);
  }

  public float getX() { return sprite.getX(); }
  public float getY() { return sprite.getY(); }

  public Sprite getSprite() { return sprite; }

  // From stackoverflow
  private Animation<TextureRegion> fromSpritesheet(Texture img, int columns, int rows, float fps) {
    TextureRegion[][] tmp = TextureRegion.split(img,
        img.getWidth() / columns,
        img.getHeight() / rows);
    TextureRegion[] walkFrames = new TextureRegion[columns * rows];
    int index = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        walkFrames[index++] = tmp[i][j];
      }
    }
    return new Animation<TextureRegion>(fps, walkFrames);
  }
}

