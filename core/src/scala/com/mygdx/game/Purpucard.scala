package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.math.{Rectangle, Vector2}




// object Purpucard {

  // import com.badlogic.gdx.Input.Keys


  // sealed trait Action
  // case object Idle extends Action
  // case object Punching extends Action
  // case object Moving extends Action
  // case object Dead extends Action

  // trait AnimationState
  // case object Playing extends AnimationState
  // case object Finished extends AnimationState

  // trait InputEvent
  // case object MoveRight extends InputEvent
  // case object Punch extends InputEvent

  // trait PurpucardState {
    // def action: Action
    // def animation: AnimationState

    // def onKeyPressed(i: InputEvent): PurpucardState
    // def onKeyReleased(i: InputEvent): PurpucardState
  // }

  // case class PurpucardState(action: Action, animation: AnimationState) {
    // def onKeyPressed(i: InputEvent): PurpucardState = ???
    // def onKeyReleased(i: InputEvent): PurpucardState = ???
  // }


  // // trait KeyReleasedEvent
  // // case object MoveRight extends KeyReleasedEvent
  // // case object Punch extends KeyReleasedEvent


// }


// Also gross to pass in level bounds like this
class Purpucard(atlas: TextureAtlas, levelRect: Rectangle)
    extends Renderable
    with HasPosition {
    import PlayerInput._
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
  var lastState = activeState
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

    // val vel = computeVelocity(in, activeState) // Don't like the order dependence here
    val vel = new Vector2

    // Collision detection
    if (levelRect.contains(vel.cpy.add(position))) {
      _position.add(vel.nor.scl(walkSpeed))
    }

    animations.values.foreach(_.animation.setPosition(position.x, position.y))
    activeState.animation.playAnimation(dt)
    animations.values.filterNot(_ == activeState).foreach(_.animation.stop)
  }

  def activeAnimation() = activeState.animation

  def onStatePressed(action: Action) {
    lastState = activeState
    activeState = if (activeAnimation.canInterrupt) action match {
      case Punch => animations("punch")
      case _ if movements.contains(action) => animations("walking")
      case _ => animations("idle")
    } else if (activeAnimation.isFinished) animations("idle")
      else activeState

    if (lastState.state != activeState.state) Gdx.app.log("Purpucard.onStatePressed", s"${lastState.state} → ${activeState.state}")
  }

  // def idealStatePressed(a: Action) {
    // (a, activeState.state) match {
      // case (_, Idle) => actAccordingly(a)
      // case (AnyMove, Idle) => startWalk()
      // case (AnyMove, AnyMove or MovementTransition) => if (MovementTransition.animation.isFinished) walking() else continueCurrentAnim
      // case _ => idle
    // }
  // }

  // def idealStateReleased(a: Action) {
    // (a, activeState.state) match {

    // }
  // }

  // Real janky bc this only fires if a movement key is current pressed down
  // def walkTransition(lastState: ) = lastState match {
    // case Idle => animations("startwalk")
    // case MovementTransition if(state.animation.isFinished) => animations("walking")
    // case _ => state
  // }

  def computeVelocity(in: Set[Action], state: StateAnimation) = {
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
  def getRect() = new Rectangle(position.x, position.y, getWidth, getHeight)
}
