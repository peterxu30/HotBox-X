package com.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.runninggame.RunningGame;
import com.runninggame.gameworld.GameRenderer;
import com.runninggame.gameworld.GameWorld;
import com.runninggame.helpers.Config;
import com.runninggame.helpers.DataPoster;
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
		System.out.println("new game");
		this.game = game;
		Config.load();
		world = new GameWorld(Config.gravity, Config.SCALE, Config.waveStart, Config.gameMode);
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
			world.update(TIME_STEP);
			renderer.render(delta);
			checkGameStatus();
		}
	}
	
	private void checkGameStatus() {
		if (world.isGameOver()) {
			if (Config.gamesOver()) {
				DataPoster.sendData();
				game.setScreen(new EndScreen(game));
			} else {
				DataPoster.newRound(Config.getCurrentGame() - 1);
				game.setScreen(new GameScreen(game));
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		renderer.resize(width, height);
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	@Override
	public void hide() {
		Gdx.app.log("GameScreen", "Hide");
		dispose();
	}

	@Override
	public void dispose() {
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
