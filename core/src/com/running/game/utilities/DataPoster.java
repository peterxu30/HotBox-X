package com.running.game.utilities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.TimeUtils;
import com.running.game.gameobjects.GameObject;

/**
 * Sends game data to web server
 * @author Peter
 *
 */
public class DataPoster {
	
	/** URL to send game data to */
	private static final String DATA_URL = Config.DATA_URL;
	
	/** Conversion scale from Box2D units to pixels */
	private static final float SCALE = Config.SCALE;
	
	/** Has player jumped */
	private static boolean jumped;
	
	/** Has new wave spawned */
	private static boolean newWave;
	
	/** Was there a player-obstacle collision */
	private static boolean obstacleCollision;
	
	/** Did player collect a reward */
	private static boolean rewarded;
	
	/** Current game number */
	private static int gameNumber;
	
	/** In-game time */
	private static long time;
	
	/** Randomly generated user id */
	private static String id;
	
	/** Current game data */
	private static Data currentData;
	
	/** All games data */
	private static ArrayList<Data> allData;
	
	/**
	 * Set player id
	 */
	public static void setUp() {
		id = UUID.uuid();
		gameNumber = 0;
		allData = new ArrayList<Data>();
	}
	
	/**
	 * Reset time for new game.
	 */
	public static void resetTime() {
		time = TimeUtils.millis();
		currentData = new Data();
		currentData.id = id;
		currentData.game = gameNumber;
	}
	
	/**
	 * Adds data for previous round to allData.
	 * Set up for new round.
	 * @param game
	 */
	public static void newRound(int game) {
		gameNumber = game;
		allData.add(currentData);
	}
	
	/**
	 * Record new wave spawn
	 * @param playerY: Player y-coordinate at moment of wave spawn
	 * @param wave: Wave at that moment
	 * @param score: Player score at that moment
	 */
	public static void createdWave(float playerY, ArrayList<GameObject> wave, int score) {
		newWave = true;
		createData(playerY, wave, score);
	}
	
	/**
	 * Record player-object collision (both reward and obstacle)
	 * @param playerY: Player y-coordinate at that moment
	 * @param score: Player score at that moment
	 * @param rewarded: reward or obstacle
	 */
	public static void hasCollided(float playerY, int score, boolean rewarded) {
		obstacleCollision = !rewarded;
		DataPoster.rewarded = rewarded;
		createData(playerY, null, score);
	}
	
	/**
	 * Record player jump
	 * @param playerY: Player y-coordinate at that moment
	 * @param score: Player score at that moment
	 */
	public static void hasJumped(float playerY, int score) {
		jumped = true;
		createData(playerY, null, score);
	}
	
	/**
	 * Create game data
	 * @param playerY: Player y at that moment
	 * @param wave: Wave at that moment
	 * @param score: Player score at that moment
	 */
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
	
	/**
	 * Reset checks
	 */
	private static void resetBooleans() {
		jumped = false;
		newWave = false;
		obstacleCollision = false;
		rewarded = false;
	}
	
	/**
	 * Send data to server
	 */
	public static void sendData() {
		Json jsonObj = new Json(JsonWriter.OutputType.json);
		jsonObj.setUsePrototypes(false);
		String json = jsonObj.toJson(allData);
				
		System.out.println("AllData size: " + allData.size());
		
		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
		HttpRequest httpRequest = requestBuilder
				.newRequest().method(HttpMethods.POST)
				.url(DATA_URL)
				.header("Content-Type", "application/json")
				.header("accept", "application/json")
				.header("x-access-token", Config.tokenString)
				.content(json)
				.timeout(5000)
				.build();
		Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
	            Json temp = new Json();
	            temp.setUsePrototypes(false);
	            System.out.println(temp.prettyPrint(allData));
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.log("DataPoster", "HTTP request failed!");
				sendData();
			}

			@Override
			public void cancelled() {
				// TODO Auto-generated method stub
				Gdx.app.log("DataPoster", "HTTP request cancelled!");
				
			}
		});
		
	}
	
}
