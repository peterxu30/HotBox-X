package com.running.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.running.game.gameWorld.GameRenderer;
import com.running.game.gameWorld.GameWorld;

public class GameScreen implements Screen {
	
	private GameWorld world;
	private GameRenderer renderer;
	private final float timeStep = 1/60f;
	
	public GameScreen() {
		world = new GameWorld(98f);
		renderer = new GameRenderer(world, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		world.update(timeStep);
		renderer.render();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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

	}

}
