package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.{Input, InputAdapter}




object PlayerState {

  sealed trait InputAction
  case object MoveLeft extends InputAction
  case object MoveRight extends InputAction
  case object MoveUp extends InputAction
  case object MoveDown extends InputAction
  case object Punch extends InputAction

  val movement = Seq(MoveLeft, MoveRight, MoveDown, MoveUp)

  sealed trait State {
    val moves = false
  }
  case object Idle extends State
  case object Punching extends State
  case object MovementTransition extends State { override val moves = true }
  case object Moving extends State { override val moves = true }


  case class StateAnimation(state: State, animation: AnimatedSprite)


  // InputAdapter methods are called in response to I/O
  // (Int => Boolean) but the result is only relevant to InputMultiplexer
  // So basically they might as well be Int => Unit
  //
  // Ultimately chain we need is
  // Key => Action => Animation
  // getInput()  () => Set[InputAction]
  val bindings = Map(
    Input.Keys.RIGHT -> MoveRight,
    Input.Keys.LEFT -> MoveLeft,
    Input.Keys.UP    -> MoveUp,
    Input.Keys.DOWN  -> MoveDown,
    Input.Keys.SPACE -> Punch
  )

}
