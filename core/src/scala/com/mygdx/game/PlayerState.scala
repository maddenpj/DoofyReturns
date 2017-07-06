package com.mygdx.game

import com.badlogic.gdx.{Gdx, InputAdapter}
import com.badlogic.gdx.Input.Keys




object PlayerState {

  sealed trait State {
    val moves = false
  }
  case object Idle extends State
  case object Punching extends State
  // This is weird to me bc.. this isn't a "gameplay" state
  // but it's an inbetween state only relevant to the players animation
  case object MovementTransition extends State { override val moves = true }
  case object Moving extends State { override val moves = true }


  case class StateAnimation(state: State, animation: AnimatedSprite)

}
