package com.mygdx.game

import com.badlogic.gdx.graphics.g2d.SpriteBatch


trait Renderable {
  def update(dt: Float): Unit
  def draw(s: SpriteBatch): Unit
}
