package com.runninggame.gameobjects;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.physics.box2d.World;
import com.runninggame.distributions.NormalDistribution;
import com.runninggame.distributions.UniformIntegerDistribution;

/**
 * Responsible for generating new 'waves' of objects (rewards and obstacles) in the game.
 * Will vary the position of objects based on uniform or normal distributions.
 * There are 9 zones in which objects can be spawned in. Obstacles are of variable height,
 * calculated in multiples of the zone width. GameObjectMaker generates 'gaps' first based on
 * the distribution and then populates the rest of the wave with obstacles to ensure 
 * there is no 'no-win' scenario where there are no openings for the player to pass through.
 * @author Peter
 *
 */
public class GameObjectMaker {
	
	/** Box2D world */
	private World world;
	
	/** Conversion scale from Box2D units to pixels */
	private float scale;
	
	/** Width of game's vertical boundaries */
	private float boundary;
	
	/** Width of a zone */
	private float zoneWidth;
	
	/** Number of zones */
	private final int NUMBER_OF_ZONES = 9;
	
	/** Number of openings player may pass through */
	private final int NUMBER_OF_GAPS = 2;
	
	/** Distribution type. Normal or uniform */
	private String distType;
	
	/** Distribution object. Two classes do not share a common parent so distribution
	 * must be casted later. NormalDistribution or UniformIntegerDistribution */
	private Object distribution;
	
	/** Reward object dimension. Not meant to be changed. */
	private final float REWARD_LENGTH = 24f;
	
	/** Wave spawn x-coordinate */
	private float spawnX;
	
	/** Width of obstacle */
	private float width;
	
	/** Speed of objects in Box2D units */
	private float speed;
	
	/**
	 * Set the Box2D world of this gameObjectMaker
	 * @param world: Box2D world
	 * @return current gameObjectMaker
	 */
	public GameObjectMaker setWorld(World world) {
		//zones 2 - 9. zone# * 44px = startingY
		this.world = world;
		return this;
	}
	
	/**
	 * Set the conversion scale from Box2D units to pixels
	 * @param scale: Box2D units to pixel scale
	 * @return current gameObjectMaker
	 */
	public GameObjectMaker setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	/**
	 * Set the spawn point of waves.
	 * @param spawnX: Spawn x-coordinate
	 * @return current gameObjectMaker
	 */
	public GameObjectMaker setSpawn(float spawnX) {
		this.spawnX = spawnX;
		return this;
	}
	
	/**
	 * Set object width in Box2D units. Save for all objects.
	 * @param width: Object width in Box2D units
	 * @return current gameObjectMaker
	 */
	public GameObjectMaker setObjectWidth(float width) {
		this.width = width;
		return this;
	}
	
	/**
	 * Set speed of objects in Box2D units
	 * @param speed: Object speed in Box2D units
	 * @return current gameObjectMaker
	 */
	public GameObjectMaker setSpeed(float speed) {
		this.speed = speed;
		return this;
	}
	
	/**
	 * Set boundaries so objects don't spawn where they shouldn't
	 * @param boundary: Boundary width in Box2D units
	 * @return current gameObjectMaker
	 */
	public GameObjectMaker setBoundary(float boundary) {
		this.boundary = boundary;
		this.zoneWidth = (480f - (2 * boundary)) / NUMBER_OF_ZONES;
		return this;
	}
	
	/**
	 * Set distribution that this gameObjectMaker will use when generating waves
	 * @param dist: Distribution type.
	 * @param normalMean: Mean if normal distribution
	 * @param normalSD: Standard deviation if normal distribution
	 * @return
	 */
	public GameObjectMaker setDistribution(String dist, double normalMean, double normalSD) {
		distType = dist;
		if ("normal".equals(distType)) {
			distribution = new NormalDistribution(normalMean, normalSD);
		} else if ("uniform".equals(distType)) {
			distribution = new UniformIntegerDistribution(0, NUMBER_OF_ZONES - 2);
		}
		return this;
	}
	
	/**
	 * First method in wave creation process. There are several methods, each accomplishing
	 * a different task. They chain together.
	 * This method selects gaps in the wave.
	 * @return ArrayList that represents the wave.
	 */
	public ArrayList<GameObject> createWave() {
		//must have 2 zone wide gap for player
		boolean rewarded = false;
		float rewardY = boundary + zoneWidth;
		int numberGaps =  (randomize() % NUMBER_OF_GAPS) + 1;
		//zones represents the zones objects can spawn in
		boolean[] zones = new boolean[NUMBER_OF_ZONES + 1];
		while (numberGaps > 0) {
			//randomly selecting gap based on current distribution
			int gap = randomize();
			//false = not a gap
			if (zones[gap] == false) {
				if (gap > 0) {
					//setting the zone to be a gap
					zones[gap - 1] = true;
				}
				//gaps take up 3 indices in the array if possible. represents 2*zoneWidth gap
				if (gap < zones.length - 1) {
					zones[gap + 1] = true;
				}
				//create the reward if hasn't been created yet.
				if (!rewarded) {
					rewarded = true;
					rewardY = (gap + 1) * zoneWidth + boundary;
				}
				zones[gap] = true;
				numberGaps -= 1;
			}
		}
		//off screen zone. for NewWaveSignal object
		zones[NUMBER_OF_ZONES] = true;
		return createObjects(zones, rewarded, rewardY);
	}
	
	/**
	 * Creates obstacles and rewards after gaps have been selected.
	 * @param zones: Boolean array representing which zones are gaps.
	 * @param rewarded: Does wave contain reward object
	 * @param rewardY: Y-coordinate of reward object in Box2D units
	 * @return ArrayList that represents the wave.
	 */
	private ArrayList<GameObject> createObjects(boolean[] zones, boolean rewarded, float rewardY) {
		ArrayList<GameObject> wave = new ArrayList<GameObject>();
		if (rewarded) {
			wave = createReward(wave, rewardY);
		}
		int firstObs = 0;
		int lastObs = 0;
		boolean unbroken = false;
		boolean obs = false;
		/*obstacles are of variable width and width must be calculated.
		Loop runs through all the zones, checks if each zone should have gap or not.
		If zone does not have gap and was preceded by a non gap zone, then an obstacle of
		2 zoneWidth will be created. Checks for unbroken chain of non gap zones to determine width.
		Constructs new obstacles when chain is broken. */
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
		//for signalling new wave to spawn
		NewWaveSignal nws = (NewWaveSignal) new NewWaveSignal(world, 20f / scale, 20f / scale)
			.setSpeed(speed)
			.setPosition(spawnX / scale, 490f / scale);
		wave.add(nws);
		return wave;
	}
	
	/**
	 * Creates reward object
	 * @param wave: Wave reward will spawn in
	 * @param y: Y-coordinate of reward in Box2D units
	 * @return ArrayList that represents the wave.
	 */
	private ArrayList<GameObject> createReward(ArrayList<GameObject> wave, float y) {
		Reward reward = (Reward) new Reward(world, REWARD_LENGTH / scale, REWARD_LENGTH / scale)
			.setSpeed(speed)
			.setPosition(spawnX / scale, y / scale);
		wave.add(reward);
		return wave;
	}
	
	/**
	 * Create obstacle object
	 * @param wave: Wave object will spawn in
	 * @param height: Height dimension of object in Box2D units
	 * @param y: Obstacle y-coordinate in Box2D units
	 * @return
	 */
	private ArrayList<GameObject> createObstacle(ArrayList<GameObject> wave, float height, float y) {
		Obstacle obstacle = (Obstacle) new Obstacle(world, width/scale, height/scale)
			.setSpeed(speed)
			.setPosition(spawnX/scale, y/scale);
		wave.add(obstacle);
		return wave;
	}
	
	/**
	 * Generates a random number based on distribution.
	 * @return random number
	 */
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
