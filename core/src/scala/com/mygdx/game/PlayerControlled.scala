package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input


trait PlayerControlled {
  def isKeyPressed(k: Int) = Gdx.input.isKeyPressed(k)
}
