package com.mygdx.game.background


import com.badlogic.gdx.Gdx

import com.badlogic.gdx.graphics.g2d.{TextureRegion, Sprite, Animation => GdxAnimation}
import com.badlogic.gdx.math.{Rectangle, Vector2}



object Animation {
  type Mode = GdxAnimation.PlayMode

  // Other things
  // Interruptible or not
  // Looping or not
  // Reversed or not

  trait State {
    // def elapsed: Float
  }
  case object Stopped extends State
  case object Playing extends State
  case object Finished extends State



  trait Properties {
    def interruptible: Boolean
    def mode: Mode
    def fps: Float
  }

  trait SpriteAnimation {
    def sprite: Sprite
    def anim: GdxAnimation[TextureRegion]
    def props: Properties

    private var _state: State = Stopped
    def state = _state

    private var _elapsed: Float = _
    def elapsed = _elapsed

    def duration = anim.getAnimationDuration

    def position = new Vector2(sprite.getX, sprite.getY)
    def x = sprite.getX
    def y = sprite.getY

    def setPosition(x: Float, y: Float) = sprite.setPosition(x,y)

    def play(dt: Float) {
      if (state == Stopped) {
        _elapsed = 0.0f
        _state = Playing
      }
      _elapsed += dt
    }

    def stop() {
      _state = Stopped
    }

    def isFinished = anim.isAnimationFinished(elapsed)

  }


}
