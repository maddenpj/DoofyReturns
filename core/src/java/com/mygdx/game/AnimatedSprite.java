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

  // This is mostly for messing around, use the Atlas constructor
  public AnimatedSprite (Texture sheet, int sRows, int sCols, float sFps, Animation.PlayMode mode) {
    this.sheet = sheet;
    this.fps = sFps;
    this.sheetCols = sCols;
    this.sheetRows = sRows;
    animation = fromSpritesheet(sheet, sheetCols, sheetRows, fps);
    animation.setPlayMode(mode);
    sprite = new Sprite(animation.getKeyFrame(0));
    sprite.setPosition(0, 0);
  }

  public AnimatedSprite (Texture sheet, int sRows, int sCols, float sFps) {
    this(sheet, sRows, sCols, sFps, Animation.PlayMode.LOOP);
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

  /** Flips all frames from {@code startTime} to {@code endTime}.
   *  Note the actual TextureRegions are {@link TextureRegion#flip(boolean, boolean) flipped}, so if the {@link #animation} contains a region more than once, those frames cannot be flipped differently at the same time.
   *  Also they will be flipped as often as they occur in the given time range.
   *  @param startTime the animation state time of the first frame to flip
   *  @param endTime the animation state time of the last frame to flip
   *  @param set if the frames should be set to {@code flipX} and {@code flipY} instead of actually flipping them */
  public void flipFrames(float startTime, float endTime, boolean flipX, boolean flipY, boolean set) {
    for(float t = startTime; t < endTime; t += animation.getFrameDuration()) {
      TextureRegion frame = animation.getKeyFrame(t);
      frame.flip(set ? flipX && !frame.isFlipX() : flipX, set ? flipY && !frame.isFlipY() : flipY);
    }
  }

  /** flips all frames
   *  @see #flipFrames(boolean, boolean, boolean) */
  public void flipFrames(boolean flipX, boolean flipY) {
    flipFrames(flipX, flipY, false);
  }

  /** flips all frames
   *  @see #flipFrames(float, float, boolean, boolean, boolean) */
  public void flipFrames(boolean flipX, boolean flipY, boolean set) {
    flipFrames(0, animation.getAnimationDuration(), flipX, flipY, set);
  }

  /** @see #flipFrames(float, float, boolean, boolean, boolean) */
  public void flipFrames(float startTime, float endTime, boolean flipX, boolean flipY) {
    flipFrames(startTime, endTime, flipX, flipY, false);
  }
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

