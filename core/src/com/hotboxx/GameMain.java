package com.hotboxx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.runninggame.screens.InstructionsScreen;
import com.runninggame.utilities.AssetLoader;
import com.runninggame.utilities.Config;

/**
 * Main game file. This file runs the game.
 * @author Peter Xu
 * 
 */
public class GameMain extends Game {
	/** Game title */
	public static final String TITLE = "HotBox X";
	/** Game version number */
	public static final String VERSION = "8.8.15";
	
	/** Initializes game at start */
	@Override
	public void create() {
		AssetLoader.load();
		Config.login();
		setScreen(new InstructionsScreen(this));
	}
	
	/** Disposes game elements when game closed */
	@Override
	public void dispose() {
		Gdx.app.log("RunningGame", "Dispose");
		super.dispose();
		AssetLoader.dispose();
	}
	
	/** Unused */
	@Override
	public void render() {
		super.render();
	}
	
	/** Unused */
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	
	/** Unused */
	@Override
	public void pause() {
		super.pause();
	}
	
	/** Unused */
	@Override
	public void resume() {
		super.resume();
	}
	
}
