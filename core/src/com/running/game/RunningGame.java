package com.running.game;

import java.io.FileNotFoundException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
	private static Music music;
	
	@Override
	public void create() {
		AssetLoader.load();
		try {
			Config.load();
		} catch (FileNotFoundException e) {
			Gdx.app.log("Config Error", "File Missing");
		}
		if (Config.splash) {
			setScreen(new Splash("img/notime.jpg"));
			music = Gdx.audio.newMusic(Gdx.files.internal("sound/notime.mp3"));
			music.play();
			music.setPosition(23f);
			music.setLooping(true);
		} else {
			setScreen(new GameScreen());
		}
//		setScreen(new GameScreen());
		
	}
	
	@Override
	public void dispose() {
		Gdx.app.log("RunningGame", "Dispose");
		super.dispose();
		AssetLoader.dispose();
		
		//for fun
		if (Config.splash) {
			music.dispose();
		}
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
