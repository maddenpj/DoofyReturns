package com.mygdx.game.background


import com.badlogic.gdx.Gdx

import com.badlogic.gdx.graphics.g2d.{TextureAtlas, TextureRegion, Sprite, SpriteBatch, Animation => GdxAnimation}
import com.badlogic.gdx.math.Vector2



object Animation {
  import GdxAnimation.PlayMode
  type Mode = PlayMode

  // Other things
  // Interruptible or not
  // Looping or not
  // Reversed or not

  trait State {
    // def elapsed: Float
  }
  case object Stopped extends State
  case object Playing extends State


  trait SpriteAnimation {
    def sprite: Sprite
    def anim: GdxAnimation[TextureRegion]
    def mode: Mode
    def interruptible: Boolean
    def isFinished: Boolean // Should be Option too
    def duration: Option[Float]

    private var _state: State = Stopped
    def state = _state

    private var _elapsed: Float = _
    def elapsed = _elapsed

    def position = new Vector2(sprite.getX, sprite.getY)
    def x = sprite.getX
    def y = sprite.getY

    def setPosition(x: Float, y: Float) = sprite.setPosition(x,y)

    // should be update and inherit Renderable
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

    def render(s: SpriteBatch) {
      sprite.setRegion(anim.getKeyFrame(elapsed))
      sprite.draw(s)
    }

    // Note gdx will return true for looping animations after they've run a whole cycle
    // def isFinished = anim.isAnimationFinished(elapsed)
  }

  // TODO: Maybe some sort of assert that mode is (LOOP || LOOP_PINGPONG)
  class LoopingAnimation(
    val sprite: Sprite,
    val anim: GdxAnimation[TextureRegion],
    val mode: Mode) extends SpriteAnimation {
      val interruptible = false
      val isFinished = false
      val duration = None
  }

  class NormalAnimation(
    val sprite: Sprite,
    val anim: GdxAnimation[TextureRegion],
    val mode: Mode,
    val interruptible: Boolean) extends SpriteAnimation {
      
      def duration = Some(anim.getAnimationDuration)
      def isFinished = anim.isAnimationFinished(elapsed)
  }


  def normal(atlas: TextureAtlas, atlasKey: String, fps: Float, mode: Mode = PlayMode.NORMAL, interruptible: Boolean = true) = {
    val anim = atlasToAnim(atlas, atlasKey, fps, mode)
    new NormalAnimation(atlas.createSprite(atlasKey), anim, mode, interruptible)
  }

  def looping(atlas: TextureAtlas, atlasKey: String, fps: Float, mode: Mode = PlayMode.LOOP) = {
    val anim = atlasToAnim(atlas, atlasKey, fps, mode)
    new LoopingAnimation(atlas.createSprite(atlasKey), anim, mode)
  }

  def atlasToAnim(atlas: TextureAtlas, key: String, fps: Float, mode: Mode) =
    new GdxAnimation[TextureRegion](fps, atlas.findRegions(key), mode)


}
