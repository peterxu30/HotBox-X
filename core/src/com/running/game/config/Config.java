package com.running.game.config;

import java.io.Serializable;

public class Config implements Serializable {
	private float playerSpeed;
	private String obstacleDistribution;
	private float obstacleWidth;
	private float obstacleSpeed;
	private float obstacleSpawnHeight;
	private boolean obstacleAccelerationBoolean;
	private float obstacleAccelerationRate;
	private long waveTime;
	private int rewardValue;
	private String gameMode;
	
	public float getPlayerSpeed() {
		return playerSpeed;
	}
	
	public void setPlayerSpeed(float speed) {
		playerSpeed = speed;
	}
	
	public String getObstacleDistribution() {
		return obstacleDistribution;
	}
	
	public void setObstacleDistribution(String dist) {
		obstacleDistribution = dist;
	}
	
	public float getObstacleWidth() {
		return obstacleWidth;
	}
	
	public void setObstacleWidth(float w) {
		obstacleWidth = w;
	}
	
	public float getObstacleSpeed() {
		return obstacleSpeed;
	}
	
	public void setObstacleSpeed(float speed) {
		obstacleSpeed = speed;
	}
	
	public float getObstacleSpawnHeight() {
		return obstacleSpawnHeight;
	}
	
	public void setObstacleSpawnHeight(float height) {
		obstacleSpawnHeight = height;
	}
	
	public boolean getObstacleAccelerationBoolean() {
		return obstacleAccelerationBoolean;
	}
	
	public void setObstacleAccelerationBoolean(boolean bool) {
		obstacleAccelerationBoolean = bool;
	}
	
	public float getObstacleAccelerationRate() {
		return obstacleAccelerationRate;
	}
	
	public void setObstacleAccelerationRate(float rate) {
		obstacleAccelerationRate = rate;
	}
	
	public long getWaveTime() {
		return waveTime;
	}
	
	public void setWaveTime(long time) {
		waveTime = time;
	}
	
	public int getRewardValue() {
		return rewardValue;
	}
	
	public void setRewardValue(int val) {
		rewardValue = val;
	}
	
	public String getGameMode() {
		return gameMode;
	}
	
	public void setGameMode(String mode) {
		gameMode = mode;
	}
	
}
