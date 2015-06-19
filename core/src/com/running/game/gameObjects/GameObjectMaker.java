package com.running.game.gameObjects;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.badlogic.gdx.Gdx;

public class GameObjectMaker {
	
	private float zoneHeight = Gdx.graphics.getHeight() / 12;
	private float minHeight = zoneHeight;
	
	public GameObjectMaker(float spawnX, float width, float ms, int maxNumberOfObstacles) {
		
	}
	
	public ArrayList<GameObject> createWave() {
		return new ArrayList<GameObject>();
	}
	
}
