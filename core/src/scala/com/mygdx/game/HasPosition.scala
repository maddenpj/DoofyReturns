package com.mygdx.game

 //So disgusting.. Mutable :(

trait HasPosition {
  var posX: Float = 0.0f
  var posY: Float = 0.0f

  def setPosition(x: Float, y: Float) {
    posX = x
    posY = y
  }

  def incrPosition(dx: Float = 0.0f, dy: Float = 0.0f) {
    posX += dx
    posY += dy
  }

  def getX() = posX
  def getY() = posY

}
