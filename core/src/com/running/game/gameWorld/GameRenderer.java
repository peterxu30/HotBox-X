package com.running.game.gameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.running.game.gameObjects.GameObject;
import com.running.game.gameObjects.Player;
import com.running.game.helpers.AssetLoader;

public class GameRenderer {

	private GameWorld world;
	private OrthographicCamera camera;
	private Viewport viewport;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private float scale;
	
	private Player player;
	
	private Box2DDebugRenderer debugRenderer;
	
	public GameRenderer(GameWorld world, float width, float height, float scale) {
		this.world = world;
		this.scale = scale;
		camera = new OrthographicCamera();
		camera.setToOrtho(true, width, height);
		viewport = new FitViewport(width, height, camera);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		player = world.getPlayer();
	}
	
	public void render() {
//		Gdx.app.log("GameRenderer", "render");
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        batch.draw(AssetLoader.playerTexture, 
        		(player.getX() - (player.getWidth() / 2)) * scale,
        		(player.getY() - (player.getHeight() / 2)) * scale);
        
//        batch.draw(AssetLoader.playerTexture, 
//        		player.getX() * scale - 20f,
//        		player.getY() * scale - 20f);
        
        batch.draw(AssetLoader.obstacleTexture, world.getGroundBody().getPosition().x * scale,
        		world.getGroundBody().getPosition().y * scale, 800f, 40f);
        
        batch.draw(AssetLoader.obstacleTexture, world.getSkyBody().getPosition().x * scale,
        		world.getSkyBody().getPosition().y * scale - 40f, 800f, 40f);

        renderObjects();
        
        System.out.println("Score: " + world.getScore());
        
//        debugRenderer.render(world.getWorld(), camera.combined);
       
        batch.end();
	}
	
	private void renderObjects() {
		for (GameObject obj : world.getWave()) {
        	float objX = (obj.getBody().getPosition().x - (obj.getWidth() / 2)) * scale;
        	float objY = (obj.getBody().getPosition().y - (obj.getHeight() / 2)) * scale;
        	float objWidth = obj.getWidth() * scale;
        	float objHeight = obj.getHeight() * scale;
        	Texture objTexture = AssetLoader.obstacleTexture;
        	if (obj.getItemID() == 2) {
        		objTexture = AssetLoader.rewardTexture;
        	}
        	batch.draw(objTexture, objX, objY, objWidth, objHeight);
        }
	}
	
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	public void dispose() {
		AssetLoader.dispose();
		batch.dispose();
	}
	
}
