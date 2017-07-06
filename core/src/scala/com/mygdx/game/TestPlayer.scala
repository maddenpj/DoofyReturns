package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{TextureAtlas, SpriteBatch}
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode

import com.mygdx.game.background.Animation



class TestPlayer(atlas: TextureAtlas)
  extends Renderable
  with HasPosition {

 

  val idle = Animation.looping(atlas, "idle", 0.12f, PlayMode.LOOP_PINGPONG)
  idle.sprite.setScale(3.0f)
  idle.setPosition(100,100)

  def update(dt: Float) {
    idle.play(dt)
  }

  def draw (s: SpriteBatch) = idle.render(s)
}
