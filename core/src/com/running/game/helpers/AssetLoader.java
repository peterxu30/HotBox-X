package com.running.game.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import org.ho.yaml.Yaml;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class AssetLoader {
	
	public static Texture playerTexture, obstacleTexture, rewardTexture; 
	private static Map configMap;
	
	public static void load() {
		playerTexture = new Texture(Gdx.files.internal("img/player.png"));
		obstacleTexture = new Texture(Gdx.files.internal("img/obstacle.png"));
		rewardTexture = new Texture(Gdx.files.internal("img/reward.png"));
	}
	
	public static void dispose() {
		playerTexture.dispose();
		obstacleTexture.dispose();
		rewardTexture.dispose();
	}
	
}
