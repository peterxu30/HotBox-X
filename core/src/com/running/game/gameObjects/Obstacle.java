package com.running.game.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Obstacle class represents all obstacles in the game.
 * Obstacle class is not responsible for creating itself, that
 * is taken care of by the ObstacleCreator class.
 * @author Peter Xu
 *
 */
public class Obstacle extends Rectangle {
	private Texture obstacleImage;
	public Sprite obstacleSprite;
	private float moveSpeed;
	
	/**
	 * Obstacle constructor. Width and height should be whole numbers!
	 * @param x Starting x-coordinate
	 * @param y Starting y-coordinate
	 * @param width Width of obstacle
	 * @param height Height of obstacle
	 * @param ms Movement speed of obstacle
	 * @param obstacleImg Obstacle image
	 */
	public Obstacle(float x, float y, float width, float height, float ms, String obstacleImg) {
		super(x, y, width, height);
		obstacleImage = new Texture(Gdx.files.internal(obstacleImg));
		obstacleSprite = new Sprite(obstacleImage, ((int) width), ((int) height));
		obstacleSprite.setPosition(x, y);
		moveSpeed = ms;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public float getWidth() {
		return this.width;
	}
	
	public float getHeight() {
		return this.height;
	}
	
	public void updateMotion() {
		this.y -= moveSpeed;
		obstacleSprite.setPosition(this.x, this.y);
	}
	
	public void dispose() {
		obstacleSprite.getTexture().dispose();
	}
	
}
