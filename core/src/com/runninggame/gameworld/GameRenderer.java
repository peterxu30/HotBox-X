package com.runninggame.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.runninggame.gameobjects.GameObject;
import com.runninggame.gameobjects.Player;
import com.runninggame.helpers.AssetLoader;

public class GameRenderer {
	
	private GameWorld gameWorld;
	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;
	private float scale;
	private float width;
	private float height;
	private float boundaryX;
	private float groundY;
	private float skyY;
	private Player player;
	
	public GameRenderer(GameWorld gameWorld, float width, float height, float scale) {
		this.gameWorld = gameWorld;
		this.scale = scale;
		this.width = width;
		this.height = height;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(true, width, height);
		viewport = new FitViewport(width, height, camera);
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		player = gameWorld.getPlayer();
		calculations();
	}
	
	private void calculations() {
		boundaryX = gameWorld.getGroundBody().getPosition().x * scale;
		groundY = gameWorld.getGroundBody().getPosition().y * scale;
		skyY = gameWorld.getSkyBody().getPosition().y * scale - 42f;
	}
	
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        drawPlayer();
        drawBoundaries();
        drawObjects();
        System.out.println("Score: " + gameWorld.getScore()); //draw it later
        batch.end();
	}
	
	private void drawPlayer() {
		float playerX = (player.getX() - (player.getWidth() / 2)) * scale;
		float playerY = (player.getY() - (player.getHeight() / 2)) * scale;
		float playerW = player.getWidth() * scale;
		float playerH = player.getHeight() * scale;
		batch.draw(AssetLoader.playerTexture, playerX, playerY, playerW, playerH);
	}
	
	private void drawBoundaries() {
		batch.draw(AssetLoader.obstacleTexture, boundaryX, groundY, width, gameWorld.getBoundaryWidth());
        batch.draw(AssetLoader.obstacleTexture, boundaryX, skyY, width, gameWorld.getBoundaryWidth());
	}
	
	private void drawObjects() {
		for (GameObject obj : gameWorld.getWave()) {
        	float objX = (obj.getBody().getPosition().x - (obj.getWidth() / 2)) * scale;
        	float objY = (obj.getBody().getPosition().y - (obj.getHeight() / 2)) * scale;
        	float objWidth = obj.getWidth() * scale;
        	float objHeight = obj.getHeight() * scale;
        	Texture objTexture;
        	if (obj.getItemID() == 1) {
        		objTexture = AssetLoader.obstacleTexture;
        	} else {
        		objTexture = AssetLoader.rewardTexture;
        	}
        	batch.draw(objTexture, objX, objY, objWidth, objHeight);
        }
	}
	
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	public void dispose() {
		Gdx.app.log("GameRenderer", "Dispose");
		batch.dispose();
	}
}

