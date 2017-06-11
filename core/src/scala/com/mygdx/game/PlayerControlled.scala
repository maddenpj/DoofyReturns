package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input


// This really shouldn't be a trait. Should be an object contained in Player.
// Not through inheritance bc bindings shouldn't be fixed at compile time
trait PlayerControlled {

  sealed trait Action
  case object MoveLeft extends Action
  case object MoveRight extends Action
  case object MoveUp extends Action
  case object MoveDown extends Action
  case object Punch extends Action

  def bindings: Map[Action, Int]
  def getInput() = bindings.filter(x => isKeyPressed(x._2)).keySet
  def isKeyPressed(k: Int) = Gdx.input.isKeyPressed(k)

  // But how to ensure complete match?
  // def bindings2(a: Action) = a match {
    // case MoveRight => Input.Keys.RIGHT
    // case MoveLeft  => Input.Keys.LEFT
    // case MoveUp    => Input.Keys.UP
    // case MoveDown  => Input.Keys.DOWN
    // case Jump => Input.Keys.SPACE
  // }
}


