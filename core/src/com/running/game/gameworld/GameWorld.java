package com.running.game.gameworld;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.running.game.gameobjects.GameObject;
import com.running.game.gameobjects.GameObjectMaker;
import com.running.game.gameobjects.NewWaveDetector;
import com.running.game.gameobjects.Player;
import com.running.game.utilities.Config;
import com.running.game.utilities.DataPoster;
import com.running.game.utilities.TimeManager;

/**
 * GameWorld takes care of all game logic. This includes wave spawning, score keeping,
 * and object collisions.
 * @author Peter
 *
 */
public class GameWorld {
	
	/** The Box2D world */
	private World physicsWorld;
	
	/** Gravity in Box2D units */
	private float gravity;
	
	/** Conversion scale from Box2D units to pixels */
	private float scale;
	
	/** Current game mode */
	private boolean permadeath;
	
	/** Is game over */
	private boolean gameOver;
	
	/** Player object */
	private Player player;
	
	/** Width of player object in pixels */
	private float playerWidth;
	
	/** Height of player object in pixles */
	private float playerHeight;
	
	/** Box2D body of ground */
	private Body groundBody;
	
	/** Box2D body of sky */
	private Body skyBody;
	
	/** Width of sky and ground boundaries */
	private final float BOUNDARY_WIDTH = 42f;
	
	/** Wave spawner */
	private GameObjectMaker objectMaker;
	
	/** Represents wave, contains obstacles, rewards, and newWaveSignals */
	private ArrayList<GameObject> wave;
	
	/** New wave indicator */
	private boolean newWave;
	
	/** Where the current wave must pass to spawn a new wave */
	private float spawnPoint;
	
	/** Player score */
	private int score;
	
	/** Positioned at the spawnPoint to signal when to spawn new waves */
	public NewWaveDetector wavePoint;
	
	/**
	 * Class constructor
	 * @param gravityY: Vertical gravity component in Box2D units
	 * @param scale: Conversion scale from Box2D units to pixels
	 * @param spawnPoint: Where current wave must pass to spawn new wave
	 * @param gameMode: Permadeath or penalty
	 */
	public GameWorld(float gravityY, float scale, float spawnPoint, String gameMode) {
		// Set up the data recorder for this round
		DataPoster.reset();
		
		this.scale = scale;
		this.spawnPoint = spawnPoint;
		if ("permadeath".equals(gameMode)) {
			this.permadeath = true;
		}
		
		physicsWorld = new World(new Vector2(0, gravityY), true);
		setContactListener();
		
		/* When a wave passes this object, a new wave is created. */
		wavePoint = (NewWaveDetector) new NewWaveDetector(physicsWorld, 1f/scale, 20f/scale)
			.setPosition(spawnPoint / scale, 490f / scale)
			.setSpeed(0f);
	
		player = new Player(physicsWorld, Config.playerWidth/scale, Config.playerHeight/scale);
		player.setPosition(Config.playerX/scale, Config.playerY/scale);
		player.setSpeed(Config.playerSpeed);
		
		addBoundaries();
		wave = new ArrayList<GameObject>();
		
		objectMaker = new GameObjectMaker()
		.setWorld(physicsWorld)
		.setScale(scale)
		.setBoundary(BOUNDARY_WIDTH)
		.setDistribution(Config.distribution, Config.normalMean, Config.normalSD)
		.setSpawn(Config.objectSpawnX)
		.setObjectWidth(Config.objectWidth)
		.setGapWidth(Config.gapWidth)
		.setSpeed(Config.objectSpeed);
		
		newWave = true;
	}
	
	/**
	 * Collision detector
	 */
	private void setContactListener() {
		physicsWorld.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Body bodyB = contact.getFixtureB().getBody();
				GameObject.GameObjectData dataB = (GameObject.GameObjectData) bodyB.getUserData();
				
				// New wave detector
				if (dataB.getID() == 4) {
					newWave = true;
				} 
				if (dataB.getID() == 2) {
					score += Config.rewardValue;
					DataPoster.hasCollided(player.getY(), getScore(), true);
					dataB.markRemove();
				} else if (dataB.getID() == 1) {
					if (!permadeath) {
						score -= Config.penaltyValue;
					}
					if (permadeath || score < Config.minScore) {
						setGameOver();
					}
					DataPoster.hasCollided(player.getY(), getScore(), false);
					dataB.markRemove();
				}
			}
			
			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	/**
	 * Check if game is over
	 * @return boolean
	 */
	public boolean isGameOver() {
		return gameOver;
	}
	
	/**
	 * Mark the game as over
	 */
	public void setGameOver() {
		gameOver = true;
	}
	
	/**
	 * Creates sky and ground boundaries
	 */
	private void addBoundaries() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 438f/scale);
		
		ChainShape groundShape = new ChainShape();
		groundShape.createChain(new Vector2[] {new Vector2(-25, 0), new Vector2(25, 0)});
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 1f;
		fixtureDef.restitution = 0f;
		
		groundBody = physicsWorld.createBody(bodyDef);
		groundBody.createFixture(fixtureDef);
		
		bodyDef.position.set(0, 42f/scale);
		
		skyBody = physicsWorld.createBody(bodyDef);
		skyBody.createFixture(fixtureDef);
		
		bodyDef.position.set(0, 510f/scale);
		physicsWorld.createBody(bodyDef).createFixture(fixtureDef);
		
		groundShape.dispose();
	}
	
	/**
	 * Get the x-coordinate of spawn point
	 * @return
	 */
	public float getSpawnPoint() {
		return wavePoint.getBody().getPosition().x;
	}
	
	/**
	 * Set the x-coordinate of spawn point
	 * @param spawnPoint
	 */
	public void setSpawnPoint(float spawnPoint) {
		wavePoint.getBody().setTransform(spawnPoint / scale, 0f, 0f);
	}
	
	/**
	 * Update the physics engine and create new waves
	 * @param delta: Time interval between updates
	 */
	public void update(float delta) {
		checkTime();
		physicsWorld.step(delta, 8, 3);
		
		if (newWave) {
			createWave();
			newWave = false;
		}
		removeObjects();
	}
	
	private void checkTime() {
		System.out.println(TimeManager.getSecondsTime());
		if (Config.timed && TimeManager.getSecondsTime() > Config.timeLimit) {
			setGameOver();
		}
	}
	
	/**
	 * Helper method of update(float delta).
	 * Responsible for creating a new wave of obstacles/rewards based on whether or not
	 * sufficient time (waveTime) has passed since the last wave.
	 */
	private void createWave() {
		ArrayList<GameObject> wavePositions = objectMaker.createWave();
		wave.addAll(wavePositions);
		DataPoster.createdWave(player.getY(), wavePositions, getScore());
	}
	
	/**
	 * Remove objects that have left the screen
	 */
	private void removeObjects() {
		for (Iterator<GameObject> iterator = wave.iterator(); iterator.hasNext();) {
		    GameObject obj = iterator.next();
		    if ((obj.getBody().getPosition().x * scale) < -5 || obj.checkRemove()) {
				physicsWorld.destroyBody(obj.getBody());
				iterator.remove();
			}
		}
	}
	
	/**
	 * Dispose of game elements
	 */
	public void dispose() {
		for (Iterator<GameObject> iterator = wave.iterator(); iterator.hasNext();) {
		    GameObject obj = iterator.next();
			physicsWorld.destroyBody(obj.getBody());
			iterator.remove();
		}
		physicsWorld.destroyBody(player.getBody());
		physicsWorld.destroyBody(wavePoint.getBody());
		physicsWorld.dispose();
	}
	
	/**
	 * Get the player object
	 * @return player object
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Get ground Box2D body
	 * @return ground body
	 */
	public Body getGroundBody() {
		return groundBody;
	}
	
	/**
	 * Get sky Box2D body
	 * @return sky body
	 */
	public Body getSkyBody() {
		return skyBody;
	}
	
	/**
	 * Get wave of gameObjects
	 * @return ArrayList of gameObjects
	 */
	public ArrayList<GameObject> getWave() {
		return wave;
	}
	
	/**
	 * Get the Box2D world
	 * @return Box2D world
	 */
	public World getWorld() {
		return physicsWorld;
	}
	
	/**
	 * Get player score
	 * @return player score
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Get width of sky and ground boundaries
	 * @return Width of boundaries
	 */
	public float getBoundaryWidth() {
		return BOUNDARY_WIDTH;
	}
	
}
