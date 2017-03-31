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

  public AnimatedSprite (TextureAtlas atlas, String name, float fps) {
    animation = new Animation<TextureRegion>(fps, atlas.findRegions(name), Animation.PlayMode.LOOP);
    sprite = new Sprite(animation.getKeyFrame(0));
    // sprite = atlas.createSprite(name);
  }

  // Gotta be a way to play animations without using total elapsed time. like fucking just dt. dt all you need. I need some deepthroat
  public void playAnimation(float elapsedTime) {
    // Animation here just tells the sprite to move it's Region to the latest frame
    sprite.setRegion(animation.getKeyFrame(elapsedTime));
  }

  public void draw(SpriteBatch s) {
    sprite.draw(s);
  }

  public void setPosition(float x, float y) {
    sprite.setPosition(x,y);
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

