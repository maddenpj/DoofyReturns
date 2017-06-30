package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.{InputAdapter}

import PlayerState.InputAction


// Int -> PlayerState.InputAction

trait InputStateListener {
  def hasState(key: Int): Option[InputAction]
  def onStatePressed(action: InputAction): Unit
}

class PlayerInputListener(player: Purpucard, keyBinds: Map[Int, InputAction]) extends InputStateListener {
  def hasState(key: Int) = keyBinds.get(key)

  def onStatePressed(action: InputAction) {
    player.onStatePressed(action)
  }
}


class InputHandler(listener: InputStateListener) extends InputAdapter {

  override def keyDown(k: Int): Boolean = {
    listener.hasState(k).map { action =>
      listener.onStatePressed(action)
    }
    true
  }

  override def keyUp(k: Int): Boolean = {
    true
  }
}
