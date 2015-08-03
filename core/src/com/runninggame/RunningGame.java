package com.runninggame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.runninggame.helpers.AssetLoader;
import com.runninggame.helpers.Config;
import com.runninggame.helpers.DataPoster;
import com.runninggame.screens.InstructionsScreen;

public class RunningGame extends Game {
	
	public static final String TITLE = "Running Game";
	public static final String VERSION = "6.29.15";
	
	@Override
	public void create() {
		AssetLoader.load();
		Config.loadJson();
		setScreen(new InstructionsScreen(this));
	}
	
	@Override
	public void dispose() {
		Gdx.app.log("RunningGame", "Dispose");
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
