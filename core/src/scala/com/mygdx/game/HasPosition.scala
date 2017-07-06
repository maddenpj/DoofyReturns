package com.mygdx.game

import com.badlogic.gdx.math.Vector2

trait HasPosition {
  // Hmmmm does this make sense?
  // could it just be public
  protected val _position = new Vector2

  def setPosition(x: Float, y: Float) = _position.set(x, y)
  def setPosition(v: Vector2) = _position.set(v)

  def position = _position.cpy

}
