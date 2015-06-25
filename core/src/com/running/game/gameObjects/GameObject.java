package com.running.game.gameObjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.running.game.helpers.AssetLoader;

public abstract class GameObject {
	
	public class Obstacle {

	}

	private float width;
	private float height;
	
	private Body objectBody;
	
	public GameObject(World world, float x, float y, float width, float height, boolean sensor) {
		this.width = width;
		this.height = height;
		
		BodyDef objectBodyDef = new BodyDef();
		objectBodyDef.type = BodyDef.BodyType.KinematicBody;
		objectBodyDef.position.set(x, y);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		
		FixtureDef objectFixtureDef = new FixtureDef();
		objectFixtureDef.shape = shape;
		objectFixtureDef.isSensor = sensor;
		objectFixtureDef.density = 1f;
		objectFixtureDef.friction = 0f;
		
		objectBody = world.createBody(objectBodyDef);
		objectBody.createFixture(objectFixtureDef);
		objectBody.setUserData(new GameObjectData());
		
		shape.dispose();
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public Body getBody() {
		return objectBody;
	}
	
	public void setSpeed(float speed) {
		objectBody.setLinearVelocity(-speed, 0);
	}
	
	/* 0 - player, 1 - obstacle, 2 - reward */
	public void setItemID(int id) {
		((GameObjectData) objectBody.getUserData()).setID(id);
	}
	
	public int getItemID() {
		return ((GameObjectData) objectBody.getUserData()).getID();
	}
	
	public boolean checkRemove() {
		GameObjectData data = (GameObjectData) objectBody.getUserData();
		return data.checkRemove();
	}
	
	public class GameObjectData {
		
		private int ITEM_ID;
		private boolean remove = false;
		
		public void setID(int id) {
			ITEM_ID = id;
		}
		
		public int getID() {
			return ITEM_ID;
		}
		
		public void markRemove() {
			remove = true;
		}
		
		public boolean checkRemove() {
			return remove;
		}
		
	}
	
}
