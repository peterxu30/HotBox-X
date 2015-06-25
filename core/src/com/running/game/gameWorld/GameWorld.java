package com.running.game.gameWorld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import oldCode.Play;

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
import com.running.game.gameObjects.GameObject;
import com.running.game.gameObjects.GameObjectMaker;
import com.running.game.gameObjects.Obstacle;
import com.running.game.gameObjects.Player;
import com.running.game.gameObjects.Reward;
import com.running.game.helpers.AssetLoader;
import com.running.game.helpers.Config;
import com.running.game.screens.GameScreen;
import com.running.game.screens.Splash;

public class GameWorld {
	
	private World physicsWorld;
	private float scale;
	private boolean gameOver;
	
	private Player player;
	private Body groundBody;
	private Body skyBody;
	private GameObjectMaker objectMaker;
	
	private ArrayList<GameObject> wave;
	
	private long lastWaveTime;
//	private long waveTime = 999990000;
	private long waveTime = Config.waveTime;
	
	private int score;
	
	public GameWorld(float gravityY) {
		gameOver = false;
		scale = Config.scale;
		physicsWorld = new World(new Vector2(0, gravityY), true);
		setContactListener();
		player = new Player(physicsWorld, Config.playerX/scale, Config.playerY/scale, Config.playerSpeed, scale);
		addBoundaries();
		wave = new ArrayList<GameObject>();
		
		//test
		objectMaker = new GameObjectMaker(physicsWorld);
	}
	
	private void setContactListener() {
		physicsWorld.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				// TODO Auto-generated method stub
				//bodyA = player
				Body bodyA = contact.getFixtureA().getBody();
				Body bodyB = contact.getFixtureB().getBody();
				
				GameObject.GameObjectData dataA = (GameObject.GameObjectData) bodyA.getUserData();
				GameObject.GameObjectData dataB = (GameObject.GameObjectData) bodyB.getUserData();
				GameObject.GameObjectData gameObjectData = dataB;
				
				if (dataA != null && dataA.getID() != 0) {
					gameObjectData = dataA;
				}
				
				if (gameObjectData != null && (int) gameObjectData.getID() == 2) {
					//Need to implement reward, score + 1
					score += Config.rewardValue;
//					System.out.println("CONTACT REWARD");
					gameObjectData.markRemove();
				} else if (gameObjectData != null && gameObjectData.getID() == 1) {
					//game Over or penalty
					//if penalty, dataA.markRemove();
//					System.out.println("CONTACT OBSTACLE");
					
					/* if (penaltyMode) {
					 * 	score -= Config.deathPenalty;
					 *  if (score <= Config.minScore) {
					 *   gameOver = true;
					 *  }
					 * } else if (deathMode) {
					 * 	gameOver = true;
					 * }
					 * gameObjectData.markRemove();
					 */
						 
					gameObjectData.markRemove();
					gameOver = true;
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
	
	private void addBoundaries() {
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 440f/scale);
		
		ChainShape groundShape = new ChainShape();
		groundShape.createChain(new Vector2[] {new Vector2(-25, 0), new Vector2(25, 0)});
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 1f;
		fixtureDef.restitution = 0f;
		
		groundBody = physicsWorld.createBody(bodyDef);
		groundBody.createFixture(fixtureDef);
		
		bodyDef.position.set(0, 40f/scale);

		fixtureDef.friction = 1f;
		fixtureDef.restitution = 0f;
		
		skyBody = physicsWorld.createBody(bodyDef);
		skyBody.createFixture(fixtureDef);
		
		groundShape.dispose();
	}
	
	private void setWaveTime(long time) {
		waveTime = time;
	}
	
	public void update(float delta) {
//		Gdx.app.log("Gameworld", "update");
		physicsWorld.step(delta, 6, 2);
		newWave();
		removeObjects();
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
}
