package com.runninggame.gameobjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class GameObject {
	
	protected Body body;
	private float width;
	private float height;
	
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
	
	public GameObject setPosition(float x, float y) {
		body.setTransform(x, y, 0f);
		return this;
	}
	
	public GameObject setSpeed(float speed) {
		body.setLinearVelocity(-speed, 0f);
		return this;
	}
	
	public Body getBody() {
		return body;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getX() {
		return body.getPosition().x;
	}
	
	public float getY() {
		return body.getPosition().y;
	}
	
	/* 0 - player, 1 - obstacle, 2 - reward */
	public void setItemID(int id) {
		((GameObjectData) body.getUserData()).setID(id);
	}
	
	public int getItemID() {
		return ((GameObjectData) body.getUserData()).getID();
	}
	
	/* Used in checkRemove in GameWorld */
	public boolean checkRemove() {
		return ((GameObjectData) body.getUserData()).checkRemove();
	}
	
	public class GameObjectData {
		
		private int itemID;
		private boolean remove;
		
		public void setID(int id) {
			itemID = id;
		}
		
		public int getID() {
			return itemID;
		}
		
		/* Used in ContactListener */
		public void markRemove() {
			remove = true;
		}
		
		public boolean checkRemove() {
			return remove;
		}
	}
}
