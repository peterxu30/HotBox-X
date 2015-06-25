package com.running.game;

import java.io.FileNotFoundException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.running.game.helpers.AssetLoader;
import com.running.game.helpers.Config;
import com.running.game.screens.GameScreen;
import com.running.game.screens.Splash;

public class RunningGame extends Game {
	public static final String TITLE = "Running Game", VERSION = "0.0.0.0.1";
	
	
	@Override
	public void create() {
//		setScreen(new Splash());
		AssetLoader.load();
		try {
			Config.load();
		} catch (FileNotFoundException e) {
			Gdx.app.log("Config Error", "File Missing");
		}
		setScreen(new GameScreen());
	}
	
	@Override
	public void dispose() {
		super.dispose();
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
