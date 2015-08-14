package com.running.game.gameobjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * GameObject abstract class. Parent class of Player, NewWaveDetector, 
 * NewWaveSignal, Obstacle, and Reward classes.
 * @author Peter
 * 
 */
public abstract class GameObject {
	/** Box2D body of gameObject for physics */
	protected Body body;
	
	/** Width of object in Box2D units */
	private float width;
	
	/** Height of object in Box2D units */
	private float height;
	
	/**
	 * Class constructor. All values are in Box2D units
	 * @param world: Box2D world that this object belongs in
	 * @param width: Width of object
	 * @param height: Height of object
	 * @param kinematic: Is object kinematic or dynamic
	 * @param sensor: Is object Box2D sensor
	 */
	public GameObject(World world, float width, float height, boolean kinematic, boolean sensor) {
		this.width = width;
		this.height = height;
		
		BodyDef bodyDef = new BodyDef();
		if (kinematic) {
			bodyDef.type = BodyDef.BodyType.KinematicBody;
		} else {
			bodyDef.type = BodyDef.BodyType.DynamicBody;
		}
		bodyDef.position.set(18f, 6f);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2f, height / 2f);
		
		FixtureDef objectFixtureDef = new FixtureDef();
		objectFixtureDef.shape = shape;
		objectFixtureDef.isSensor = sensor;
		objectFixtureDef.density = 1f;
		objectFixtureDef.friction = 0f;
		
		body = world.createBody(bodyDef);
		body.createFixture(objectFixtureDef);
		body.setUserData(new GameObjectData());
		body.setLinearVelocity(-6f, 0f);
		
		shape.dispose();
	}
	
	/**
	 * Set position of object in Box2D units
	 * @param x: x-coordinate
	 * @param y: y-coordinate
	 * @return current gameObject
	 */
	public GameObject setPosition(float x, float y) {
		body.setTransform(x, y, 0f);
		return this;
	}
	
	/**
	 * Set speed of object in Box2D units
	 * @param speed: Speed value
	 * @return current gameObject
	 */
	public GameObject setSpeed(float speed) {
		body.setLinearVelocity(-speed, 0f);
		return this;
	}
	
	/**
	 * Get Box2D body of current gameObject
	 * @return Box2D body
	 */
	public Body getBody() {
		return body;
	}
	
	/**
	 * Get width of current gameObject in Box2D units
	 * @return width in Box2D units
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Get height of current gameObject in Box2D units
	 * @return height in Box2D units
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * Get x-coordinate of current gameObject's position in Box2D units
	 * @return x-coordinate in Box2D units
	 */
	public float getX() {
		return body.getPosition().x;
	}
	
	/**
	 * Get y-coordinate of current gameObject's position in Box2D units
	 * @return y-coordinate in Box2D units
	 */
	public float getY() {
		return body.getPosition().y;
	}
	
	/**
	 * Set ID that identifies what type of GameObject child class current gameObject is
	 * 0 - player, 1 - obstacle, 2 - reward, 3 - NewWaveDetector
	 * @param id: ID of current gameObject
	 */
	public void setItemID(int id) {
		((GameObjectData) body.getUserData()).setID(id);
	}
	
	/**
	 * Get ID of current gameObject
	 * @return ID of current gameObject
	 */
	public int getItemID() {
		return ((GameObjectData) body.getUserData()).getID();
	}
	
	/**
	 * Check if current gameObject should be removed by Box2D world
	 * Used in checkRemove in GameWorld
	 * @return boolean
	 */
	public boolean checkRemove() {
		return ((GameObjectData) body.getUserData()).checkRemove();
	}
	
	/**
	 * Nested class that contains data for GameObjects.
	 * Box2D body can set user data. This subclass is that user data.
	 * @author Peter
	 *
	 */
	public class GameObjectData {
		
		/** Same ID as gameObject's */
		private int itemID;
		
		/** Should current gameObject be removed */
		private boolean remove;
		
		/**
		 * Set item ID. May seem redundant but is necessary for collision detection.
		 * @param id: ID value
		 */
		public void setID(int id) {
			itemID = id;
		}
		
		/**
		 * Get item ID. May seem redundant but is necessary for collision detection.
		 * @return ID value
		 */
		public int getID() {
			return itemID;
		}
		
		/**
		 * Mark current gameObject for removal
		 * Used in ContactListener of GameWorld class
		 */
		public void markRemove() {
			remove = true;
		}
		
		/**
		 * Check if current gameObject should be removed
		 * @return boolean
		 */
		public boolean checkRemove() {
			return remove;
		}
	}
	
}
