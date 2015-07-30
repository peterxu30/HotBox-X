package com.runninggame.gameobjects;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.physics.box2d.World;
import com.runninggame.distributions.NormalDistribution;
import com.runninggame.distributions.UniformIntegerDistribution;

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
		distType = dist;
		if ("normal".equals(distType)) {
			distribution = new NormalDistribution(normalMean, normalSD);
		} else if ("uniform".equals(distType)) {
			distribution = new UniformIntegerDistribution(0, NUMBER_OF_ZONES - 2);
		}
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
		boolean[] zones = new boolean[NUMBER_OF_ZONES + 1];
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
		zones[NUMBER_OF_ZONES] = true;
		return createObjects(zones, rewarded, rewardY);
	}
	
	private ArrayList<GameObject> createObjects(boolean[] zones, boolean rewarded, float rewardY) {
		ArrayList<GameObject> wave = new ArrayList<GameObject>();
		if (rewarded) {
			wave = createReward(wave, rewardY);
		}
		int firstObs = 0;
		int lastObs = 0;
		boolean unbroken = false;
		boolean obs = false;
		for (int i = 0; i < zones.length; i++) {
			if (zones[i] == false) {
				if (unbroken) {
					lastObs = i;
				} else {
					firstObs = i;
					lastObs = i;
					unbroken = true;
				}
				obs = true;
			} else {
				if (obs) {
					float obsHeight = (lastObs - firstObs + 2) * zoneWidth;
					float y = ((zoneWidth * (firstObs + 1) + boundary) 
								+ (zoneWidth * (lastObs + 1) + boundary)) / 2f;
					createObstacle(wave, obsHeight, y);
				}
				unbroken = false;
				obs = false;
			}
		}
		NewWaveSignal nws = (NewWaveSignal) new NewWaveSignal(world, 20f / scale, 20f / scale)
			.setSpeed(speed)
			.setPosition(spawnX / scale, 490f / scale);
		wave.add(nws);
		return wave;
	}
	
	private ArrayList<GameObject> createReward(ArrayList<GameObject> wave, float y) {
		Reward reward = (Reward) new Reward(world, REWARD_LENGTH / scale, REWARD_LENGTH / scale)
			.setSpeed(speed)
			.setPosition(spawnX / scale, y / scale);
		wave.add(reward);
		return wave;
	}
	
	private ArrayList<GameObject> createObstacle(ArrayList<GameObject> wave, float height, float y) {
		Obstacle obstacle = (Obstacle) new Obstacle(world, width/scale, height/scale)
			.setSpeed(speed)
			.setPosition(spawnX/scale, y/scale);
		wave.add(obstacle);
		return wave;
	}
	
	public int randomize() {
		switch (distType) {
		case "normal":
			int random = (int) ((NormalDistribution) distribution).sample();
			if (random < 0) {
				random = 0;
			} else if (random >= NUMBER_OF_ZONES - 2) {
				random = NUMBER_OF_ZONES - 1;
			}
			return random;
		case "uniform":
			return ((UniformIntegerDistribution) distribution).sample();
		}
		Random randomizer = new Random();
		return randomizer.nextInt(NUMBER_OF_ZONES - 1);
	}
}

