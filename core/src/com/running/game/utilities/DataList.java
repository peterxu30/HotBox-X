package com.running.game.utilities;

/**
 * Helper class of Data class. Will get serialized into JSON
 * @author Peter
 *
 */
public class DataList {
	
	/** timeStamp of this snapshot */
	public double timeStamp;
	
	/** Did wave spawn */
	public boolean waveSpawned;
	
	/** Did player jump */
	public boolean pressedSpace;
	
	/** Was there a player-obstacle collision */
	public boolean obstacleCollision;
	
	/** Did player collect a reward */
	public boolean rewarded;
	
	/** Player y-coordinate at moment of snapshot */
	public float playerY;
	
	/** Reward x-coordinate at moment of snapshot, 0 if N/A */
	public float rewardX;
	
	/** Reward y-coordinate at moment of snapshot, 0 if N/A */
	public float rewardY;
	
	/** Number of obstacles in wave */
	public int numberOfObstacles;
	
	/** First obstacle x-coordinate */
	public float obstacle1X;
	
	/** First obstacle y-coordinate */
	public float obstacle1Y;
	
	/** First obstacle height */
	public float obstacle1Height;
	
	/** Second obstacle x-coordinate */
	public float obstacle2X;
	
	/** Second obstacle y-coordinate */
	public float obstacle2Y;
	
	/** Second obstacle height */
	public float obstacle2Height;
	
	/** Third obstacle x-coordinate */
	public float obstacle3X;
	
	/** Third obstacle y-coordinate */
	public float obstacle3Y;
	
	/** Third obstacle height */
	public float obstacle3Height;
	
	/** Current score */
	public int score;
}
