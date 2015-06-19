package oldCode;

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
public class Obstacle_OLD extends Rectangle {
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
	public Obstacle_OLD(float x, float y, float width, float height, float ms, String obstacleImg) {
		super(x, y, width, height);
		obstacleImage = new Texture(Gdx.files.internal(obstacleImg));
		obstacleSprite = new Sprite(obstacleImage, ((int) width), ((int) height));
		obstacleSprite.setPosition(x, y);
		moveSpeed = ms;
	}
	
	@Override
	public Rectangle setX(float x) {
		this.x = x;
		obstacleSprite.setPosition(x, y);
		return this;
	}
	
	@Override
	public Rectangle setY(float y) {
		this.y = y;
		obstacleSprite.setPosition(x, y);
		return this;
	}
	
	public float getWidth() {
		return this.width;
	}
	
	@Override
	public Rectangle setWidth(float w) {
		width = w;
		obstacleSprite = new Sprite(obstacleImage, ((int) width), ((int) height));
		obstacleSprite.setPosition(x, y);
		return this;
	}
	
	public float getHeight() {
		return this.height;
	}
	
	@Override
	public Rectangle setHeight(float h) {
		height = h;
		obstacleSprite = new Sprite(obstacleImage, ((int) width), ((int) height));
		obstacleSprite.setPosition(x, y);
		return this;
	}
	
	public void updateMotion() {
		this.y -= moveSpeed;
		obstacleSprite.setPosition(this.x, this.y);
	}
	
	public void dispose() {
		obstacleSprite.getTexture().dispose();
	}
	
}
