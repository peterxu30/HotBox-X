package com.running.game.helpers;

import oldCode.Play;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.running.game.gameObjects.Player;
import com.running.game.screens.GameScreen;

public class InputHandler implements InputProcessor {

	private Player player;
	
	public InputHandler(Player player) {
		this.player = player;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
//		Gdx.app.log("InputHandler", "space down");
		switch (keycode) {
		case (Keys.SPACE):
			player.move();
			break;
		case (Keys.Z):
			((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
			break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
