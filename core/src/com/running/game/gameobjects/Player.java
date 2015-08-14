package com.running.game.gameobjects;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Player object class. This class is static in the x-axis 
 * so setSpeed deals only with vertical velocity.
 * Player item ID is 0.
 * @author Peter
 *
 */
public class Player extends GameObject {
	
	/** Increase in Y velocity when player jumps */
	private float upYSpeed;
	
	/**
	 * Class constructor.
	 * @param world: Box2D world
	 * @param width: Width of newWaveSignal in Box2D units (0 width)
	 * @param height: Height of newWaveSignal in Box2D units (0 height)
	 */
	public Player(World world, float width, float height) {
		super(world, width, height, false, false);
		body.setLinearVelocity(0f, 0f);
		setSpeed(7.3f);
		setItemID(0);
	}
	
	/**
	 * Set the upYSpeed of the player.
	 */
	@Override
	public Player setSpeed(float speed) {
		upYSpeed = speed;
		return this;
	}
	
	/**
	 * Called when player jumps. Performs the change in y-velocity.
	 */
	public void move() {
		body.setLinearVelocity(0f, -upYSpeed);
	}
	
}
