package com.running.game.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import org.ho.yaml.Yaml;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class AssetLoader {
	
	public static Texture playerTexture, obstacleTexture, rewardTexture; 
	private static Map configMap;
	
	public static void load() {
		playerTexture = new Texture(Gdx.files.internal("img/player.png"));
		obstacleTexture = new Texture(Gdx.files.internal("img/obstacle.png"));
		rewardTexture = new Texture(Gdx.files.internal("img/reward.png"));
	}
	
	private static void readConfig() throws FileNotFoundException {
		File configFile = new File("config.yml");
		Object temp = Yaml.load(configFile);
		configMap = (Map) temp;
		
//		leftBoundary = ((Double) configMap.get("boundaryWidth")).floatValue();
//		rightBoundary = screenWidth - leftBoundary;
//		playerWidth = ((Double) configMap.get("playerWidth")).floatValue();
//		playerHeight = ((Double) configMap.get("playerHeight")).floatValue();
//		playerSpeed = ((Double) configMap.get("playerSpeed")).floatValue();
//		zonedSpeed = (screenWidth - (2 * leftBoundary)) / 3;
//		startingX = screenWidth/2 - playerWidth/2;
//		startingY = ((Double) configMap.get("startingY")).floatValue();
//		obstacleDistribution = ((String) configMap.get("obstacleDistribution"));
//		minObstacleWidth = ((Double) configMap.get("minimumObstacleWidth")).floatValue();
//		obstacleSpeed = ((Double) configMap.get("obstacleSpeed")).floatValue();
//		obstacleSpawnHeight = ((Double) configMap.get("obstacleSpawnHeight")).floatValue();
//		obstacleAccelerationBoolean = ((Boolean) configMap.get("obstacleAccelerationBoolean"));
//		obstacleAccelerationRate = ((Double) configMap.get("obstacleAccelerationRate")).floatValue();
//		waveTime = ((Double) configMap.get("waveTime")).longValue(); 
//		rewardValue = ((Integer) configMap.get("rewardValue"));
//		gameMode = ((String) configMap.get("gameMode"));
//		zoned = ((boolean) configMap.get("zoned"));
//		scoreTime = ((Double) configMap.get("scoreTime")).longValue();
	}
	
	public static void dispose() {
		playerTexture.dispose();
		obstacleTexture.dispose();
//		rewardTexture.dispose();
	}
	
}
