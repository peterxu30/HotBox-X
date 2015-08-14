package com.running.game.gameobjects;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Reward class. Players collide with these objects to increase score.
 * Reward item ID is 2.
 * @author Peter
 *
 */
public class Reward extends GameObject {
	
	/**
	 * 
	 * @param world
	 * @param width
	 * @param height
	 */
	public Reward(World world, float width, float height) {
		super(world, width, height, true, true);
		setItemID(2);
	}
	
}
