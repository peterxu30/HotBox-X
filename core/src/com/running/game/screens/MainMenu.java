package com.running.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Main menu screen for video game. Plan to implement buttons to lead to game start,
 * how to play, and options.
 * Work in progress.
 * @author Peter
 *
 */
public class MainMenu implements Screen {

	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private TextButton buttonPlay, buttonExit;
	private BitmapFont text, black;
	private Label heading;
	private SpriteBatch batch;
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
//		atlas = new TextureAtlas("ui/button.pack");
//		skin = new Skin(atlas);
		batch = new SpriteBatch();
		text = new BitmapFont();
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		batch.begin();
		text.draw(batch, "Test String", 240, 300);
		batch.end();
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
