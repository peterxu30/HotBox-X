package com.running.game.gameobjects;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.physics.box2d.World;
import com.running.game.distributions.NormalDistribution;
import com.running.game.distributions.UniformIntegerDistribution;

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
	
	/** Highest point of playing field */
	private float ceiling;
	
	/** Lowest point of playing field */
	private float floor;
	
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
	
	/** Width of gap */
	private float gapWidth;
	
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
	 * Set width of gap in waves.
	 * @param width: Width of gaps between obstacles.
	 * @return current gameObjectMaker
	 */
	public GameObjectMaker setGapWidth(float width) {
		this.gapWidth = width;
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
		this.ceiling = boundary;
		this.floor = 480f - boundary;
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
		float[][] gapCoordinates = new float[numberGaps][2];
		//zones represents the zones objects can spawn in
		boolean[] zones = new boolean[NUMBER_OF_ZONES + 1];
		while (numberGaps > 0) {
			//randomly selecting gap based on current distribution
			int gap = randomize();
			//false = not a gap
			if (zones[gap] == false) {
				float gapY1 = ((gap + 1) * zoneWidth) + boundary - (gapWidth / 2.0f);
				float gapY2 = ((gap + 1) * zoneWidth) + boundary + (gapWidth / 2.0f);
				gapCoordinates[numberGaps - 1][0] = gapY1;
				gapCoordinates[numberGaps - 1][1] = gapY2;
				//create the reward if hasn't been created yet.
				if (!rewarded) {
					rewarded = true;
					rewardY = (gap + 1) * zoneWidth + boundary;
				}
				zones[gap] = true;
				numberGaps -= 1;
			}
		}
		//keep higher gap in first slot.
		if (gapCoordinates.length > 1) {
			if (gapCoordinates[1][0] < gapCoordinates[0][0]) {
				float[] oldY1 = gapCoordinates[0];
				gapCoordinates[0] = gapCoordinates[1];
				gapCoordinates[1] = oldY1;
			}
			
			//merge gaps if space between is too small for obstacle
			if (gapCoordinates[0][1] + zoneWidth >= gapCoordinates[1][0]) {
				float y1 = gapCoordinates[0][0];
				float y2 = gapCoordinates[1][1];
				gapCoordinates = new float[1][2];
				gapCoordinates[0][0] = y1;
				gapCoordinates[0][1] = y2;
			}
		}
		
		return createObjects(gapCoordinates, rewarded, rewardY);
	}
	
	/**
	 * Creates obstacles and rewards after gaps have been selected.
	 * @param zones: Boolean array representing which zones are gaps.
	 * @param rewarded: Does wave contain reward object
	 * @param rewardY: Y-coordinate of reward object in Box2D units
	 * @return ArrayList that represents the wave.
	 */
	private ArrayList<GameObject> createObjects(float[][] gapCoordinates, boolean rewarded, float rewardY) {
		ArrayList<GameObject> wave = new ArrayList<GameObject>();
		if (rewarded) {
			wave = createReward(wave, rewardY);
		}
		
		if (gapCoordinates[0][0] - zoneWidth >= ceiling) {
			float height = gapCoordinates[0][0] - ceiling;
			float y = (height / 2.0f) + ceiling;
			createObstacle(wave, height, y);
		}
		
		if (gapCoordinates.length > 1) {
			//middle obstacle
			float height = gapCoordinates[1][0] - gapCoordinates[0][1];
			float y = (height / 2.0f) + gapCoordinates[0][1];
			createObstacle(wave, height, y);
			
			//last obstacle
			if (gapCoordinates[1][1] + zoneWidth <= floor) {
				float height1 = floor - gapCoordinates[1][1];
				float y1 = (height1 / 2.0f) + gapCoordinates[1][1];
				createObstacle(wave, height1, y1);
			}
		} else if (gapCoordinates[0][1] + zoneWidth <= floor) {
			float height = floor - gapCoordinates[0][1];
			float y = (height / 2.0f) + gapCoordinates[0][1];
			createObstacle(wave, height, y);
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
