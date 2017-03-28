package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

public class AnimatedSprite {
  
  Sprite sprite;
  Texture sheet;
  int sheetCols, sheetRows;
  Animation<TextureRegion> animation;
  float fps;

  public AnimatedSprite (Texture sheet, int sRows, int sCols, float sFps) {
    this.sheet = sheet;
    this.fps = sFps;
    this.sheetCols = sCols;
    this.sheetRows = sRows;
    animation = fromSpritesheet(sheet, sheetCols, sheetRows, fps);

    // Sprite and Animation both use the same Texture
    // This way spritesheet.png gets loaded into gpu once and remains there
    // TextureRegions are just a rectangle (x,y,w,h) and a pointer to a texture
    // So Sprite/Animation only have to pass around TextureRegions
    sprite = new Sprite(animation.getKeyFrame(0));

    // This is player/enemy/etc. position, makes it super easy to wire input to a sprite
    sprite.setPosition(100, 200);
  }

  public void draw(SpriteBatch s, float totalTime) {
    // Rotation only because wanted to test something Sprite specific
    // not just something that a TextureRegion can do also
    // sprite.setRotation(sprite.getRotation() + 0.1f);
    
    // Animation here just tells the sprite to move it's Region to the latest frame
    sprite.setRegion(animation.getKeyFrame(totalTime, true));
    sprite.draw(s);
  }

  public void incrementPositionX(float dx) {
    sprite.setX(sprite.getX() + dx);
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

