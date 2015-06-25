package com.running.game.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import org.ho.yaml.Yaml;

public class Config {
	
	public static float scale;
	public static float playerSpeed; 
	public static float playerX; 
	public static float playerY; 
	public static float objectWidth;
	public static float objectSpeed; 
	public static float objectSpawnX;
	public static float normalMean;
	public static float normalSD;
	public static long waveTime;
	public static int rewardValue;
	public static String distribution;
	public static String gameMode;

	public static void load() throws FileNotFoundException {
		File configFile = new File("config.yml");
		Object temp = Yaml.load(configFile);
		Map configMap = (Map) temp;

		scale = ((Double) configMap.get("scale")).floatValue();
		System.out.println(scale);
		playerSpeed = ((Double) configMap.get("playerSpeed")).floatValue();
		playerX = ((Double) configMap.get("playerX")).floatValue();
		playerY = ((Double) configMap.get("playerY")).floatValue();
		objectWidth = ((Double) configMap.get("objectWidth")).floatValue();
		objectSpeed = ((Double) configMap.get("objectSpeed")).floatValue();
		objectSpawnX = ((Double) configMap.get("objectSpawnX")).floatValue();
		normalMean = ((Double) configMap.get("normalMean")).floatValue();
		normalSD = ((Double) configMap.get("normalSD")).floatValue();
		waveTime = ((Double) configMap.get("waveTime")).longValue(); 
		rewardValue = ((Integer) configMap.get("rewardValue"));
		distribution = ((String) configMap.get("distribution"));
		gameMode = ((String) configMap.get("gameMode"));
		
//		obstacleAccelerationBoolean = ((Boolean) configMap.get("obstacleAccelerationBoolean"));
//		obstacleAccelerationRate = ((Double) configMap.get("obstacleAccelerationRate")).floatValue();
//		minObstacleWidth = ((Double) configMap.get("minimumObstacleWidth")).floatValue();
//		zonedSpeed = (screenWidth - (2 * leftBoundary)) / 3;
//		leftBoundary = ((Double) configMap.get("boundaryWidth")).floatValue();
//		rightBoundary = screenWidth - leftBoundary;
//		playerWidth = ((Double) configMap.get("playerWidth")).floatValue();
//		playerHeight = ((Double) configMap.get("playerHeight")).floatValue();
//		zoned = ((boolean) configMap.get("zoned"));
//		scoreTime = ((Double) configMap.get("scoreTime")).longValue();
	}
	
}
