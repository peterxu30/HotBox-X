package com.running.game.gameObjects;

import com.badlogic.gdx.physics.box2d.World;

public class Obstacle extends GameObject {

	public Obstacle(World world, float x, float y, float width, float height) {
		super(world, x, y, width, height, true);
		setItemID(1);
	}
	
}
