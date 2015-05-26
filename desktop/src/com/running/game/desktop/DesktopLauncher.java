package com.running.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.running.game.RunningGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = RunningGame.TITLE + "v" + RunningGame.VERSION;
		config.vSyncEnabled = true;
		config.height = 800;
		config.width = 480;
		new LwjglApplication(new RunningGame(), config);
	}
}
