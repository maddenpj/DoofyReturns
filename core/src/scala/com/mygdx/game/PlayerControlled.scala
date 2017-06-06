package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input


trait PlayerControlled {

  val bindings = Map(
    "moveRight" -> Input.Keys.RIGHT,
    "moveLeft"  -> Input.Keys.LEFT,
    "moveUp"    -> Input.Keys.UP,
    "moveDown"  -> Input.Keys.DOWN,
    "punch"     -> Input.Keys.SPACE
  )

  // could be very bad in terms of mutablity
  def getInput = bindings.map { case (k,v) =>
    k -> isKeyPressed(v)
  }

  def isKeyPressed(k: Int) = Gdx.input.isKeyPressed(k)
}


