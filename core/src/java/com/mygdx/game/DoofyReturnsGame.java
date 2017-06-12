package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.mappings.Xbox;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import com.mygdx.game.background.*;


public class DoofyReturnsGame extends ApplicationAdapter {
  final String TAG = "DoofyReturnsGame";
  SpriteBatch batch;
  // Texture img;
  AnimatedSprite sprite;
  Player player;
  Controller playerOne;
  Sprite s;
  // Alucard purp;
  Purpucard purp;
  // Texture background;
  ParallaxBackground background;

  @Override
  public void create () {


    batch = new SpriteBatch();
    // TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("pack/dguy.atlas"));
    // player = new Player(atlas);
    // Texture alucardSheet = new Texture(Gdx.files.internal("purpucard_walk.png")); //31
    // sprite = new AnimatedSprite(alucardSheet, 1, 31, 0.08f);
    // sprite.setPosition(100.0f, 100.0f);

    ParallaxLayer pl = new ParallaxLayer(
        new TextureRegion(new Texture(Gdx.files.internal("d4background.bmp"))),
        new Vector2(1.0f, 0.0f),
        new Vector2(0, 0)
    );
    background = new ParallaxBackground(
        new ParallaxLayer[]{pl},
        Gdx.graphics.getWidth(),
        Gdx.graphics.getHeight(),
        new Vector2(150, 0)
        // ,batch
      );
    
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("pack/alucard.atlas"));
    purp = new Purpucard(atlas);
    purp.setPosition(40.0f,200.0f);

  }

  public void update () {
    // player.update(Gdx.graphics.getDeltaTime());
    float dt = Gdx.graphics.getDeltaTime();
    purp.update(dt);
  }

  @Override
  public void render () {
    update();
    // Off white bc I was trying to match the bg color of anime spritesheet
    // you know bc converting the png to transparent bg was too hard :(
    Gdx.gl.glClearColor(0.0f, 0.0f,0.0f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    float delta = 0.0f;
    if (Gdx.input.isKeyPressed(Input.Keys.K)) {
      delta = 1.0f;
    }


    background.render(delta);
    batch.begin();

    purp.draw(batch);

    batch.end();
  }

  @Override
  public void dispose () {
    batch.dispose();
    // img.dispose();
  }

  private void isPlayerOneReady() {
    Gdx.app.log(TAG, "Controllers:");
    for (Controller controller : Controllers.getControllers()) {
      Gdx.app.log(TAG, controller.getName());
      if (Xbox.isXboxController(controller)) {
        playerOne = controller;
        Gdx.app.log(TAG, "Ready Player One.");
      }
    }
  }
}
