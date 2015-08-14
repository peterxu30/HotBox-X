package com.running.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.running.game.GameMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = GameMain.TITLE + "v" + GameMain.VERSION;
		config.vSyncEnabled = true;
		config.height = 480;
		config.width = 800;
		new LwjglApplication(new GameMain(), config);
	}
}
