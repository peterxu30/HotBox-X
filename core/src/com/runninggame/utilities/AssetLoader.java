package com.runninggame.utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * Loads all images that will be used by the game
 * @author Peter
 *
 */
public class AssetLoader {
	
	/** LibGDX class to manage assets */
	public static AssetManager manager = new AssetManager();
	
	/** Instructions screen image */
	public static Texture instructionsTexture;
	
	/** Player texture */
	public static Texture playerTexture;
	
	/** Obstacle texture */
	public static Texture obstacleTexture;
	
	/** Reward texture */
	public static Texture rewardTexture;
	
	/** Boundary texture */
	public static Texture boundaryTexture;
	
	/** Game Over screen image */
	public static Texture gameOverTexture;
	
	/**
	 * Load all the images
	 */
	public static void load() {
		manager.load("img/instructions.png", Texture.class);
		manager.load("img/player.png", Texture.class);
		manager.load("img/obstacle.png", Texture.class);
		manager.load("img/reward.png", Texture.class);
		manager.load("img/boundary.png", Texture.class);
		manager.load("img/gameOver.png", Texture.class);
		
		//can't assign images that aren't loaded
		while (!manager.update()) {
			continue;
		}
		instructionsTexture = manager.get("img/instructions.png", Texture.class);
		playerTexture = manager.get("img/player.png", Texture.class);
		obstacleTexture = manager.get("img/obstacle.png", Texture.class);
		rewardTexture = manager.get("img/reward.png", Texture.class);
		boundaryTexture = manager.get("img/boundary.png", Texture.class);
		gameOverTexture = manager.get("img/gameOver.png", Texture.class);
	}
	
	/**
	 * Load the AssetManager until finished 
	 * @return boolean
	 */
	public static boolean update() {
		return manager.update();
	}
	
	/**
	 * Dispose of a specific image
	 * @param img
	 */
	public static void dispose(String img) {
		manager.unload(img);
	}
	
	/**
	 * Dispose all images
	 */
	public static void dispose() {
		manager.dispose();
	}
	
}
