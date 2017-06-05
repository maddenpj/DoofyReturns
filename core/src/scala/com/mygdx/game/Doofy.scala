package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture

class Doofy {
  def openGLVersion = Gdx.graphics.getGLVersion.getRendererString

  def foo(t: Texture) {
    Gdx.app.log("ScalaDoofiez", s"${t.getWidth} x ${t.getHeight}")
  }

  def blah() {
    val img = new Texture("sprite1.png")
    // val x = new AnimatedSprite(img, 2,2,0.14f)
    Gdx.app.log("ScalaDoofiez", s"done")
  }
}
