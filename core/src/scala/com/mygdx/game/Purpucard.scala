package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.math.{Rectangle, Vector2}


object Purpucard {

  sealed trait State {
    val moves = false
  }
  case object Idle extends State
  case object Punching extends State
  case object MovementTransition extends State { override val moves = true }
  case object Moving extends State { override val moves = true }

  case class StateAnimation(state: State, animation: AnimatedSprite)



}



// Also gross to pass in level bounds like this
class Purpucard(atlas: TextureAtlas, levelRect: Rectangle)
    extends Renderable
    with HasPosition
    with PlayerInput {
    import Purpucard._


  val walkSpeed = 2.5f;
  val spriteScale = 3.0f;
  var activeAnimationName = "idle"

  // Coupling these together was a shit idea
  val animations = Map(
    "idle"        -> StateAnimation(Idle, new AnimatedSprite( atlas, "idle", 0.12f, Animation.PlayMode.LOOP_PINGPONG)),
    "walking"     -> StateAnimation(Moving, new AnimatedSprite( atlas, "walking", 0.08f)),
    "startwalk"   -> StateAnimation(MovementTransition, new AnimatedSprite( atlas, "startwalk", 0.08f, Animation.PlayMode.NORMAL, false)),
    "startwalk_r" -> StateAnimation(MovementTransition, new AnimatedSprite( atlas, "startwalk", 0.08f, Animation.PlayMode.REVERSED, false)),
    "punch"       -> StateAnimation(Punching, new AnimatedSprite( atlas, "punch", 0.08f, Animation.PlayMode.NORMAL, false))
  )
  var activeState: StateAnimation = animations("idle")

  val a1 = Set(PlayerInput.MoveUp, PlayerInput.MoveRight, PlayerInput.Punch)
  val a2 = Set(PlayerInput.MoveUp, PlayerInput.MoveRight) 
  val a3 = Set(PlayerInput.Punch)
  val a4 = Set[PlayerInput.Action]()
  Gdx.app.log(s"$a1", "= " + a1.exists(PlayerInput.movement.contains))
  Gdx.app.log(s"$a2", "= " + a2.exists(PlayerInput.movement.contains))
  Gdx.app.log(s"$a3", "= " + a3.exists(PlayerInput.movement.contains))
  Gdx.app.log(s"$a4", "= " + a4.exists(PlayerInput.movement.contains))
  // val animations = Map(
    // "idle" -> new AnimatedSprite(atlas, "idle", 0.12f, Animation.PlayMode.LOOP_PINGPONG),
    // "walking" -> new AnimatedSprite(atlas, "walking", 0.08f),
    // "startwalk" -> new AnimatedSprite(atlas, "startwalk", 0.08f, Animation.PlayMode.NORMAL),
    // "startwalk_r" -> new AnimatedSprite(atlas, "startwalk", 0.08f, Animation.PlayMode.REVERSED),
    // "punch" -> new AnimatedSprite(atlas, "punch", 0.08f, Animation.PlayMode.NORMAL, false)
  // )

  // animations("walking").getSprite.setSize(105.0f, 61.0f)
  animations.values.foreach(_.animation.getSprite.setScale(spriteScale))

  val bindings = Map(
    PlayerInput.MoveRight -> Input.Keys.RIGHT,
    PlayerInput.MoveLeft  -> Input.Keys.LEFT,
    PlayerInput.MoveUp    -> Input.Keys.UP,
    PlayerInput.MoveDown  -> Input.Keys.DOWN,
    PlayerInput.Punch -> Input.Keys.SPACE
  )

  // (a: Set[Action]) => String
  // But now we have Purpucard.State
  // def animBindings(a: Set[Action]) = a match {
    // case _ if a.contains(Punch) => "punch"
    // case _ if a.nonEmpty => "walking"
    // case _ => "idle"
  // }

  def animState(animName: String) = animName match {
    case "punch" => Purpucard.Punching
    case "walking" => Purpucard.Moving //TODO - dangerous
    case _ if animName.contains("startwalk") => Purpucard.MovementTransition
    case "idle" => Purpucard.Idle
  }



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
    // var vel = 0.0f

    // This is so gross
    val in = getInput()
    // if (activeAnimation.canInterrupt) {
      // activeAnimationName = animBindings(in)
      // if (in(MoveRight)) vel.x = walkSpeed
      // if (in(MoveLeft)) vel.x = -walkSpeed
      // if (in(MoveUp)) vel.y = walkSpeed
      // if (in(MoveDown)) vel.y = -walkSpeed
    // } else {
      // if (activeAnimation.isFinished) {
        // activeAnimationName = "idle"
      // }
    // }
    // if (Gdx.input.isKeyPressed(Input.Keys.N)) {
      // activeAnimationName = "startwalk"
    // }
    // if (Gdx.input.isKeyPressed(Input.Keys.M)) {
      // activeAnimationName = "startwalk_r"
    // }

    activeState = stateTransition(in, activeState)
    val vel = computeVelocity(in, activeState)
    Gdx.app.log("alucard", s"${activeState.state} - $in - $vel")

    // So this can be abstracted out to be done "later"
    // Or globally ie: for all entities in game
    // aka update() breaks down into
    //   -> handleInput()
    //   -> handleCollisions()
    if (levelRect.contains(vel.cpy.add(getX, getY))) {
      incrPosition(vel.nor.scl(walkSpeed))
    }

    animations.values.foreach(_.animation.setPosition(posX, posY))
    activeState.animation.playAnimation(dt)
    // animations.filterNot(_._1 == activeAnimationName).foreach(_._2.stop)
  }

  def activeAnimation() = activeState.animation

  def inputAllowed(state: StateAnimation) = state.animation.canInterrupt || state.animation.isFinished

  def stateTransition(in: Set[PlayerInput.Action], state: StateAnimation) = if (!inputAllowed(state)) state
    else if (in.contains(PlayerInput.Punch)) animations("punch")
    else if (in.exists(PlayerInput.movement.contains)) state match {
      case StateAnimation(Idle, _) => animations("startwalk")
      case StateAnimation(MovementTransition, _) => animations("walking")
      // case StateAnimation(Moving, _) => state
      case _ => state
    } else animations("idle")

  def computeVelocity(in: Set[PlayerInput.Action], state: StateAnimation) = {
    val vel = new Vector2();

    if (state.state.moves) {
      if (in(PlayerInput.MoveRight)) vel.x = walkSpeed
      if (in(PlayerInput.MoveLeft)) vel.x = -walkSpeed
      if (in(PlayerInput.MoveUp)) vel.y = walkSpeed
      if (in(PlayerInput.MoveDown)) vel.y = -walkSpeed
    }
    vel
  }

  def draw (s: SpriteBatch) = activeAnimation.draw(s)

  def getWidth() = activeAnimation.getSprite.getWidth
  def getHeight() = activeAnimation.getSprite.getHeight
  def getRect() = new Rectangle(getX, getY, getWidth, getHeight)
}
