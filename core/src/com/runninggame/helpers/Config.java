package com.runninggame.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

//import org.ho.yaml.Yaml;

public class Config {
	
	public final static float SCALE = 40;
//	public static float playerSpeed;
//	public static float playerWidth;
//	public static float playerHeight;
//	public static float playerX; 
//	public static float playerY;
//	public static float gravity;
//	public static float objectWidth;
//	public static float objectSpeed; 
//	public static float objectSpawnX;
//	public static float normalMean;
//	public static float normalSD;
//	public static long waveTime;
//	public static int rewardValue;
//	public static int penaltyValue;
//	public static int minScore;
//	public static String distribution;
//	public static String gameMode;
//	public static boolean splash;
	
	public static float playerSpeed = 7.3f;
	public static float playerWidth = 32f;
	public static float playerHeight = 32f;
	public static float playerX = 60f; 
	public static float playerY = 240f;
	public static float gravity = 14f;
	public static float objectWidth = 18f;
	public static float objectSpeed = 6f; 
	public static float objectSpawnX = 800f;
	public static float normalMean = 1;
	public static float normalSD = 1;
	public static float waveTime = 480f;
	public static int rewardValue = 1;
	public static int penaltyValue = 1;
	public static int minScore = 0;
	public static String distribution = "uniform";
	public static String gameMode = "penalty";
	public static boolean splash = false;

	public static void load() throws FileNotFoundException {
//		File configFile = new File("config.yml");
//		Object temp = Yaml.load(configFile);
//		Map<?, ?> configMap = (Map<?, ?>) temp;
//
//		playerSpeed = ((Double) configMap.get("playerSpeed")).floatValue();
//		playerWidth = ((Double) configMap.get("playerWidth")).floatValue();
//		playerHeight = ((Double) configMap.get("playerHeight")).floatValue();
//		playerX = ((Double) configMap.get("playerX")).floatValue();
//		playerY = ((Double) configMap.get("playerY")).floatValue();
//		gravity = ((Double) configMap.get("gravity")).floatValue();
//		objectWidth = ((Double) configMap.get("objectWidth")).floatValue();
//		objectSpeed = ((Double) configMap.get("objectSpeed")).floatValue();
//		objectSpawnX = ((Double) configMap.get("objectSpawnX")).floatValue();
//		normalMean = ((Double) configMap.get("normalMean")).floatValue();
//		normalSD = ((Double) configMap.get("normalSD")).floatValue();
//		waveTime = ((Double) configMap.get("waveTime")).longValue(); 
//		rewardValue = ((Integer) configMap.get("rewardValue"));
//		penaltyValue = ((Integer) configMap.get("penaltyValue"));
//		minScore = ((Integer) configMap.get("minScore"));
//		distribution = ((String) configMap.get("distribution"));
//		gameMode = ((String) configMap.get("gameMode"));
//		splash = (boolean) configMap.get("splash");
//		ArrayList<?> arrayTest = (ArrayList<?>) configMap.get("test"); 
//		int x = (int) arrayTest.get(0);
//		System.out.println(x);
	}
}
