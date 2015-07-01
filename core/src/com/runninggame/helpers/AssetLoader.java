package com.runninggame.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AssetLoader {
	
	public static Texture playerTexture; 
	public static Texture obstacleTexture;
	public static Texture rewardTexture;
	public static Texture endurance;
	
	public static void load() {
		playerTexture = new Texture(Gdx.files.internal("img/player.png"));
		obstacleTexture = new Texture(Gdx.files.internal("img/obstacle.png"));
		rewardTexture = new Texture(Gdx.files.internal("img/reward.png"));
		endurance = new Texture(Gdx.files.internal("img/endurance.png"));
	}
	
	public static void dispose() {
		playerTexture.dispose();
		obstacleTexture.dispose();
		rewardTexture.dispose();
		endurance.dispose();
	}
}
