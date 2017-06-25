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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Preferences;

import io.anuke.gif.GifRecorder;

import com.mygdx.game.background.*;


public class DoofyReturnsGame extends ApplicationAdapter {

  Preferences prefs;
  SpriteBatch batch;
  // Texture img;
  AnimatedSprite sprite;
  Controller playerOne;
  Sprite s;
  // Alucard purp;
  Purpucard purp;
  Texture background;
  Rectangle levelRect;
  // ParallaxBackground background;
  OrthographicCamera camera = new OrthographicCamera();
  ShapeRenderer debugRenderer;
  boolean debug;
  GifRecorder recorder;

  @Override
  public void create () {

    prefs = Gdx.app.getPreferences("com.pensi.doofy");


    batch = new SpriteBatch();
    camera.setToOrtho(false);
    camera.update();
    background = new Texture(Gdx.files.internal("d4background.bmp"));
    levelRect = new Rectangle(0, 0, 2046, 101);


    debug = prefs.getBoolean("debug", false);
    if (debug) {
      recorder = new GifRecorder(batch);
      recorder.setResizeKey(Input.Keys.R);
      debugRenderer = new ShapeRenderer();
    }


    Gdx.input.setInputProcessor(PlayerState.defaultInputProcessor());

    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("pack/alucard.atlas"));
    purp = new Purpucard(atlas, levelRect);
    purp.setPosition(40.0f, 50.0f);

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
    // levelRect.y += delta;
    // Gdx.app.log("levelRect", levelRect.toString());
    // camera.position.x += delta;
    // camera.update();


    // background.render(delta);
    batch.setProjectionMatrix(camera.combined);
    batch.begin();

    batch.draw(background, 0.0f, 0.0f); 
    purp.draw(batch);

    batch.end();

    if(debug) {
      recorder.update();
      renderDebug();
    }
  }

  public void renderDebug() {
    debugRenderer.setProjectionMatrix(camera.combined);
    debugRenderer.begin(ShapeRenderer.ShapeType.Line);
    debugRenderer.setColor(Color.RED);
    Rectangle purpRect = purp.getRect();
    debugRenderer.rect(purpRect.x, purpRect.y, purpRect.width, purpRect.height);
    debugRenderer.setColor(Color.BLUE);
    debugRenderer.rect(levelRect.x, levelRect.y, levelRect.width, levelRect.height);
    debugRenderer.end();
  }

  @Override
  public void dispose () {
    batch.dispose();
    prefs.flush();
  }

  private void isPlayerOneReady() {
    Gdx.app.log("ControllerSupport", "Controllers:");
    for (Controller controller : Controllers.getControllers()) {
      Gdx.app.log("ControllerSupport", controller.getName());
      if (Xbox.isXboxController(controller)) {
        playerOne = controller;
        Gdx.app.log("ControllerSupport", "Ready Player One.");
      }
    }
  }
}
