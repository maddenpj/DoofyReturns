package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
  Controller playerOne;
  Sprite s;
  // Alucard purp;
  Purpucard purp;
  Texture background;
  // ParallaxBackground background;
  OrthographicCamera camera = new OrthographicCamera();
  ShapeRenderer debugRenderer;
  boolean debug = true;

  @Override
  public void create () {


    batch = new SpriteBatch();
    // TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("pack/dguy.atlas"));
    // player = new Player(atlas);
    // Texture alucardSheet = new Texture(Gdx.files.internal("purpucard_walk.png")); //31
    // sprite = new AnimatedSprite(alucardSheet, 1, 31, 0.08f);
    // sprite.setPosition(100.0f, 100.0f);

    // ParallaxLayer pl = new ParallaxLayer(
        // new TextureRegion(new Texture(Gdx.files.internal("d4background.bmp"))),
        // new Vector2(1.0f, 0.0f),
        // new Vector2(0, 0)
    // );
    // background = new ParallaxBackground(
        // new ParallaxLayer[]{pl},
        // Gdx.graphics.getWidth(),
        // Gdx.graphics.getHeight(),
        // new Vector2(10, 0)
        // // ,batch
      // );
    

    camera.setToOrtho(false);
    camera.update();
    background = new Texture(Gdx.files.internal("d4background.bmp"));

    debugRenderer = new ShapeRenderer();

    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("pack/alucard.atlas"));
    purp = new Purpucard(atlas);
    purp.setPosition(40.0f,200.0f);

  }

  public void update () {
    // player.update(Gdx.graphics.getDeltaTime());
    float dt = Gdx.graphics.getDeltaTime();
    purp.update(dt);
    float halfWidth = Gdx.graphics.getWidth() / 2.0f;
    if (purp.getX() > halfWidth) {
      camera.position.x = purp.getX();
    }
    camera.update();
  }

  @Override
  public void render () {
    update();
    // Off white bc I was trying to match the bg color of anime spritesheet
    // you know bc converting the png to transparent bg was too hard :(
    Gdx.gl.glClearColor(0.0f, 0.0f,0.0f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // float delta = 0.0f;
    // if (Gdx.input.isKeyPressed(Input.Keys.K)) {
      // delta = 1.0f;
    // }
    // else if (Gdx.input.isKeyPressed(Input.Keys.J)) {
      // delta = -1.0f;
    // }
    // camera.position.x += delta;
    // camera.update();


    // background.render(delta);
    batch.setProjectionMatrix(camera.combined);
    batch.begin();

    batch.draw(background, 0.0f, 0.0f); 
    purp.draw(batch);

    batch.end();

    if(debug) renderDebug();
  }

  public void renderDebug() {
    debugRenderer.setProjectionMatrix(camera.combined);
    debugRenderer.begin(ShapeRenderer.ShapeType.Line);
    debugRenderer.setColor(Color.RED);
    debugRenderer.rect(purp.getX(), purp.getY(), purp.getWidth(), purp.getHeight());
    debugRenderer.end();
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
