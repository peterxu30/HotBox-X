package oldCode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

/**
 * Player class. This object is what the player controls.
 * The Player class differs from other objects in that it
 * extends InputAdapter, which handles controlling the player,
 * instead of extending Rectangle. As such, it has a Rectangle
 * field to represent its hitbox.
 * @author Peter Xu
 *
 */
public class Player extends InputAdapter {
	public Texture playerImage;
	public Sprite playerSprite;
	public Rectangle hitBox;
	private boolean upMove;
	private boolean downMove;
	private float moveSpeed;
	private boolean zoned;
	
	public Player(float x, float y, float width, float height, float ms, boolean zone, String playerImg) {
		playerImage = new Texture(Gdx.files.internal(playerImg));
		hitBox = new Rectangle(x, y, width, height);
		playerSprite = new Sprite(playerImage, ((int) width), ((int) height));
		playerSprite.setPosition(hitBox.x, hitBox.y);
		moveSpeed = ms;
		zoned = zone;
	}
	
	public float getX() {
		return hitBox.x;
	}
	
	public float getY() {
		return hitBox.y;
	}
	
	public float getWidth() {
		return hitBox.width;
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		if (zoned) {
			return zoneKeyDown(keyCode);
		}
		return contKeyDown(keyCode);
	}
	
	/**
	 * Helper method for keyDown(int keyCode)
	 * Handles controlling the player for the zoned game mode.
	 * @param keyCode Key being pressed down
	 * @return boolean value indicating key pressed or not
	 */
	private boolean zoneKeyDown(int keyCode) {
		switch(keyCode) {
		case (Keys.A):
		case (Keys.LEFT):
				hitBox.x -= moveSpeed;
				break;
		case (Keys.D):
		case (Keys.RIGHT):
				hitBox.x += moveSpeed;
				break;
		default:
			return false;
		}
		return true;
	}
	
	/**
	 * Helper method for keyDown(int keyCode)
	 * Handles controlling the player for the continuous game mode.
	 * @param keyCode Key being pressed down
	 * @return boolean value indicating key pressed or not
	 */
	private boolean contKeyDown(int keyCode) {
		switch(keyCode) {
		case (Keys.A):
		case (Keys.LEFT):
			leftMove = true;
			break;
		case (Keys.D):
		case (Keys.RIGHT):
			rightMove = true;
			break;
		default:
			return false;
		}
		return true;
	}
	
	@Override
	public boolean keyUp(int keyCode) {
		switch(keyCode) {
		case (Keys.A):
		case (Keys.LEFT):
			leftMove = false;
			break;
		case (Keys.D):
		case (Keys.RIGHT):
			rightMove = false;
			break;
		default:
			return false;
		}
		return true;
	}
	
	public void updateMotion() {
		if (!zoned) {
			if (leftMove) {
				hitBox.x -= moveSpeed;
			}
			if (rightMove) {
				hitBox.x += moveSpeed;
			}
		}
		playerSprite.setPosition(hitBox.x, hitBox.y);
	}
	
	public void setX(float newX) {
		hitBox.x = newX;
		playerSprite.setPosition(hitBox.x, hitBox.y);
	}
	
}
