package com.runninggame.gameobjects;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;

import com.badlogic.gdx.physics.box2d.World;

public class GameObjectMaker {
	
	private World world;
	private float scale;
	private float boundary;
	private float zoneWidth;
	private final int NUMBER_OF_ZONES = 9;
	private final int NUMBER_OF_GAPS = 2;
	private String distType;
	private Object distribution;
	
	private final float REWARD_LENGTH = 24f;
	private float spawnX;
	private float width;
	private float speed;
	
	//zones 2 - 9. zone# * 44px = startingY
	public GameObjectMaker setWorld(World world) {
		this.world = world;
		return this;
	}
	
	public GameObjectMaker setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	public GameObjectMaker setSpawn(float spawnX) {
		this.spawnX = spawnX;
		return this;
	}
	
	public GameObjectMaker setObjectWidth(float width) {
		this.width = width;
		return this;
	}
	
	public GameObjectMaker setSpeed(float speed) {
		this.speed = speed;
		return this;
	}
	
	public GameObjectMaker setBoundary(float boundary) {
		this.boundary = boundary;
		this.zoneWidth = (480f - (2 * boundary)) / NUMBER_OF_ZONES;
		return this;
	}
	
	public GameObjectMaker setDistribution(String dist, double normalMean, double normalSD) {
//		distType = dist;
//		switch (dist) {
//			case "normal":
//				distribution = new NormalDistribution(normalMean, normalSD);
//				break;
//			case "uniform":
//				distribution = new UniformIntegerDistribution(0, NUMBER_OF_ZONES - 2);
//				break;
//		}
		return this;
	}
	
	public ArrayList<GameObject> createWave() {
		return chooseGaps();
	}
	
	private ArrayList<GameObject> chooseGaps() {
		//must have 2 zone wide gap for player
		boolean rewarded = false;
		float rewardY = boundary + zoneWidth;
		int numberGaps =  (randomize() % NUMBER_OF_GAPS) + 1;
		boolean[] zones = new boolean[NUMBER_OF_ZONES];
		while (numberGaps > 0) {
			int gap = randomize();
			if (zones[gap] == false) {
				if (gap > 0) {
					zones[gap - 1] = true;
				}
				if (gap < zones.length - 1) {
					zones[gap + 1] = true;
				}
				if (!rewarded) {
					rewarded = true;
					rewardY = (gap + 1) * zoneWidth + boundary;
				}
				zones[gap] = true;
				numberGaps -= 1;
			}
		}
		return createObjects(zones, rewarded, rewardY);
	}
	
	private ArrayList<GameObject> createObjects(boolean[] zones, boolean rewarded, float rewardY) {
		ArrayList<GameObject> wave = new ArrayList<GameObject>();
		if (rewarded) {
			wave = createReward(wave, rewardY);
		}
		for (int i = 0; i < zones.length; i++) {
			float y = (i + 1) * zoneWidth + boundary;
			if (zones[i] == false) {
				Obstacle obs = (Obstacle) new Obstacle(world, width/scale, (2 * zoneWidth)/scale)
					.setSpeed(speed)
					.setPosition(spawnX/scale, y/scale);
				wave.add(obs);
			}
		}
		return wave;
	}
	
	private ArrayList<GameObject> createReward(ArrayList<GameObject> wave, float rewardY) {
		Reward reward = (Reward) new Reward(world, REWARD_LENGTH / scale, REWARD_LENGTH / scale)
			.setSpeed(speed)
			.setPosition(spawnX/scale, rewardY/scale);
		wave.add(reward);
		return wave;
	}
	
	public int randomize() {
//		switch (distType) {
//		case "normal":
//			return Math.abs((int) (((NormalDistribution) distribution).sample()) % NUMBER_OF_ZONES);
//		case "uniform":
//			return ((UniformIntegerDistribution) distribution).sample();
//		}
		Random randomizer = new Random();
		return randomizer.nextInt(NUMBER_OF_ZONES - 1);
	}
}

