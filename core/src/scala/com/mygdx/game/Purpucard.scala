package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.math.{Rectangle, Vector2}




// Also gross to pass in level bounds like this
class Purpucard(atlas: TextureAtlas, levelRect: Rectangle)
    extends Renderable
    with HasPosition {
    import PlayerState._


  val walkSpeed = 2.5f;
  val spriteScale = 3.0f;
  var activeAnimationName = "idle"

  val startFps = 0.06f;
  val walkFps = 0.08f;
  val punchFps = 0.08f;
  // Coupling these together was a shit idea Prob should be just a Seq()
  val animations = Map(
    "idle"        -> StateAnimation(Idle, new AnimatedSprite( atlas, "idle", 0.12f, Animation.PlayMode.LOOP_PINGPONG)),
    "walking"     -> StateAnimation(Moving, new AnimatedSprite( atlas, "walking", walkFps)),
    "startwalk"   -> StateAnimation(MovementTransition, new AnimatedSprite( atlas, "startwalk", startFps, Animation.PlayMode.NORMAL, true)),
    "startwalk_r" -> StateAnimation(MovementTransition, new AnimatedSprite( atlas, "startwalk", startFps, Animation.PlayMode.REVERSED, true)),
    "punch"       -> StateAnimation(Punching, new AnimatedSprite( atlas, "punch", punchFps, Animation.PlayMode.NORMAL, false))
  )
  var activeState: StateAnimation = animations("idle")
  var lastState = activeState.state
  // animations("punch").animation.flipFrames(true, false)

  // Might cause issues
  animations.values.foreach(_.animation.getSprite.setScale(spriteScale))

  // update(in: Set[Action], dt: Float)? might be better
  // but breaks Renderable
  //
  // Thinking out loud, how do games work
  // (Input) -> [Engine layer] -> Player action
  // ie: Just bc moveRight is down doesn't mean player will move (wall, etc)
  //
  //  TODO 4AM EDIT: THIS IS A STATE Monad!
  //
  def update(dt: Float) {
    val in = PlayerState.defaultInputProcessor.getInput()
    lastState = activeState.state
    activeState = stateTransition(in, activeState)
    val vel = computeVelocity(in, activeState) // Don't like the order dependence here

    if (lastState != activeState.state) Gdx.app.log("Purpucard", s"$lastState → ${activeState.state}")

    // Collision detection
    if (levelRect.contains(vel.cpy.add(getX, getY))) {
      incrPosition(vel.nor.scl(walkSpeed))
    }

    animations.values.foreach(_.animation.setPosition(posX, posY))
    activeState.animation.playAnimation(dt)
    animations.values.filterNot(_ == activeState).foreach(_.animation.stop)
  }

  def activeAnimation() = activeState.animation

  // TODO: Ok so this is all the state machine transistion stuff for the keyDown "event"
  //        need to complete state machine with transistions for keyUp
  def stateTransition(in: Set[InputAction], state: StateAnimation) =
    if (state.animation.canInterrupt) in match {
        case _ if in.contains(Punch) => animations("punch")
        case _ if in.exists(movement.contains) => walkTransition(in, state)
        case _ => animations("idle")
    } else if (state.animation.isFinished) animations("idle")
      else state

  // Real janky bc this only fires if a movement key is current pressed down
  def walkTransition(in: Set[InputAction], state: StateAnimation) = state.state match {
    case Idle => animations("startwalk")
    case MovementTransition if(state.animation.isFinished) => animations("walking")
    case _ => state
  }

  def computeVelocity(in: Set[InputAction], state: StateAnimation) = {
    val vel = new Vector2();

    if (state.state.moves) {
      if (in(MoveRight)) vel.x = walkSpeed
      if (in(MoveLeft)) vel.x = -walkSpeed
      if (in(MoveUp)) vel.y = walkSpeed
      if (in(MoveDown)) vel.y = -walkSpeed
    }
    vel
  }

  def draw (s: SpriteBatch) = activeAnimation.draw(s)

  def getWidth() = activeAnimation.getSprite.getWidth
  def getHeight() = activeAnimation.getSprite.getHeight
  def getRect() = new Rectangle(getX, getY, getWidth, getHeight)
}
