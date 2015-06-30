package com.running.game;

import java.io.FileNotFoundException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.running.game.helpers.AssetLoader;
import com.running.game.helpers.Config;
import com.running.game.screens.GameScreen;
import com.running.game.screens.Splash;

public class RunningGame extends Game {
	
	public static final String TITLE = "Running Game";
	public static final String VERSION = "6.29.15";
	private Screen currentScreen;
	
	@Override
	public void create() {
		AssetLoader.load();
		try {
			Config.load();
		} catch (FileNotFoundException e) {
			Gdx.app.log("Config Error", "File Missing");
		}
		currentScreen = new GameScreen();
		setScreen(currentScreen);
	}
	
	@Override
	public void dispose() {
		Gdx.app.log("RunningGame", "Dispose");
//		currentScreen.dispose();
		super.dispose();
		AssetLoader.dispose();
	}
	
	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	
	@Override
	public void pause() {
		super.pause();
	}
	
	@Override
	public void resume() {
		super.resume();
	}
}
