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
    sprite = new Sprite(animation.getKeyFrame(0));
    sprite.setPosition(100, 200);
  }

  public void draw(SpriteBatch s, float dt) {
    sprite.setRotation(sprite.getRotation() + 0.1f);
    sprite.setRegion(animation.getKeyFrame(dt, true));
    sprite.draw(s);
  }

  private Animation<TextureRegion> fromSpritesheet(Texture img, int columns, int rows, float dt) {
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
    return new Animation<TextureRegion>(dt, walkFrames);
  }


}

