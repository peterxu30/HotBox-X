package com.running.game.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.running.game.gameobjects.GameObject;
import com.running.game.gameobjects.Player;
import com.running.game.helpers.AssetLoader;
import com.running.game.helpers.Config;

public class GameRenderer {
	
	private GameWorld gameWorld;
	private OrthographicCamera camera;
	private Viewport viewport;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private float scale;
	private float width;
	private float height;
	private Player player;
	private Texture playerTexture;
	
	private float boundaryX;
	private float groundY;
	private float skyY;
	
	public GameRenderer(GameWorld gameWorld, float width, float height, float scale) {
		this.gameWorld = gameWorld;
		this.scale = scale;
		this.width = width;
		this.height = height;
		camera = new OrthographicCamera();
		camera.setToOrtho(true, width, height);
		viewport = new FitViewport(width, height, camera);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
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
//		Gdx.app.log("GameRenderer", "render");
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        playerTexture = AssetLoader.playerTexture;
        // Just for fun
        if (Config.splash) {
        	playerTexture = AssetLoader.endurance;
        }
        
        batch.begin();
        
        drawPlayer();
        drawBoundaries();
        drawObjects();
        
        System.out.println("Score: " + gameWorld.getScore());
       
        batch.end();
	}
	
	private void drawPlayer() {
		batch.draw(playerTexture, 
        		(player.getX() - (player.getWidth() / 2)) * scale,
        		(player.getY() - (player.getHeight() / 2)) * scale,
        		Config.playerWidth,
        		Config.playerHeight);
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
	
	//NOT FINISHED
	public void dispose() {
		Gdx.app.log("GameRenderer", "Dispose");
		if (batch != null) {
			batch.dispose();
		}
	}
	
}
