package com.running.game.screens;

import gameObjects.Player;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;

public class GameMain implements Screen {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private Player player;
	private float playerRadius;
	private Intersector collisionDetector;
	private Game game;
	private float leftBoundary = 0f;
	private float rightBoundary = 480;
	
	/* Use a priority queue to hold obstacles */
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		player = new Player(240f, 110f, 40f);
		System.out.println(player.getR());
		playerRadius = player.getR();
		game = ((Game) Gdx.app.getApplicationListener()); 
		collisionDetector = new Intersector();
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(198/255f, 198/255f, 198/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.draw(player.playerImage, player.getX() - playerRadius, player.getY() - playerRadius);
		batch.end();
		
		Gdx.input.setInputProcessor(player);
		player.updateMotion();
		
		checkBounds();
		
	}
	
	/**
	 * Checks if the player is out of bounds.
	 * Resets player's x-coordinate to keep player within bounds. 
	 */
	public void checkBounds() {
		if (player.getX() - playerRadius < leftBoundary) {
			player.setX(leftBoundary + 2 * playerRadius);
		} 
		if (player.getX() + playerRadius > rightBoundary) {
			player.setX(rightBoundary);
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

	}

}
