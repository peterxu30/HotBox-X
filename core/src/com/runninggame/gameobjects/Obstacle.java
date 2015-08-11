package com.runninggame.gameobjects;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Obstacle class. Players must avoid colliding with these to avoid penalties.
 * Obstacle item ID is 1.
 * @author Peter
 *
 */
public class Obstacle extends GameObject {
	
	/**
	 * Class constructor
	 * @param world: Box2D world
	 * @param width: Width of obstacle in Box2D units (0 width)
	 * @param height: Height of obstacle in Box2D units (0 height)
	 */
	public Obstacle(World world, float width, float height) {
		super(world, width, height, true, true);
		setItemID(1);
	}
	
}
