package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input


// This really shouldn't be a trait. Should be an object contained in Player.
// Not through inheritance bc bindings shouldn't be fixed at compile time
trait PlayerControlled {

  val bindings = Map(
    "moveRight" -> Input.Keys.RIGHT,
    "moveLeft"  -> Input.Keys.LEFT,
    "moveUp"    -> Input.Keys.UP,
    "moveDown"  -> Input.Keys.DOWN,
    "punch"     -> Input.Keys.SPACE
  )

  def getInput() = bindings.filter(x => isKeyPressed(x._2)).keySet
  def isKeyPressed(k: Int) = Gdx.input.isKeyPressed(k)
}


