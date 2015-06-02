package com.running.game.gameObjects;

import java.util.ArrayList;
import java.util.Random;

/**
 * Object that creates obstacles. All probability distribution calculations
 * happen in this class; this includes obstacle. GameMain does not handle anything
 * related to obstacle creation, it inserts into the game whatever ObstacleCreator
 * gives it.
 * Work in progress.
 * @author Peter Xu
 *
 */
public class ObstacleCreator {
	private float boundWidth;
	private float startingY;
	private float movementSpeed;
	private String obstacleImage;
	
	/* Game Screen has 6 60 pixel zones. */
	public ObstacleCreator(float boundaryWidth, float y, float ms, String obstacleImg) {
		boundWidth = boundaryWidth;
		startingY = y;
		movementSpeed = ms;
		obstacleImage = obstacleImg;
	}
	
	public ArrayList<Obstacle> createObstacleWave(boolean zoned) {
		int zones = 5;
		float intervalX = 60f;
		float obstacleWidth = 60f;
		if (zoned) {
			zones = 3;
			intervalX = 120f;
			obstacleWidth = 120f;
		}
		Random rng = new Random(); 
		int numberOfObstacles = rng.nextInt(2);
		ArrayList<Obstacle> wave = new ArrayList<Obstacle>();
		while (numberOfObstacles >= 0) {
			int zone = rng.nextInt(zones);
				float start = (float) zone * intervalX + boundWidth;
				wave.add(createObstacle(start, startingY, obstacleWidth)); 
				numberOfObstacles -= 1;
		}
		return wave;
	}
	
//Obstacle(float x, float y, float width, float height, float ms, String obstacleImg)
	private Obstacle createObstacle(float x, float y, float w) {
		Random dimensionsRandom = new Random();
//		float height = (float) dimensionsRandom.nextInt(60);
		return new Obstacle(x, y, w, 20f, movementSpeed, obstacleImage);
	}
	
	public void changeMoveSpeed(float ms) {
		movementSpeed = ms;
	}
	
	public void changeImage(String img) {
		obstacleImage = img;
	}
	
	
}
