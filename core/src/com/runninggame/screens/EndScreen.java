package com.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.runninggame.RunningGame;
import com.runninggame.helpers.AssetLoader;
import com.runninggame.helpers.Config;

public class EndScreen implements Screen {

	private RunningGame game;
	private SpriteBatch batch;
	private Sprite gameOverSprite;
	
	public EndScreen(RunningGame game) {
		this.game = game;
		batch = new SpriteBatch();
		gameOverSprite = new Sprite(AssetLoader.gameOverTexture);
		gameOverSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

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
//		AssetLoader.dispose("img/gameOver.png");
		batch.dispose();
	}

}
