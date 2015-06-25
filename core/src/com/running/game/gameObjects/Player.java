package com.running.game.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.running.game.helpers.AssetLoader;

public class Player {

	private BodyDef playerBodyDef;
	private FixtureDef playerFixtureDef;
	private Body playerBody;
	
	private float scale;
	
	private float upYSpeed;
	
	public Player(World world, float x, float y, float upYSpeed, float scale) {
		this.upYSpeed = upYSpeed;
		this.scale = scale;
		
		playerBodyDef = new BodyDef();
		playerBodyDef.type = BodyDef.BodyType.DynamicBody;
		playerBodyDef.position.set(x, y);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(AssetLoader.playerTexture.getWidth() / (2 * scale), AssetLoader.playerTexture.getHeight() / (2 * scale));
		
		playerFixtureDef = new FixtureDef();
		playerFixtureDef.shape = shape; 
		playerFixtureDef.density = 1f;
		playerFixtureDef.friction = 0f;
		playerFixtureDef.isSensor = false;
		
		playerBody = world.createBody(playerBodyDef);
		playerBody.createFixture(playerFixtureDef);
		
		shape.dispose();
	}
	
	public float getX() {
		return playerBody.getPosition().x;
	}
	
	public float getY() {
		return playerBody.getPosition().y;
	}
	
	/**
	 * Exists solely for design. There are no plans to change player dimensions.
	 * @return Width as a float.
	 */
	public float getWidth() {
		return 40f / scale;
	}
	
	/**
	 * Exists solely for design. There are no plans to change player dimensions.
	 * @return
	 */
	public float getHeight() {
		return 40f / scale;
	}
	
	public void move() {
		playerBody.setLinearVelocity(0, -upYSpeed);
//		Gdx.app.log("Player", "move");
	}
	
}
