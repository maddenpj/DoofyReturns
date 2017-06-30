package com.mygdx.game

import com.badlogic.gdx.{Gdx, InputAdapter}
import com.badlogic.gdx.Input.Keys




object PlayerState {

  sealed trait State {
    val moves = false
  }
  case object Idle extends State
  case object Punching extends State
  case object MovementTransition extends State { override val moves = true }
  case object Moving extends State { override val moves = true }


  case class StateAnimation(state: State, animation: AnimatedSprite)

}
