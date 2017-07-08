package com.mygdx.game

import com.badlogic.gdx.{Gdx, Game}
import com.badlogic.gdx.graphics.g2d.SpriteBatch

// import com.mygdx.game.screen.DoofyReturnsScreen


class GameApplication extends Game {

  var _batch: SpriteBatch = _

  def batch = _batch

  override def create() {

    val screen = Gdx.app.getPreferences("com.pensi.doofy").getString("screen", "DoofyReturnsScreen")
    _batch = new SpriteBatch

    screen match {
      case "DoofyReturnsScreen" => setScreen(new DoofyReturnsGame(this))
    }

  }

  override def render() {
    super.render()
  }

  override def dispose() {
    batch.dispose
  }


}
