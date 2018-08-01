package com.mygdx.game.screen

import com.badlogic.gdx.{Gdx, ScreenAdapter, Input}
import com.badlogic.gdx.graphics.{OrthographicCamera, GL20, Texture}
// import com.badlogic.gdx.graphics.g2d._

import com.mygdx.game.GameApplication


class STGScreen(game: GameApplication) extends ScreenAdapter {

  val camera = new OrthographicCamera
  camera.setToOrtho(false)
  camera.update
  val speed = 10.0f

  val background = new Texture(Gdx.files.internal("d4background.bmp"))


  def update(dt: Float) {
    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
      Gdx.app.exit

    val delta =
      if (Gdx.input.isKeyPressed(Input.Keys.K))       speed
      else if (Gdx.input.isKeyPressed(Input.Keys.J)) -speed
      else 0.0f
    camera.position.x += delta;
    camera.update;
  }

  override def render(dt: Float) {
    update(dt)
    Gdx.gl.glClearColor(0.0f, 0.0f,0.0f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    game.batch.setProjectionMatrix(camera.combined)
    game.batch.begin
    game.batch.draw(background, 0.0f, 0.0f)
    game.batch.end

  }

  override def dispose() {
    background.dispose
  }


}
