// package com.mygdx.game.background

// import com.badlogic.gdx.Gdx
// import com.badlogic.gdx.graphics.g2d._
// import com.badlogic.gdx.graphics.OrthographicCamera;
// import com.badlogic.gdx.math.Vector2


// class CenteredBackground(
  // bgTexture: TextureRegion,
  // speed: Vector2) extends Renderable {

  // val camera = new OrthographicCamera()

  // val pl = new ParallaxLayer(
    // bgTexture,
    // new Vector2(1.0f, 0.0f),
    // new Vector2(0, 0)
  // )
  // val background = new ParallaxBackground(
    // Array(pl),
    // Gdx.graphics.getWidth(),
    // Gdx.graphics.getHeight(),
    // speed
  // )

  // def update(dt: Float, player: Purpucard) {
    // val half = Gdx.graphics.getWidth() / 2
    // if (player.getX > half) {
      // camera.position.x = player.getX
      // camera.update
    // }
  // }
// }
