package com.mygdx.game.screen;

import com.badlogic.gdx.maps.tiled._;
import com.badlogic.gdx.{ApplicationAdapter, Gdx, Input, InputProcessor, ScreenAdapter};
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera, Texture};
import com.badlogic.gdx.graphics.g2d._;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.mygdx.game.GameApplication

class TiledScreen(game: GameApplication) extends ScreenAdapter{

    var map : TiledMap = _;
	var renderer : OrthogonalTiledMapRenderer = _;

    val camera = new OrthographicCamera;
	val font = new BitmapFont;
	val batch = new SpriteBatch;
    
		val w = Gdx.graphics.getWidth();
		val h = Gdx.graphics.getHeight();
		Gdx.app.log("width,height:",w.toString + " " + h.toString)

		camera.setToOrtho(false, (w / h) * 10, 10);
		camera.update();

		//cameraController = new OrthoCamController(camera);
		//Gdx.input.setInputProcessor(cameraController);

        val level = "levels/level1.tmx";
		map = new TmxMapLoader().load(level);

		renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);

	override def render (dt: Float) {
		Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		renderer.setView(camera);
		renderer.render();
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		batch.end();
	}

	override def dispose () {
		map.dispose();
	}

} 
	