package gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

public class Player extends InputAdapter {
	public Texture playerImage;
	public Circle hitBox;
	private boolean leftMove;
	private boolean rightMove;
	
	public Player(float x, float y, float radius) {
		playerImage = new Texture(Gdx.files.internal("img/player.png"));
		hitBox = new Circle(x, y, radius);
	}
	
	public float getX() {
		return hitBox.x;
	}
	
	public float getY() {
		return hitBox.y;
	}
	
	public float getR() {
		return hitBox.radius;
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		switch(keyCode) {
		case Keys.A:
			leftMove = true;
			break;
		case Keys.D:
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
		case Keys.A:
			leftMove = false;
			break;
		case Keys.D:
			rightMove = false;
			break;
		default:
			return false;
		}
		return true;
	}
	
	public void updateMotion() {
		if (leftMove) {
			hitBox.x -= 10;
		}
		if (rightMove) {
			hitBox.x += 10;
		}
	}
	
	public void setX(float newX) {
		hitBox.x = newX - hitBox.radius;
	}
	
//	public boolean isCollision(Obstacle obs) {
		// TO DO
//	}
}
