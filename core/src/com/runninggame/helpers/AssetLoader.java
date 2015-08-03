package com.runninggame.helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class AssetLoader {
	
	public static AssetManager manager = new AssetManager();
	public static Texture instructionsTexture;
	public static Texture playerTexture; 
	public static Texture obstacleTexture;
	public static Texture rewardTexture;
	public static Texture boundaryTexture;
	public static Texture gameOverTexture;
	
	public static void load() {
		manager.load("img/instructions.png", Texture.class);
		manager.load("img/player.png", Texture.class);
		manager.load("img/obstacle.png", Texture.class);
		manager.load("img/reward.png", Texture.class);
		manager.load("img/boundary.png", Texture.class);
		manager.load("img/gameOver.png", Texture.class);
		
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
	
	public static boolean update() {
		return manager.update();
	}
	
	public static void dispose(String img) {
		manager.unload(img);
	}
	
	public static void dispose() {
		manager.dispose();
	}
}
