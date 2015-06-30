package com.running.game.gameobjects;

import com.badlogic.gdx.physics.box2d.World;

public class Obstacle extends GameObject {

	public Obstacle(World world, float width, float height) {
		super(world, width, height, true, true);
		setItemID(1);
	}
}
