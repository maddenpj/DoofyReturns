package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.Input;

class Purpucard(atlas: TextureAtlas ) extends HasPosition {
  val walkSpeed = 0.5f;
  var activeAnimation = "idle"

  val animations = Map(
    "idle" -> new AnimatedSprite(atlas, "idle", 0.12f, Animation.PlayMode.LOOP_PINGPONG),
    "swalk" -> new AnimatedSprite(atlas, "swalk", 0.08f)
  )

  def update(dt: Float) {
    var vel = 0.0f

    val moveRight = isKeyPressed(Input.Keys.RIGHT)
    val moveLeft = isKeyPressed(Input.Keys.LEFT)

    activeAnimation = if (moveRight || moveLeft) {
      vel = if (moveRight) walkSpeed else -walkSpeed
      "swalk"
    } else "idle"

    incrPosition(vel)
    animations.values.foreach(_.setPosition(posX, posY))
    animations(activeAnimation).playAnimation(dt)
    
  }

  def draw (s: SpriteBatch) = animations(activeAnimation).draw(s)

  def isKeyPressed(k: Int) = Gdx.input.isKeyPressed(k)

}
