package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.DoofyReturnsGame;


public class DesktopLauncher {
  public static void main (String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();


    // TexturePacker.process("../raw", "pack", "warrior");
    LwjglApplication app = new LwjglApplication(new DoofyReturnsGame(), config);
  }
}
