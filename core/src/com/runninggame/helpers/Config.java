package com.runninggame.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Config {
	
	public final static float SCALE = 40;
	
	public static final String DATA_URL = "http://localhost:3000/data/";
	private static final String CONFIG_URL = "http://localhost:3000/settings/";
	
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
	public static float waveStart = 480f;
	public static int rewardValue = 1;
	public static int penaltyValue = 5;
	public static int minScore = 0;
	public static String distribution = "uniform";
	public static String gameMode = "penalty";

	private static boolean loaded;
	private static int numberOfGames; 
	private static int currentGame = 0;
	private static JsonValue json;
	private static JsonValue currentJson;

	public static void loadJson() {
		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
		HttpRequest httpRequest = requestBuilder
				.newRequest().method(HttpMethods.GET)
				.url(CONFIG_URL)
				.build();
		Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				final int statusCode = httpResponse.getStatus().getStatusCode();
	            String settingsJson = httpResponse.getResultAsString();
	            json = new JsonReader().parse(settingsJson);
	            numberOfGames = json.size;
	            loaded = true;
	            System.out.println(settingsJson);
			}

			@Override
			public void failed(Throwable t) {
				// TODO Auto-generated method stub
				loaded = false;
				System.out.println("HTTP request failed!");
			}

			@Override
			public void cancelled() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public static void load() {
		if (loaded) {
			currentJson = json.require(currentGame);
			playerSpeed = currentJson.getFloat("playerSpeed");
			playerWidth = currentJson.getFloat("playerWidth");
			playerHeight = currentJson.getFloat("playerHeight");
			playerX = currentJson.getFloat("playerX");
			playerY = currentJson.getFloat("playerY");
			gravity = currentJson.getFloat("gravity");
			objectWidth = currentJson.getFloat("objectWidth");
			objectSpeed = currentJson.getFloat("objectSpeed");
			objectSpawnX = currentJson.getFloat("objectSpawnX");
			normalMean = currentJson.getFloat("normalMean");
			normalSD = currentJson.getFloat("normalSD");
			waveStart = currentJson.getFloat("waveStart");
			rewardValue = currentJson.getInt("rewardValue");
			penaltyValue = currentJson.getInt("penaltyValue");
			minScore = currentJson.getInt("minScore");
			distribution = currentJson.getString("distribution");
			gameMode = currentJson.getString("gameMode");
			currentGame += 1;
		}
	}
	
	public static void reset() {
		currentGame = 0;
	}
	
	public static boolean gamesOver() {
		return (currentGame >= numberOfGames);
	}
	
	public static int getCurrentGame() {
		return currentGame;
	}
}
