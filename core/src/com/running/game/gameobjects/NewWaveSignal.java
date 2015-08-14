package com.running.game.gameobjects;

import com.badlogic.gdx.physics.box2d.World;

/**
 * NewWaveSignal helps trigger when to spawn a new wave. A newWaveSignal is spawned in every wave.
 * When a newWaveSignal collides with the newWaveDetector, a new wave is spawned.
 * NewWaveSignal item ID is 4.
 * @author Peter
 *
 */
public class NewWaveSignal extends GameObject {
	
	/**
	 * Class constructor.
	 * @param world: Box2D world
	 * @param width: Width of newWaveSignal in Box2D units (0 width)
	 * @param height: Height of newWaveSignal in Box2D units (0 height)
	 */
	public NewWaveSignal(World world, float width, float height) {
		super(world, width, height, true, true);
		setItemID(4);
	}

}
