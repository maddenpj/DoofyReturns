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
    Input.Keys.UP    -> MoveUp,
    Input.Keys.DOWN  -> MoveDown,
    Input.Keys.SPACE -> Punch
  )

  class InputController(keyBinds: Map[Int, InputAction]) extends InputAdapter {

    val keyStates =
      collection.mutable.Map[InputAction, Boolean]() ++= keyBinds.values.map(_ -> false)

    def getInput() = keyStates.filter(_._2).keys.toSet


    override def keyDown(k: Int): Boolean = {
      if(keyBinds.contains(k)) keyStates(keyBinds(k)) = true
      true
    }

    override def keyUp(k: Int): Boolean = {
      if(keyBinds.contains(k)) keyStates(keyBinds(k)) = false
      true
    }
  }

  val defaultInputProcessor= new InputController(bindings)
}
