package com.mygdx.game.background


import com.badlogic.gdx.Gdx

import com.badlogic.gdx.graphics.g2d.{TextureAtlas, TextureRegion, Sprite, SpriteBatch, Animation => GdxAnimation}
import com.badlogic.gdx.math.Vector2

import com.mygdx.game.HasPosition



object Animation {
  import GdxAnimation.PlayMode
  type Mode = PlayMode

  trait State
  case object Stopped extends State
  case object Playing extends State


  trait SpriteAnimation extends HasPosition {
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

    // should be update and inherit Renderable
    // More thoughts: I wonder if play(dt) and stop(dt) only worry about state
    // and the animation has an update method that updates anim based on State
    def play(dt: Float) {
      // Hmm is this the best way to ensure the underlying Sprites (x,y) always mirrors player?
      sprite.setPosition(position.x, position.y)

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
  }

  // TODO: Maybe some sort of assert that mode is (LOOP || LOOP_PINGPONG)
  class LoopingAnimation(
    val sprite: Sprite,
    val anim: GdxAnimation[TextureRegion],
    val mode: Mode) extends SpriteAnimation {
      val interruptible = false
      val isFinished = false // Note gdx isAnimationFinished returns true for looping anims
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
