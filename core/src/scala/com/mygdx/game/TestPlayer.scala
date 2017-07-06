package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{TextureAtlas, SpriteBatch}
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode

import com.mygdx.game.background.Animation



class TestPlayer(atlas: TextureAtlas)
  extends Renderable
  with HasPosition {

 
  // TODO:
  // So I'm leaning towards an intermediary between Player and his collection of animations
  // The only "states" relevant to Player at his level should be actual gameplay related ones
  // And his AnimationManager or w/e manages the states of his animations and their transisitions

  val idle = Animation.looping(atlas, "idle", 0.12f, PlayMode.LOOP_PINGPONG)
  idle.sprite.setScale(3.0f)
  setPosition(400,100)

  def update(dt: Float) {
    idle.setPosition(position)
    idle.play(dt)
  }

  def draw (s: SpriteBatch) = idle.render(s)
}
