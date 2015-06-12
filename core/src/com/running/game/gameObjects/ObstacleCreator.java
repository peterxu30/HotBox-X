package com.running.game.gameObjects;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;

/**
 * Object that creates obstacles. All probability distribution calculations
 * happen in this class; this includes obstacle. GameMain does not handle anything
 * related to obstacle creation, it inserts into the game whatever ObstacleCreator
 * gives it.
 * 
 * Obstacles are discrete in size and location. It is too difficult and unpredictable
 * to have obstacles that can spawn anywhere with any width. Risk of overlap, impassable waves etc.
 * Work in progress.
 * @author Peter Xu
 *
 */
public class ObstacleCreator {
	private final float playFieldWidth = 360f;
	private final float boundWidth = 60f;
	private float playerWidth;
	private int playerZones;
	private int maxNumberOfObstacles;
	private float startingY;
	private float obstacleHeight;
	private float movementSpeed;
	private String obstacleImage;
	
	/* Zones 0 - 11 */
	private final int numberOfZones = 12;
	private final float zoneWidth = 30f;
	private int minObstacleZones = 2;
	private int maxObstacleZones;
	
	/* Game Screen has 12 30 pixel zones. */
	/**
	 * Constructor for ObstacleCreator class
	 * @param spawnY Obstacle spawn height
	 * @param minWidth Minimum width of obstacle
	 * @param height Height of obstacles
	 * @param ms Movement speed of obstacles
	 */
	public ObstacleCreator(float spawnY, float height, float ms, int maxNumberOfObstacles) {
		// use setBoundary to configure boundWidth and playFieldWidth
		//use setPlayerWidth to configure playerWidth
//		minObstacleWidth = minWidth;
		startingY = spawnY;
		obstacleHeight = height;
		movementSpeed = ms;
		this.maxNumberOfObstacles = maxNumberOfObstacles;
	}
	
	public ObstacleCreator setPlayerWidth(float width) {
		playerWidth = width;
		playerZones = convertToZones(playerWidth);
		maxObstacleZones = numberOfZones - playerZones;
		return this;
	}
	
	/**
	 * Returns the minimum number of zones a given width will fit in
	 * @param width Width value of some object
	 * @return Minimum number of zones such that an object will fit
	 */
	private int convertToZones(float width) {
		return Math.round(width / zoneWidth);
	}
	
	public ObstacleCreator setSpriteImage(String obstacleImg) {
		obstacleImage = obstacleImg;
		return this;
	}
	
	
	public ArrayList<Obstacle> createObstacleWave(boolean zoned) {
//		if (zoned) { fix later
//			//(Gdx.graphics.getWidth() - (2 * boundaryWidth)) / 3; for zoned games figure it out later
//			zones = 3;
//			intervalX = 120f;
//			obstacleWidth = 120f;
//		}
		Random rng = new Random(); //distribution fix later
		ArrayList<Obstacle> wave = new ArrayList<Obstacle>();
		int numberOfObstacles = rng.nextInt(maxNumberOfObstacles + 1);
		int numberOfOpenings = (12 - numberOfObstacles) / 2;
		// generate openings first, fill rest with obstacles
		boolean[] freeZones = new boolean[numberOfZones];
		while (numberOfObstacles >= 0) {
			int zoneSize = rng.nextInt(maxObstacleZones) + 1;
			float width = (zoneSize * zoneWidth);
			int startZone = rng.nextInt(numberOfZones - zoneSize);
			float start = (startZone * zoneWidth) + boundWidth;
			wave.add(createObstacle(start, startingY, width));
			
			numberOfObstacles -= 1;
		}
		return wave;
	}
	
	private boolean[] updateTakenZones(boolean[] zoneArray, int zoneSize, int startZone) {
		int counter = startZone;
		while (zoneSize > 0) {
			zoneArray[counter] = true;
			counter += 1;
			zoneSize -= 1;
		}
		return zoneArray;
	}
	
	private void checkOverlap(ArrayList<Obstacle> wave) {
		
	}
	
//Obstacle(float x, float y, float width, float height, float ms, String obstacleImg)
	private Obstacle createObstacle(float x, float y, float width) {
		Random dimensionsRandom = new Random();
		return new Obstacle(x, y, width, obstacleHeight, movementSpeed, obstacleImage);
	}
	
	public void changeMoveSpeed(float ms) {
		movementSpeed = ms;
	}
	
	public void changeImage(String img) {
		obstacleImage = img;
	}
	
	
}
