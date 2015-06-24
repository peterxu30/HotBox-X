package com.running.game.gameObjects;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;

public class GameObjectMaker {
	private World world;
	private float scale;
	private float zoneHeight = Gdx.graphics.getHeight() / 12;
//	private float zoneHeight = 50f;
	private float minHeight = zoneHeight;
	
	private String distType;
	private Object distribution;
	
	private float spawnX;
	private float width;
	private float ms;
	private int maxNumberOfObstacles;
	
	//zones 2 - 10. zone# * 40px = startingY
	public GameObjectMaker(World world, float spawnX, float width, float ms, int maxNumberOfObstacles) {
		this.world = world;
		this.spawnX = spawnX;
		this.width = width;
		this.ms = ms;
		this.maxNumberOfObstacles = maxNumberOfObstacles;
		//		NormalDistribution nD = new NormalDistribution();
	}
	
	public GameObjectMaker setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	public GameObjectMaker setDistribution(String dist) {
		distType = dist;
		switch (dist) {
		case "normal":
			distribution = new NormalDistribution(/* Config.normalMean, Config.normalSD */);
			break;
		case "uniform":
			distribution = new UniformIntegerDistribution(0, 8);
			break;
		}
		return this;
	}
	
	public ArrayList<GameObject> createWave() {
		ArrayList<GameObject> wave = new ArrayList<GameObject>();
		//must have 2 zone wide gap for player
		int numberGaps = (randomizer() % 3) + 1;
		boolean[] zones = new boolean[9];
		while (numberGaps > 0) {
			int gap = randomizer();
			if (zones[gap] == false) {
				if (gap > 0) {
					zones[gap - 1] = true;
				}
				if (gap < zones.length - 1) {
					zones[gap + 1] = true;
				}
				zones[gap] = true;
				numberGaps -= 1;
			}
		}
		//fill with objects
		// if gap, roll for reward
		// if no gap, fill with obstacle
		int obsWidth = 0;
		boolean rewarded = false;
		for (int i = 0; i < zones.length; i++) {
			int zone = i + 2;
			float y = zone * zoneHeight;
			if (zones[i] == false) {
				Obstacle obs = new Obstacle(world, spawnX/scale, y/scale, width/scale, 80f/scale);
				obs.setSpeed(ms);
				wave.add(obs);
			} else {
				if (!rewarded) {
					if ((i > 0 && i < (zones.length - 1) && zones[i - 1] && zones[i + 1])
							|| ((i == 0 && zones[i + 1]) || (i == zones.length - 1 && zones[i - 1]))) {
						Reward reward = new Reward(world, spawnX/scale, (y)/scale, width/scale, 20f/scale);
						reward.setSpeed(ms);
						wave.add(reward);
						rewarded = true;
					}
				}
			}
		}
		
		return wave;
	}
	
	public int randomizer() {
		switch (distType) {
		case "normal":
			return (int) ((NormalDistribution) distribution).sample();
//			break;
		case "uniform":
			return ((UniformIntegerDistribution) distribution).sample();
//			break;
		}
		Random randomizer = new Random();
		return randomizer.nextInt(6) + 2;
	}
	
}
