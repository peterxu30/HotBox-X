package com.running.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.running.game.gameWorld.GameRenderer;
import com.running.game.gameWorld.GameWorld;
import com.running.game.helpers.InputHandler;

public class GameScreen implements Screen {
	
	private GameWorld world;
	private GameRenderer renderer;
	private final float timeStep = 1/60f;
	private final float meterToPixelScale = 40f;
	
	public GameScreen() {
		world = new GameWorld(9.8f, meterToPixelScale);
		renderer = new GameRenderer(world, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), meterToPixelScale);
		Gdx.input.setInputProcessor(new InputHandler(world.getPlayer()));
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
		renderer.dispose();
	}

}
