package com.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hotboxx.GameMain;
import com.runninggame.utilities.AssetLoader;
import com.runninggame.utilities.Config;

/**
 * This screen shows when all matches are over.
 * @author Peter
 *
 */
public class EndScreen implements Screen {
	
	/** Main game object */
	private GameMain game;
	
	/** LibGDX camera for the game. Determines what to display on screen. */
	private OrthographicCamera camera;
	
	private Viewport viewport;
	
	/** Screen width in pixels */
	public static final float SCREEN_WIDTH = Config.SCREEN_WIDTH;
	
	/** Screen height in pixels */
	public static final float SCREEN_HEIGHT = Config.SCREEN_HEIGHT;
	
	/** Responsible for drawing sprites and textures */
	private SpriteBatch batch;
	
	/** Game over image */
	private Sprite gameOverSprite;
	
	/**
	 * Class constructor
	 * @param game: Main game object
	 */
	public EndScreen(GameMain game) {
		this.game = game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(true, SCREEN_WIDTH, SCREEN_HEIGHT);
		viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
		
		batch = new SpriteBatch();
		gameOverSprite = new Sprite(AssetLoader.gameOverTexture);
		gameOverSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	/** Unused */
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	/** Renders game over screen */
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		batch.begin();
		gameOverSprite.draw(batch);
		batch.end();
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			Config.reset();
			game.setScreen(new InstructionsScreen(game));
		}
	}
	
	/**
	 * Resizing method. When the game window is expanded/shrunk.
	 * @param width: New width of screen.
	 * @param height: New height of screen.
	 */
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		
	}

	/** Unused */
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
	/** Unused */
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Called when screen no longer shown
	 */
	@Override
	public void hide() {
		batch.dispose();
		
	}
	
	/** 
	 * Dispose of game elements 
	 */
	@Override
	public void dispose() {
		batch.dispose();
	}

}
