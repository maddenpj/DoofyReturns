package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.math.{Rectangle, Vector2}


object Purpucard {

  sealed trait State
  case object Idle extends State
  case object MovementTransition extends State
  case object Moving extends State
  case object Punching extends State

}



// Also gross to pass in level bounds like this
class Purpucard(atlas: TextureAtlas, levelRect: Rectangle)
    extends Renderable
    with HasPosition
    with PlayerControlled {

  val walkSpeed = 2.5f;
  val spriteScale = 3.0f;
  var activeAnimationName = "idle"
  var state = Purpucard.Idle

  val animations = Map(
    "idle" -> new AnimatedSprite(atlas, "idle", 0.12f, Animation.PlayMode.LOOP_PINGPONG),
    "walking" -> new AnimatedSprite(atlas, "walking", 0.08f),
    "startwalk" -> new AnimatedSprite(atlas, "startwalk", 0.08f, Animation.PlayMode.NORMAL, false),
    "punch" -> new AnimatedSprite(atlas, "punch", 0.08f, Animation.PlayMode.NORMAL, false)
  )
  def activeAnimation() = animations(activeAnimationName)
  // animations("walking").getSprite.setSize(105.0f, 61.0f)
  animations.values.foreach(_.getSprite.setScale(spriteScale))

  val bindings = Map(
    MoveRight -> Input.Keys.RIGHT,
    MoveLeft  -> Input.Keys.LEFT,
    MoveUp    -> Input.Keys.UP,
    MoveDown  -> Input.Keys.DOWN,
    Punch -> Input.Keys.SPACE
  )

  // (a: Set[Action]) => String
  // But now we have Purpucard.State
  def animBindings(a: Set[Action]) = a match {
    case _ if a.contains(Punch) => "punch"
    case _ if a.nonEmpty => "walking"
    case _ => "idle"
  }

  def animState(a: Set[Action]) = a match {
    case _ if a.contains(Punch) => Purpucard.Punching
    case _ if a.nonEmpty => Purpucard.Moving //TODO - dangerous
    case _ => Purpucard.Idle
  }



  // update(in: Set[Action], dt: Float)? might be better
  // but breaks Renderable
  //
  // Thinking out loud, how do games work
  // (Input) -> [Engine layer] -> Player action
  // ie: Just bc moveRight is down doesn't mean player will move (wall, etc)
  //
  def update(dt: Float) {
    // var vel = 0.0f
    val vel = new Vector2

    // This is so gross
    val in = getInput()
    if (activeAnimation.canInterrupt) {
      activeAnimationName = animBindings(in)
      if (in(MoveRight)) vel.x = walkSpeed
      if (in(MoveLeft)) vel.x = -walkSpeed
      if (in(MoveUp)) vel.y = walkSpeed
      if (in(MoveDown)) vel.y = -walkSpeed
    } else {
      if (activeAnimation.isFinished) {
        activeAnimationName = "idle"
      }
    }


    // So this can be abstracted out to be done "later"
    // Or globally ie: for all entities in game
    // aka update() breaks down into
    //   -> handleInput()
    //   -> handleCollisions()
    if (levelRect.contains(vel.cpy.add(getX, getY))) {
      incrPosition(vel.nor.scl(walkSpeed))
    }

    animations.values.foreach(_.setPosition(posX, posY))
    activeAnimation.playAnimation(dt)
    animations.filterNot(_._1 == activeAnimationName).foreach(_._2.stop)
  }

  def handleInput(in: Set[Action]) = {
    val states = (animState(in), animBindings(in))
    Gdx.app.log("Purp's States", states.toString)
  }

  def draw (s: SpriteBatch) = activeAnimation.draw(s)

  def getWidth() = activeAnimation.getSprite.getWidth
  def getHeight() = activeAnimation.getSprite.getHeight
  def getRect() = new Rectangle(getX, getY, getWidth, getHeight)
}
