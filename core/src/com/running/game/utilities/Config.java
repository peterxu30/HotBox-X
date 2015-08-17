package com.running.game.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Loads game settings from server.
 * Central hub of information for game.
 * @author Peter
 *
 */
public class Config {
	
	/** Conversion scale from Box2D units to pixels */
	public final static float SCALE = 40;
	
	/** Screen width in pixels */
	public static final float SCREEN_WIDTH = 800f;
	
	/** Screen height in pixels */
	public static final float SCREEN_HEIGHT = 480f;
	
	/** URL to authenticate game before performing HTTP requests */
	private static final String LOGIN_URL = "http://ec2-52-8-138-168.us-west-1.compute.amazonaws.com/login/authenticate";
	
	/** URL to send game data to. Used by DataPoster */
	public static final String DATA_URL = "http://ec2-52-8-138-168.us-west-1.compute.amazonaws.com/data";
	
	/** URL to get game settings from */
	private static final String CONFIG_URL = "http://ec2-52-8-138-168.us-west-1.compute.amazonaws.com/settings";
	
	/** Game login username */
	private static final String USERNAME = "hotboxx";
	
	/** Game login password */
	private static final String PASSWORD = "mpcberkeleyhotboxx";
	
	/** Player speed in Box2D units */
	public static float playerSpeed = 7.3f;
	
	/** Player width in pixels */
	public static float playerWidth = 32f;
	
	/** Player height in pixels */
	public static float playerHeight = 32f;
	
	/** Player x-coordinate */
	public static float playerX = 60f;
	
	/** Player starting y-coordinate */
	public static float playerY = 240f;
	
	/** Game gravity in Box2D units */
	public static float gravity = 14f;
	
	/** Object width in pixels */
	public static float objectWidth = 18f;
	
	/** Object speed in Box2D units */
	public static float objectSpeed = 6f;
	
	/** Wave spawn x-coordinate */
	public static float objectSpawnX = 800f;
	
	/** Mean of normal distribution */
	public static float normalMean = 1;
	
	/** Standard deviation of normal distribution */
	public static float normalSD = 1;
	
	/** X-coordinate current wave needs to reach to spawn new wave */
	public static float waveStart = 480f;
	
	/** Value of rewards */
	public static int rewardValue = 1;
	
	/** Penalty when colliding with obstacles when in penalty game mode */
	public static int penaltyValue = 5;
	
	/** Minimum score in penalty mode before game over */
	public static int minScore = 0;
	
	/** Type of distribution */
	public static String distribution = "uniform";
	
	/** Game mode */
	public static String gameMode = "penalty";
	
	/** Number of games in the set */
	private static int numberOfGames;
	
	/** Current game number */
	private static int currentGame = 0;
	
	/** JSON of all game settings */
	private static JsonValue json;
	
	/** JSON of current game settings */
	private static JsonValue currentJson;
	
	/** JWT token for authentication */
	public static String tokenString = "filler";
	
	/**
	 * Logs into web server
	 */
	public static void login() {
		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
		HttpRequest httpRequest = requestBuilder
				.newRequest().method(HttpMethods.POST)
				.url(LOGIN_URL)
				.header("name", USERNAME)
				.header("password", PASSWORD)
				.timeout(5000)
				.build();
		Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Config.tokenString = httpResponse.getResultAsString();
				JsonValue token = new JsonReader().parse(Config.tokenString);
				Config.tokenString = token.getString("token");
				Config.loadJson(); //have to do it here due to asynchronous nature
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.log("Config", "HTTP request failed!");
				login();
			}

			@Override
			public void cancelled() {
				Gdx.app.log("Config", "HTTP request cancelled!");
			}
		});
	}
	
	/**
	 * Logs into server, sends GET request to get game settings in JSON form.
	 */
	private static void loadJson() {
		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
		HttpRequest httpRequest = requestBuilder
				.newRequest().method(HttpMethods.GET)
				.url(CONFIG_URL)
				.header("x-access-token", Config.tokenString)
				.timeout(5000)
				.build();
		Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
			
			/**
			 * Saves game settings when GET request successful
			 */
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
	            String settingsJson = httpResponse.getResultAsString();
	            json = new JsonReader().parse(settingsJson);
	            numberOfGames = json.getInt("length");
	            json = json.require("settings");
			}

			/**
			 * GET request unsuccessful
			 */
			@Override
			public void failed(Throwable t) {
				Gdx.app.log("Config", "HTTP request failed!");
				loadJson();
			}
			
			/**
			 * GET request cancelled
			 */
			@Override
			public void cancelled() {
				Gdx.app.log("Config", "HTTP request cancelled!");
			}
		});
	}
	
	/**
	 * Loads current game settings
	 */
	public static void load() {
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
	
	/**
	 * Reset to game setting 0
	 */
	public static void reset() {
		currentGame = 0;
	}
	
	/**
	 * Check if all games have been played
	 * @return boolean
	 */
	public static boolean gamesOver() {
		return (currentGame >= numberOfGames);
	}
	
	/**
	 * Get current game number
	 * @return current game number
	 */
	public static int getCurrentGame() {
		return currentGame;
	}
	
}
