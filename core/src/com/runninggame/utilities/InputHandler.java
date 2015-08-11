package com.runninggame.utilities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.runninggame.gameobjects.Player;
import com.runninggame.screens.GameScreen;

/**
 * Handles player controls
 * @author Peter
 *
 */
public class InputHandler implements InputProcessor {

	/** GameScreen object */
	private GameScreen screen;
	
	/** Player object */
	private Player player;
	
	/**
	 * Class constructor
	 * @param screen: Current gameScreen
	 */
	public InputHandler(GameScreen screen) {
		this.screen = screen;
		this.player = screen.getWorld().getPlayer();
	}
	
	/**
	 * Called at every key press
	 */
	@Override
	public boolean keyDown(int keycode) {
		Gdx.app.log("InputHandler", "space down");
		switch (keycode) {
			case (Keys.SPACE):
				player.move();
				DataPoster.hasJumped(player.getY(), screen.getWorld().getScore());
				break;
			case (Keys.P):
				if (screen.isPaused()) {
					screen.resume();
				} else {
					screen.pause();
				}
		}
		return false;
	}
	
	/** Unused */
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	/** Unused */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	/** Unused */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	/** Unused */
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	/** Unused */
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	/** Unused */
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
