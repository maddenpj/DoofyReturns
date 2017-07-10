package com.mygdx.game.screen;

import com.badlogic.gdx.maps.tiled._;
import com.badlogic.gdx.{ApplicationAdapter, Gdx, Input, InputProcessor, ScreenAdapter};
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera, Texture};
import com.badlogic.gdx.graphics.g2d._;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.math.Rectangle;

import com.mygdx.game.GameApplication
import com.mygdx.game._;

class TiledScreen(game: GameApplication) extends ScreenAdapter{

  val level = "levels/level1.tmx";
  var map : TiledMap = new TmxMapLoader().load(level);
  var renderer : OrthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);

  val camera = new OrthographicCamera;
  val font = new BitmapFont;


  //Using previously created spritebatch
  val batch = game.batch;

  //declare height/width vars
  val w = Gdx.graphics.getWidth();
  val h = Gdx.graphics.getHeight();

  Gdx.app.log("width,height:",w.toString + " " + h.toString)

  //setup camera
  camera.setToOrtho(false, (w / h) * 10, 10);
  camera.update();

  //init purp

  val levelRect = new Rectangle(0, 0, 640, 480);
  val atlas = new TextureAtlas(Gdx.files.internal("pack/alucard.atlas"));

  val purp = new Purpucard(atlas, levelRect);
  purp.setPosition(80.0f, 150.0f);

  Gdx.input.setInputProcessor(PlayerInput.defaultAdapter(purp));
  val tp = new TestPlayer(atlas);

  def update(dt: Float) {

    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
      Gdx.app.exit

    purp.update(dt);
    tp.update(dt);
    
  }

  override def render (dt: Float) {
    update(dt);
    Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    camera.update();
    renderer.setView(camera);
    renderer.render();
    batch.begin();

    purp.draw(batch);
    tp.draw(batch);

    font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);

    batch.end();
  }

  override def dispose () {
    map.dispose();
  }

}
