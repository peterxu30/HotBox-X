package com.running.game.gameObjects;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.running.game.helpers.Config;

public class GameObjectMaker {
	private World world;
	private float scale;
	private float zoneHeight = Gdx.graphics.getHeight() / 12;
	private float minHeight = zoneHeight;
	
	private String distType;
	private Object distribution;
	
	private float spawnX;
	private float width;
	private float ms;
	
	//zones 2 - 10. zone# * 40px = startingY
	public GameObjectMaker(World world) {
		this.world = world;
		this.scale = Config.scale;
		this.spawnX = Config.objectSpawnX;
		this.width = Config.objectWidth;
		this.ms = Config.objectSpeed;
		setDistribution(Config.distribution);
	}
	
	private void setDistribution(String dist) {
		distType = dist;
		switch (dist) {
		case "normal":
			distribution = new NormalDistribution(Config.normalMean, Config.normalSD);
			break;
		case "uniform":
			distribution = new UniformIntegerDistribution(0, 8);
			break;
		}
	}
	
	public ArrayList<GameObject> createWave() {
		ArrayList<GameObject> wave = new ArrayList<GameObject>();
		//must have 2 zone wide gap for player
		boolean rewarded = false;
		float rewardY = 0;
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
				if (!rewarded) {
					rewardY = (gap + 2) * zoneHeight;
					rewarded = true;
				}
				zones[gap] = true;
				numberGaps -= 1;
			}
		}
		
		return createObjects(wave, zones, rewarded, rewardY);
	}
	
	private ArrayList<GameObject> createObjects(
			ArrayList<GameObject> wave, 
			boolean[] zones, 
			boolean rewarded, 
			float rewardY) {
		//fill with objects, if gap, check for reward, if no gap, fill with obstacle
		int obsWidth = 0;
		for (int i = 0; i < zones.length; i++) {
			int zone = i + 2;
			float y = zone * zoneHeight;
			if (zones[i] == false) {
				Obstacle obs = new Obstacle(world, spawnX/scale, y/scale, width/scale, 80f/scale);
				obs.setSpeed(ms);
				wave.add(obs);
			} else {
				if (rewarded) {
					Reward reward = new Reward(world, spawnX/scale, rewardY/scale, width/scale, 20f/scale);
					reward.setSpeed(ms);
					wave.add(reward);
					rewarded = false;
				}
			}
		}
		return wave;
	}
	
	public int randomizer() {
		switch (distType) {
		case "normal":
			return Math.abs((int) (((NormalDistribution) distribution).sample()) % 9);
		case "uniform":
			return ((UniformIntegerDistribution) distribution).sample();
		}
		Random randomizer = new Random();
		return randomizer.nextInt(6) + 2;
	}
	
}
