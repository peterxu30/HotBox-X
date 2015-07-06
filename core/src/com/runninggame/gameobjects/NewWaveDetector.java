package com.runninggame.gameobjects;

import com.badlogic.gdx.physics.box2d.World;

public class NewWaveDetector extends GameObject {

	public NewWaveDetector(World world, float width, float height) {
		super(world, width, height, false, false);
		setItemID(3);
	}

}
