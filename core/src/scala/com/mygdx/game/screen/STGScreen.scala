package com.mygdx.game.screen

import scala.collection.JavaConverters._

import com.badlogic.gdx.{Gdx, ScreenAdapter, Input}
import com.badlogic.gdx.graphics.{OrthographicCamera, GL20, Texture}
import com.badlogic.gdx.controllers._
import com.badlogic.gdx.controllers.mappings.Xbox
// import com.badlogic.gdx.graphics.g2d._

import com.mygdx.game.GameApplication


class STGControlAdapter extends ControllerAdapter {
  override def buttonDown(c: Controller, btnIndex: Int) = {
    // Gdx.app.log("STGControlAdapter", s"ButtonDown: $btnIndex")
    if (btnIndex == Xbox.A) Gdx.app.log("InputDown", "FIRE")
    if (btnIndex == Xbox.B) Gdx.app.log("InputDown", "BOMB")
    false
  }
}

class STGScreen(game: GameApplication) extends ScreenAdapter {

  val background = new Texture(Gdx.files.internal("Arcade - Darius - Silver Hawk.png"))

  // Controllers.getControllers().asScala.foreach(println)
  val pad = Controllers.getControllers().asScala.headOption
    .foreach(_.addListener(new STGControlAdapter))


  def update(dt: Float) {
    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
      Gdx.app.exit
  }

  override def render(dt: Float) {
    update(dt)
    Gdx.gl.glClearColor(0.0f, 0.0f,0.0f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    game.batch.begin
    game.batch.draw(background, 0.0f, 0.0f)
    game.batch.end

  }

  override def dispose() {
    background.dispose
  }


}
