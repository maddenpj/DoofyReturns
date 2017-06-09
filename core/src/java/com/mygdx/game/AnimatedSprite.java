package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

public class AnimatedSprite {
  
  Sprite sprite;
  Texture sheet;
  int sheetCols, sheetRows;
  Animation<TextureRegion> animation;
  float fps;
  float elapsedTime;
  boolean animationStarted = false;


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

  public AnimatedSprite (TextureAtlas atlas, String name, float fps, Animation.PlayMode mode) {
    animation = new Animation<TextureRegion>(fps, atlas.findRegions(name), mode);
    // sprite = new Sprite(animation.getKeyFrame(0));
    sprite = atlas.createSprite(name);
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

  public void stopAnimation() {
    animationStarted = false;
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

