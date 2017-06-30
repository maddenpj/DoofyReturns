package com.mygdx.game

import com.badlogic.gdx.{Gdx, InputAdapter}
import com.badlogic.gdx.Input.Keys




object PlayerInput {
  type KeyCode = Int

  sealed trait Action
  case object MoveLeft extends Action
  case object MoveRight extends Action
  case object MoveUp extends Action
  case object MoveDown extends Action
  case object Punch extends Action

  val movements = Seq(MoveLeft, MoveRight, MoveDown, MoveUp)

  val bindings = Map(
    Keys.RIGHT -> MoveRight,
    Keys.LEFT -> MoveLeft,
    Keys.UP    -> MoveUp,
    Keys.DOWN  -> MoveDown,
    Keys.SPACE -> Punch
  )


  class KeyboardAdapter(player: Purpucard, bindings: Map[KeyCode, Action]) extends InputAdapter {

    override def keyDown(k: KeyCode): Boolean = {
      bindings.get(k).map(player.onStatePressed(_))
      true
    }

    override def keyUp(k: KeyCode): Boolean = {
      true
    }
  }

  def defaultAdapter(player: Purpucard) = new KeyboardAdapter(player, bindings)

}
