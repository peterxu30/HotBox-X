package com.runninggame.gameobjects;

import com.badlogic.gdx.physics.box2d.World;

public class Reward extends GameObject {
	
	public Reward(World world, float width, float height) {
		super(world, width, height, true, true);
		setItemID(2);
	}
}
