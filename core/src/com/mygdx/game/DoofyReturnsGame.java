package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

public class DoofyReturnsGame extends ApplicationAdapter {
  int FRAME_COLS = 6, FRAME_ROWS = 5;
  float t = 0.0f;
  SpriteBatch batch;
  Texture img;
  TextureRegion imgr;
  Animation<TextureRegion> walkAnimation;
  
  @Override
  public void create () {
    batch = new SpriteBatch();
    img = new Texture("sprite1.png");
    // imgr = new TextureRegion(img, 0, 0, img.getWidth(), img.getHeight() / 4);
    walkAnimation = fromSpritesheet(img, 2, 2, 0.14f);
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

  @Override
  public void render () {
    Gdx.gl.glClearColor(0.86f, 0.88f,0.86f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    t += Gdx.graphics.getDeltaTime();
    batch.begin();
    batch.draw(walkAnimation.getKeyFrame(t, true), 100, 100);
    // batch.draw(imgr.getTexture(), 100, 100);
    batch.end();
  }
  
  @Override
  public void dispose () {
    batch.dispose();
    img.dispose();
  }
}
