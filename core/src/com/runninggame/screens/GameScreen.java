package com.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.hotboxx.GameMain;
import com.runninggame.gameworld.GameRenderer;
import com.runninggame.gameworld.GameWorld;
import com.runninggame.utilities.Config;
import com.runninggame.utilities.DataPoster;
import com.runninggame.utilities.InputHandler;

/**
 * GameScreen is the screen where the game is played.
 * @author Peter
 *
 */
public class GameScreen implements Screen {
	/** Screen width in pixels */
	private final float SCREEN_WIDTH = Config.SCREEN_WIDTH;
	
	/** Screen height in pixels */
	private final float SCREEN_HEIGHT = Config.SCREEN_HEIGHT;
	
	/** Time interval between game engine updates */
	private final float TIME_STEP = 1/60f;
	
	/** Is game paused */
	private boolean paused;
	
	/** GameMain object */
	private GameMain game;
	
	/** GameWorld object */
	private GameWorld world;
	
	/** GameRenderer object, renders game */
	private GameRenderer renderer;
	
	/**
	 * Class constructor
	 * @param game: GameMain object
	 */
	public GameScreen(GameMain game) {
		this.game = game;
		Config.load();
		world = new GameWorld(Config.gravity, Config.SCALE, Config.waveStart, Config.gameMode);
		renderer = new GameRenderer(world, SCREEN_WIDTH, SCREEN_HEIGHT, Config.SCALE);
		Gdx.input.setInputProcessor(new InputHandler(this));
	}
	
	/** Unused */
	@Override
	public void show() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Calls the gameWorld object to update game engine.
	 * Calls gameRenderer object to draw the updated game
	 */
	@Override
	public void render(float delta) {
		if (!paused) {
			world.update(TIME_STEP);
			renderer.render(delta);
			checkGameStatus();
		}
	}
	
	/**
	 * Checks if game is over and responds accordingly
	 */
	private void checkGameStatus() {
		if (world.isGameOver()) {
			DataPoster.newRound(Config.getCurrentGame());
			if (Config.gamesOver()) {
				DataPoster.sendData();
				game.setScreen(new EndScreen(game));
			} else {
				game.setScreen(new GameScreen(game));
			}
		}
	}
	
	/** 
	 * Resize the game if necessary 
	 */
	@Override
	public void resize(int width, int height) {
		renderer.resize(width, height);
	}
	
	/** 
	 * Pause the game 
	 */
	@Override
	public void pause() {
		paused = true;
	}
	
	/** 
	 * Resume the game 
	 */
	@Override
	public void resume() {
		paused = false;
	}

	/** 
	 * Called when screen is no longer shown 
	 */
	@Override
	public void hide() {
		Gdx.app.log("GameScreen", "Hide");
		dispose();
	}

	/**
	 * Dispose of game elements
	 */
	@Override
	public void dispose() {
		pause();
		Gdx.app.log("GameScreen", "Dispose");
		renderer.dispose();
		world.dispose();
	}
	
	/**
	 * Get gameWorld object
	 * @return gameWorld object
	 */
	public GameWorld getWorld() {
		return world;
	}
	
	/**
	 * Check if game is paused
	 * @return boolean
	 */
	public boolean isPaused() {
		return paused;
	}
	
}
