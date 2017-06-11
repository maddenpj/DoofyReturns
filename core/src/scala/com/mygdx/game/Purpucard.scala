package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d._


class Purpucard(atlas: TextureAtlas)
    extends Renderable
    with HasPosition
    with PlayerControlled {

  val walkSpeed = 0.5f;
  var activeAnimationName = "idle"

  val animations = Map(
    "idle" -> new AnimatedSprite(atlas, "idle", 0.12f, Animation.PlayMode.LOOP_PINGPONG),
    "swalk" -> new AnimatedSprite(atlas, "swalk", 0.08f),
    "punch" -> new AnimatedSprite(atlas, "punch", 0.08f, Animation.PlayMode.NORMAL, false)
  )
  def activeAnimation() = animations(activeAnimationName)
  animations("swalk").getSprite.setSize(105.0f, 61.0f)

  val bindings = Map(
    MoveRight -> Input.Keys.RIGHT,
    MoveLeft  -> Input.Keys.LEFT,
    MoveUp    -> Input.Keys.UP,
    MoveDown  -> Input.Keys.DOWN,
    Punch -> Input.Keys.SPACE
  )

  def animBindings(a: Set[Action]) = a match {
    case _ if a.contains(Punch) => "punch"
    case _ if a.nonEmpty => "swalk"
    case _ => "idle"
  }

  def update(dt: Float) {
    var vel = 0.0f

    // This is so gross
    val in = getInput()
    if (activeAnimation.canInterrupt) {
      activeAnimationName = animBindings(in)
      if (in(MoveRight) || in(MoveLeft)) {
        vel = if (in(MoveRight)) walkSpeed else -walkSpeed
      }
    } else {
      if (activeAnimation.isFinished) {
        // activeAnimation.stop
        // ^ This gets you stuck in an unescapable loop bc canInterrupt is always false
        activeAnimationName = "idle"
      }
    }

    incrPosition(vel)
    animations.values.foreach(_.setPosition(posX, posY))
    activeAnimation.playAnimation(dt)
    animations.filterNot(_._1 == activeAnimationName).foreach(_._2.stop)
  }

  def draw (s: SpriteBatch) = activeAnimation.draw(s)
}
