package com.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.runninggame.RunningGame;
import com.runninggame.helpers.AssetLoader;

public class InstructionsScreen implements Screen {
	private RunningGame game;
	private SpriteBatch batch;
	private Sprite instructionsSprite;
	
	public InstructionsScreen(RunningGame game) {
		this.game = game;
		batch = new SpriteBatch();
//		Texture instructions = //new Texture(Gdx.files.internal("img/instructions.png"));
		instructionsSprite = new Sprite(AssetLoader.instructionsTexture);
		instructionsSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		batch.begin();
		instructionsSprite.draw(batch);
		batch.end();
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			game.setScreen(new GameScreen(game));
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
		AssetLoader.dispose("img/instructions.png");
		batch.dispose();
	}

}
