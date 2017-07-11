package com.mygdx.game.screen;

import com.badlogic.gdx.maps.tiled._;
import com.badlogic.gdx.{ApplicationAdapter, Gdx, Input, InputProcessor, ScreenAdapter};
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera, Texture};
import com.badlogic.gdx.graphics.g2d._;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.math.Rectangle;

import com.mygdx.game.GameApplication
import com.mygdx.game._;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Matrix4;

class TiledScreen(game: GameApplication) extends ScreenAdapter{

  val level = "levels/level1.tmx";
  var map : TiledMap = new TmxMapLoader().load(level);
  var renderer : OrthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);

  val camera = new OrthographicCamera;
  val font = new BitmapFont;


  //Using previously created spritebatch
  val batch = game.batch;

  //declare height/width vars
  val w = Gdx.graphics.getWidth();
  val h = Gdx.graphics.getHeight();

  Gdx.app.log("width,height:",w.toString + " " + h.toString)

  //setup camera
  camera.setToOrtho(false, (w / h) * 10, 10);
  camera.update();

  //init purp

  val levelRect = new Rectangle(0, 0, 640, 480);
  val atlas = new TextureAtlas(Gdx.files.internal("pack/alucard.atlas"));

  val purp = new Purpucard(atlas, levelRect);
  purp.setPosition(80.0f, 150.0f);

  Gdx.input.setInputProcessor(PlayerInput.defaultAdapter(purp));

  //disable for now
  val enableNextGenAnims = false;
  val tp = new TestPlayer(atlas);

  //shaders
  val vertexShader = "attribute vec4 a_position;    \n" + 
                      "attribute vec4 a_color;\n" +
                      "attribute vec2 a_texCoord0;\n" + 
                      "uniform mat4 u_projTrans;\n" + 
                      "varying vec4 v_color;" + 
                      "varying vec2 v_texCoords;" + 
                      "void main()                  \n" + 
                      "{                            \n" + 
                      "   v_color = vec4(1, 1, 1, 1); \n" + 
                      "   v_texCoords = a_texCoord0; \n" + 
                      "   gl_Position =  u_projTrans * a_position;  \n"      + 
                      "}                            \n" ;

  val fragmentShader = "#ifdef GL_ES\n" +
                        "precision mediump float;\n" + 
                        "#endif\n" + 
                        "varying vec4 v_color;\n" + 
                        "varying vec2 v_texCoords;\n" + 
                        "uniform sampler2D u_texture;\n" + 
                        "void main()                                  \n" + 
                        "{                                            \n" + 
                        "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" +
                        "}";

  var shader = new ShaderProgram(vertexShader, fragmentShader);

  var mesh = new Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));
  mesh.setVertices(Array[Float](-0.5f, -0.5f, 0, 1, 1, 1, 1, 0, 1,
  0.5f, -0.5f, 0, 1, 1, 1, 1, 1, 1,
  0.5f, 0.5f, 0, 1, 1, 1, 1, 1, 0,
  -0.5f, 0.5f, 0, 1, 1, 1, 1, 0, 0));
  mesh.setIndices(Array[Short](0, 1, 2, 2, 3, 0));
  var texture = new Texture(Gdx.files.internal("levels/images/ZsPH5nFNESJyZ1IyEnEuMvKPryyoMeu2LuWu58PxVBo.png")); 
  var matrix = new Matrix4();


  def update(dt: Float) {

    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
      Gdx.app.exit

    purp.update(dt);

    if (enableNextGenAnims) tp.update(dt);

  }

  override def render (dt: Float) {
    update(dt);
    Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    camera.update();
    renderer.setView(camera);
    renderer.render();
    batch.begin();

    purp.draw(batch);

    if(enableNextGenAnims) tp.draw(batch);

    font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);

    batch.end();

texture.bind();
shader.begin();
shader.setUniformMatrix("u_projTrans", matrix);
shader.setUniformi("u_texture", 0);
mesh.render(shader, GL20.GL_TRIANGLES);
shader.end();

  }

  override def dispose () {
    map.dispose();
  }

}
