package com.running.game.gameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.running.game.gameObjects.Player;
import com.running.game.helpers.AssetLoader;

public class GameWorld {

	private World physicsWorld;
	
	private Player player;
	
	private Rectangle rect = new Rectangle(0, 0, 17, 12);
	
	public GameWorld(float gravityY) {
		physicsWorld = new World(new Vector2(0, gravityY), true);
//		player = new Player(physicsWorld, 40f, (Gdx.graphics.getHeight() / 2) 
//				 - (AssetLoader.playerTexture.getHeight() / 2), 100f);
		player = new Player(physicsWorld, 40f, 0, 100f);
	}
	
	public void update(float delta) {
		Gdx.app.log("Gameworld", "update");
		physicsWorld.step(delta, 6, 2);
//		rect.x += 1;
//		if (rect.x > Gdx.graphics.getWidth()) {
//			rect.x = 0;
//		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
}
