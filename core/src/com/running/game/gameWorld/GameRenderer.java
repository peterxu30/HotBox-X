package com.running.game.gameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.running.game.gameObjects.Player;
import com.running.game.helpers.AssetLoader;

public class GameRenderer {

	private GameWorld world;
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	
	private Player player;
	
	public GameRenderer(GameWorld world, float width, float height) {
		this.world = world;
		camera = new OrthographicCamera();
		camera.setToOrtho(true, width, height);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		player = world.getPlayer();
	}
	
	public void render() {
		Gdx.app.log("GameRenderer", "render");
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        batch.draw(AssetLoader.playerTexture, player.getX(), player.getY());
        batch.end();
	}
	
}
