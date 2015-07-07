package com.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.runninggame.RunningGame;
import com.runninggame.gameworld.GameRenderer;
import com.runninggame.gameworld.GameWorld;
import com.runninggame.helpers.Config;
import com.runninggame.helpers.InputHandler;

public class GameScreen implements Screen {
	private final float SCREEN_WIDTH = 800f;
	private final float SCREEN_HEIGHT = 480f;
	private final float TIME_STEP = 1/60f;
	private boolean paused;
	private RunningGame game;
	private GameWorld world;
	private GameRenderer renderer;
	
	public GameScreen(RunningGame game) {
		this.game = game;
		world = new GameWorld(Config.gravity, Config.SCALE, Config.waveTime, Config.gameMode);
		renderer = new GameRenderer(world, SCREEN_WIDTH, SCREEN_HEIGHT, Config.SCALE);
		Gdx.input.setInputProcessor(new InputHandler(this));
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(float delta) {
//		world.update(Math.min(delta, timeStep));
//		world.update(delta);
		if (!paused) {
			System.out.println(1/delta);
			world.update(TIME_STEP);
			renderer.render(delta);
			checkGameStatus();
		}
	}
	
	private void checkGameStatus() {
		if (world.isGameOver()) {
			game.setScreen(new GameScreen(game));
		}
	}

	@Override
	public void resize(int width, int height) {
		renderer.resize(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		paused = true;
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
//		timeStep = TIME_STEP;
		paused = false;

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		Gdx.app.log("GameScreen", "Hide");
		dispose();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		pause();
		Gdx.app.log("GameScreen", "Dispose");
		renderer.dispose();
		world.dispose();
	}
	
	public GameWorld getWorld() {
		return world;
	}
	
	public boolean isPaused() {
		return paused;
	}
}
