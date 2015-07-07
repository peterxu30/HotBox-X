package com.runninggame.gameworld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.TimeUtils;
import com.runninggame.gameobjects.GameObject;
import com.runninggame.gameobjects.GameObjectMaker;
import com.runninggame.gameobjects.NewWaveDetector;
import com.runninggame.gameobjects.Obstacle;
import com.runninggame.gameobjects.Player;
import com.runninggame.gameobjects.Reward;
import com.runninggame.helpers.AssetLoader;
import com.runninggame.helpers.Config;
import com.runninggame.screens.GameScreen;
import com.runninggame.screens.Splash;

public class GameWorld {
	
	private World physicsWorld;
	private float gravity;
	private float scale;
	private boolean permadeath;
	private boolean gameOver;
	
	private Player player;
	private float playerWidth;
	private float playerHeight;
	private Body groundBody;
	private Body skyBody;
	private final float BOUNDARY_WIDTH = 42f;
	private GameObjectMaker objectMaker;
	
	private ArrayList<GameObject> wave;
	private boolean newWave;
	private float spawnPoint;
	private int score;
	
	public NewWaveDetector wavePoint;
	
	public GameWorld(float gravityY, float scale, float spawnPoint, String gameMode) {
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
			.setSpeed(Config.objectSpeed);
		
		newWave = true;
	}
	
	private void setContactListener() {
		physicsWorld.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Body bodyB = contact.getFixtureB().getBody();
				GameObject.GameObjectData dataB = (GameObject.GameObjectData) bodyB.getUserData();
				
				if (dataB.getID() == 4) {
					newWave = true;
				} 
				if (dataB.getID() == 2) {
					score += Config.rewardValue;
					dataB.markRemove();
				} else if (dataB.getID() == 1) {
					if (permadeath) {
						setGameOver();
					} else {
						if (score > Config.minScore) {
							score -= Config.penaltyValue;
						}
					}
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
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public void setGameOver() {
		gameOver = true;
	}
	
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
	
	public float getSpawnPoint() {
		return wavePoint.getBody().getPosition().x;
	}
	
	public void setSpawnPoint(float spawnPoint) {
		wavePoint.getBody().setTransform(spawnPoint / scale, 0f, 0f);
	}
	
	public void update(float delta) {
		physicsWorld.step(delta, 8, 3);
		createWave();
		removeObjects();
	}
	
	/**
	 * Helper method of update(float delta).
	 * Responsible for creating a new wave of obstacles/rewards based on whether or not
	 * sufficient time (waveTime) has passed since the last wave.
	 */
	private void createWave() {
		if (newWave) {
			wave.addAll(objectMaker.createWave());
			newWave = false;
		}
	}
	
	private void removeObjects() {
		for (Iterator<GameObject> iterator = wave.iterator(); iterator.hasNext();) {
		    GameObject obj = iterator.next();
		    if ((obj.getBody().getPosition().x * scale) < -10 || obj.checkRemove()) {
				physicsWorld.destroyBody(obj.getBody());
				iterator.remove();
			}
		}
	}
	
	public void dispose() {
		Gdx.app.log("GameWorld", "Dispose");
		for (Iterator<GameObject> iterator = wave.iterator(); iterator.hasNext();) {
		    GameObject obj = iterator.next();
			physicsWorld.destroyBody(obj.getBody());
			iterator.remove();
		}
		physicsWorld.destroyBody(player.getBody());
		physicsWorld.destroyBody(wavePoint.getBody());
		physicsWorld.dispose();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Body getGroundBody() {
		return groundBody;
	}
	
	public Body getSkyBody() {
		return skyBody;
	}
	
	public ArrayList<GameObject> getWave() {
		return wave;
	}
	
	public World getWorld() {
		return physicsWorld;
	}
	
	public int getScore() {
		return score;
	}
	
	public float getBoundaryWidth() {
		return BOUNDARY_WIDTH;
	}
}
