package com.running.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.running.game.gameworld.GameRenderer;
import com.running.game.gameworld.GameWorld;
import com.running.game.helpers.Config;
import com.running.game.helpers.InputHandler;

public class GameScreen implements Screen {
	private final float SCREEN_WIDTH = 800f;
	private final float SCREEN_HEIGHT = 480f;
	
	private GameWorld world;
	private GameRenderer renderer;
	private final float timeStep = 1/60f;
	
	public GameScreen() {
		world = new GameWorld(Config.gravity);
		renderer = new GameRenderer(world, SCREEN_WIDTH, SCREEN_HEIGHT, Config.SCALE);
		Gdx.input.setInputProcessor(new InputHandler(world.getPlayer()));
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
//		world.update(Math.min(delta, timeStep));
		world.update(timeStep);
//		System.out.println(timeStep);
		renderer.render();
		checkGameStatus();
	}
	
	private void checkGameStatus() {
		if (world.isGameOver()) {
			dispose();
			((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		renderer.resize(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		Gdx.app.log("GameScreen", "Dispose");
		renderer.dispose();
		world.dispose();
	}
}
