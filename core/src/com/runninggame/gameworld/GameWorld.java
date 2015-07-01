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
	private long lastWaveTime;
	private long waveTime;
	
	private int score;
	
	public GameWorld(float gravityY, float scale, long waveTime, String gameMode) {
		this.scale = scale;
		this.waveTime = waveTime;
		if ("permadeath".equals(gameMode)) {
			this.permadeath = true;
		}
		
		physicsWorld = new World(new Vector2(0, gravityY), true);
		setContactListener();
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
	}
	
	private void setContactListener() {
		physicsWorld.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Body bodyA = contact.getFixtureA().getBody();
				Body bodyB = contact.getFixtureB().getBody();
				
				GameObject.GameObjectData dataA = (GameObject.GameObjectData) bodyA.getUserData();
				GameObject.GameObjectData dataB = (GameObject.GameObjectData) bodyB.getUserData();
				GameObject.GameObjectData gameObjectData = dataB;
				
				if (dataA != null && dataA.getID() != 0) {
					gameObjectData = dataA;
				}
				
				if (gameObjectData != null && (int) gameObjectData.getID() == 2) {
					score += Config.rewardValue;
					gameObjectData.markRemove();
				} else if (gameObjectData != null && gameObjectData.getID() == 1) {
					if (permadeath) {
						setGameOver();
					} else {
						if (score > Config.minScore) {
							score -= Config.penaltyValue;
						}
					}
					gameObjectData.markRemove();
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

		fixtureDef.friction = 1f;
		fixtureDef.restitution = 0f;
		
		skyBody = physicsWorld.createBody(bodyDef);
		skyBody.createFixture(fixtureDef);
		
		groundShape.dispose();
	}
	
	public void setWaveTime(long time) {
		waveTime = time;
	}
	
	public void update(float delta) {
		physicsWorld.step(delta, 6, 2);
		newWave();
		removeObjects();
		//test
//		if (player.isMoving()) {
//			player.move();
//		}
	}
	
	/**
	 * Helper method of update(float delta).
	 * Responsible for creating a new wave of obstacles/rewards based on whether or not
	 * sufficient time (waveTime) has passed since the last wave.
	 */
	private void newWave() {
		if (TimeUtils.millis() - lastWaveTime > waveTime) {
			wave.addAll(objectMaker.createWave());
			lastWaveTime = TimeUtils.millis();
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
