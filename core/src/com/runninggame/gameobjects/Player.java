package com.runninggame.gameobjects;

import com.badlogic.gdx.physics.box2d.World;

public class Player extends GameObject {

	private float upYSpeed;
	
	public Player(World world, float width, float height) {
		super(world, width, height, false, false);
		body.setLinearVelocity(0f, 0f);
		setSpeed(7.3f);
		setItemID(0);
	}
	
	@Override
	public Player setSpeed(float speed) {
		upYSpeed = speed;
		return this;
	}
	
	public void move() {
		body.setLinearVelocity(0f, -upYSpeed);
	}
	
	public void stopMove() {
		body.setLinearVelocity(0f, 0f);
	}
}
