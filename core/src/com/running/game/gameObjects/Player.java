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
	
	private float upYSpeed;
	
	public Player(World world, float x, float y, float upYSpeed) {
		this.upYSpeed = upYSpeed;
		
		playerBodyDef = new BodyDef();
		playerBodyDef.type = BodyDef.BodyType.DynamicBody;
		playerBodyDef.position.set(x, y);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(AssetLoader.playerTexture.getWidth() / 2, AssetLoader.playerTexture.getHeight() / 2);
		
		playerFixtureDef = new FixtureDef();
		playerFixtureDef.shape = shape; 
		playerFixtureDef.density = 1f;
		
		playerBody = world.createBody(playerBodyDef);
		playerBody.createFixture(playerFixtureDef);
	}
	
	public BodyDef getBodyDef() {
		return playerBodyDef;
	}
	
	public FixtureDef getFixtureDef() {
		return playerFixtureDef;
	}
	
	public float getX() {
		return playerBody.getPosition().x;
	}
	
	public float getY() {
		return playerBody.getPosition().y;
	}
	
}
