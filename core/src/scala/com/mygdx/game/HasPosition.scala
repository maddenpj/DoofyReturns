package com.mygdx.game

import com.badlogic.gdx.math.Vector2

 //So disgusting.. Mutable :(
trait HasPosition {
  var posX: Float = 0.0f
  var posY: Float = 0.0f

  def setPosition(x: Float, y: Float) {
    posX = x
    posY = y
  }

  def incrPosition(vel: Vector2) {
    posX += vel.x
    posY += vel.y
  }

  def incrPosition(dx: Float = 0.0f, dy: Float = 0.0f) {
    posX += dx
    posY += dy
  }

  def getX() = posX
  def getY() = posY

}
