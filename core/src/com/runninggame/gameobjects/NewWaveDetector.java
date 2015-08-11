package com.runninggame.gameobjects;

import com.badlogic.gdx.physics.box2d.World;

/**
 * When this object collides with a NewWaveSignal (which is included in all waves), 
 * a new wave will spawn. Enables wave spawning based on position and not time.
 * NewWaveDetector item ID is 3.
 * @author Peter
 *
 */
public class NewWaveDetector extends GameObject {
	
	/**
	 * Class constructor
	 * @param world: Box2D world
	 * @param width: Width of newWaveDetector in Box2D units
	 * @param height: Height of newWaveDetector in Box2D units
	 */
	public NewWaveDetector(World world, float width, float height) {
		super(world, width, height, false, false);
		setItemID(3);
	}

}
