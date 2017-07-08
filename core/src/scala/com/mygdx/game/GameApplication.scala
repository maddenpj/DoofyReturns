package com.mygdx.game

import com.badlogic.gdx.{Gdx, Game}
import com.badlogic.gdx.graphics.g2d.SpriteBatch

import com.mygdx.game.screen._


class GameApplication extends Game {

  var _batch: SpriteBatch = _

  def batch = _batch

  override def create() {

    val screen = Gdx.app.getPreferences("com.pensi.doofy").getString("screen", "DoofyReturnsScreen")
    Gdx.app.log("GameApplication", s"Using screen: $screen")

    _batch = new SpriteBatch

    // This is *bad* design but very convenient for dev
    screen match {
      case "DoofyReturnsScreen" => setScreen(new DoofyReturnsScreen(this))
      case "TestScreen" => setScreen(new TestScreen(this))
    }

  }

  override def render() {
    super.render()
  }

  override def dispose() {
    batch.dispose
  }


}
