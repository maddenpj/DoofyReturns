package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
// import com.badlogic.gdx.controllers.Controllers;
// import com.badlogic.gdx.controllers.Controller;
// import com.badlogic.gdx.controllers.mappings.Xbox;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Preferences;

// import com.mygdx.game.background.*;
import io.anuke.gif.GifRecorder;


public class DoofyReturnsGame extends ApplicationAdapter {

  Preferences prefs;
  SpriteBatch batch;
  Purpucard purp;
  Texture background;
  Rectangle levelRect;

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

    // Gdx.input.setInputProcessor(PlayerState.defaultInputProcessor());

    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("pack/alucard.atlas"));
    purp = new Purpucard(atlas, levelRect);
    purp.setPosition(45.0f, 70.0f);
    Gdx.input.setInputProcessor(PlayerInput.defaultAdapter(purp));



    debug = prefs.getBoolean("debug", false);
    if (debug) {
      float hw = Gdx.graphics.getWidth() / 2;
      float hh = Gdx.graphics.getHeight() / 2;
      recorder = new GifRecorder(batch);
      recorder.setResizeKey(Input.Keys.R);
      recorder.setBounds(-hw+(hw/2), -hh, 200, 200);
      recorder.setFPS(24);
      debugRenderer = new ShapeRenderer();
    }
  }

  public void update () {
    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();

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
    this.update();
    Gdx.gl.glClearColor(0.0f, 0.0f,0.0f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    batch.setProjectionMatrix(camera.combined);
    batch.begin();

    batch.draw(background, 0.0f, 0.0f); 
    purp.draw(batch);

    batch.end();

    if(debug) {
      recorder.update();
      if (prefs.getBoolean("debug.renderRects", false)) renderDebug();
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

  // private void isPlayerOneReady() {
    // Gdx.app.log("ControllerSupport", "Controllers:");
    // for (Controller controller : Controllers.getControllers()) {
      // Gdx.app.log("ControllerSupport", controller.getName());
      // if (Xbox.isXboxController(controller)) {
        // playerOne = controller;
        // Gdx.app.log("ControllerSupport", "Ready Player One.");
      // }
    // }
  // }
}
