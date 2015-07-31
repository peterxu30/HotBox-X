package com.runninggame.helpers;

import java.util.ArrayList;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.TimeUtils;
import com.runninggame.gameobjects.GameObject;

public class DataPoster {
	
	private static final String DATA_URL = Config.DATA_URL;
	private static final float SCALE = Config.SCALE;
	private static boolean jumped;
	private static boolean newWave;
	private static boolean obstacleCollision;
	private static boolean rewarded;
	private static int gameNumber;
	private static long time;
	
	private static final String id = UUID.randomUUID().toString();
	
	private static Data currentData;
	private static ArrayList<Data> allData = new ArrayList<Data>();
	
	public static void initialize() {
		time = TimeUtils.millis();
		currentData = new Data();
		currentData.id = id;
		currentData.game = gameNumber;
	}
	
	public static void newRound(int game) {
		gameNumber = game;
		allData.add(currentData);
		currentData = new Data();
		currentData.id = id;
		currentData.game = gameNumber;
		time = TimeUtils.millis();
	}
	
	public static void createdWave(float playerY, ArrayList<GameObject> wave, int score) {
		newWave = true;
		createData(playerY, wave, score);
	}
	
	public static void hasCollided(float playerY, int score, boolean rewarded) {
		obstacleCollision = !rewarded;
		DataPoster.rewarded = rewarded;
		createData(playerY, null, score);
	}
	
	public static void hasJumped(float playerY, int score) {
		jumped = true;
		createData(playerY, null, score);
	}
	
	public static void createData(float playerY, ArrayList<GameObject> wave, int score) {
		DataList dataList = new DataList();
		
		dataList.timeStamp = (TimeUtils.millis() - time) / 1000.0;
		dataList.waveSpawned = newWave;
		dataList.pressedSpace = jumped;
		dataList.obstacleCollision = obstacleCollision;
		dataList.rewarded = rewarded;
		dataList.playerY = playerY * SCALE;
		dataList.score = score;

		if (wave != null) {
			// -1 for reward, -1 for wave detector object
			dataList.numberOfObstacles = wave.size() - 2;
		
			GameObject[] obstacles = new GameObject[3];
			int count = 0;
			for (GameObject obj : wave) {
				if (obj.getItemID() == 2) {
					dataList.rewardX = obj.getX() * SCALE;
					dataList.rewardY = obj.getY() * SCALE;
				} else if (obj.getItemID() == 1) {
					obstacles[count] = obj;
					count += 1;
				}
			}
			
			if (obstacles[0] != null) {
				dataList.obstacle1X = obstacles[0].getX() * SCALE;
				dataList.obstacle1Y = obstacles[0].getY() * SCALE;
				dataList.obstacle1Height = obstacles[0].getHeight() * SCALE;
			}
			if (obstacles[1] != null) {
				dataList.obstacle2X = obstacles[1].getX() * SCALE;
				dataList.obstacle2Y = obstacles[1].getY() * SCALE;
				dataList.obstacle2Height = obstacles[1].getHeight() * SCALE;
			}
			if (obstacles[2] != null) {
				dataList.obstacle3X = obstacles[2].getX() * SCALE;
				dataList.obstacle3Y = obstacles[2].getY() * SCALE;
				dataList.obstacle3Height = obstacles[2].getHeight() * SCALE;
			}
		}
		
		resetBooleans();
		currentData.add(dataList);
	}
	
	private static void resetBooleans() {
		jumped = false;
		newWave = false;
		obstacleCollision = false;
		rewarded = false;
	}
	
	public static void sendData() {
		Json jsonObj = new Json(JsonWriter.OutputType.json);
		jsonObj.setUsePrototypes(false);
		String json = jsonObj.toJson(allData);
				
		System.out.println(allData.size());
		
		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
		HttpRequest httpRequest = requestBuilder
				.newRequest().method(HttpMethods.POST)
				.url(DATA_URL)
				.header("Content-Type", "application/json")
				.header("accept", "application/json")
				.content(json)
				.build();
		Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				final int statusCode = httpResponse.getStatus().getStatusCode();
	            System.out.println("Success: " + statusCode);
	            Json temp = new Json();
	            temp.setUsePrototypes(false);
	            System.out.println(temp.prettyPrint(allData));
			}

			@Override
			public void failed(Throwable t) {
				System.out.println("HTTP request failed!");
				Json temp = new Json();
	            temp.setUsePrototypes(false);
	            System.out.println(temp.prettyPrint(allData));
			}

			@Override
			public void cancelled() {
				// TODO Auto-generated method stub
				System.out.println("Cancelled");
				
			}
		});
		
	}
	
	/* Main data class to be serialized into JSON */
	private static class Data {
		private String id;
		private int game;
		private ArrayList<DataList> gameData;
		
		private Data() {
			gameData = new ArrayList<DataList>();
		}
		
		private void add(DataList dataList) {
			gameData.add(dataList);
		}
	}
	
	/* Helper class of Data class. Will get serialized into JSON */
	private static class DataList {
		private double timeStamp;
		private boolean waveSpawned;
		private boolean pressedSpace;
		private boolean obstacleCollision;
		private boolean rewarded;
		private float playerY;
		private float rewardX;
		private float rewardY;
		private int numberOfObstacles;
		private float obstacle1X;
		private float obstacle1Y;
		private float obstacle1Height;
		private float obstacle2X;
		private float obstacle2Y;
		private float obstacle2Height;
		private float obstacle3X;
		private float obstacle3Y;
		private float obstacle3Height;
		private int score;
	}
}
