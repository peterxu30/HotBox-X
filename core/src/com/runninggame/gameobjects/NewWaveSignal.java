package com.runninggame.gameobjects;

import com.badlogic.gdx.physics.box2d.World;

public class NewWaveSignal extends GameObject {

	public NewWaveSignal(World world, float width, float height) {
		super(world, width, height, true, true);
		setItemID(4);
	}

}
