package oldCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

import org.ho.yaml.Yaml;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.TimeUtils;
import com.runninggame.config.Config;

/**
 * GameMain is the screen where gameplay takes place.
 * This class is responsible for rendering visuals and playing audio (work in progress),
 * as well as acting as the game engine, managing both player control and obstacle/reward spawns.
 * @author Peter Xu
 *
 */
public class GameMain implements Screen {
	/* Game structure */
	private static final float screenWidth = Gdx.graphics.getWidth();
	private static final float screenHeight = Gdx.graphics.getHeight();
	private static final float topBoundary = 420f;
	private static final float bottomBoundary = 60f;
//	private static float leftBoundary = 60f;
//	private static float rightBoundary = 420f;
	
	/* Game related fields */
	private final OrthographicCamera camera = new OrthographicCamera();
	private final SpriteBatch batch = new SpriteBatch();
	private final Game game = ((Game) Gdx.app.getApplicationListener());
	private final ShapeRenderer shapeRenderer = new ShapeRenderer();
	private Map configMap;
	private BitmapFont text;
	
	/* Player related fields */
	private Player player;
	private float playerWidth = 40f;
	private float playerHeight = 40f;
	private float startingX = screenHeight/2 - playerHeight/2;
	private float startingY = 200f;
	private float playerSpeed = 10f;
	private float zonedSpeed;
	
	/* Obstacle related fields 
	 * Perhaps one day, turn obstacleCreator into objectCreator.
	 * Responsible for both creation of obstacles as well as rewards?
	 */
	private ObstacleCreator obstacleMaker;
	private final ArrayList<Obstacle_OLD> wave = new ArrayList<Obstacle_OLD>();
	private String obstacleDistribution = "uniform"; // add to obstacleCreator
	private float obstacleSpawnHeight = 700f;
	private float minObstacleWidth = 60f;
	private float obstacleSpeed = 20f;
	private boolean obstacleAccelerationBoolean = false; // add to obstacleCreator
	private float obstacleAccelerationRate = 0f; // add to obstacleCreator
	
	/* Game States */
	private State state = State.PAUSE;
	private boolean zoned = false;
	private String gameMode = "permadeath";
	private long waveTime = 1000000000;
	private long lastWaveTime;
	private long scoreTime = 75000000;
	private long lastScoreTime;
	private int score = 0;
	
	/* Work in progress */
	private int rewardValue = 1; 
	
	/** Possible Game States */
	public enum State {
		PAUSE,
		RUN,
		RESUME,
		STOPPED
	}
	
	@Override
	/**
	 * Implementation of Screen interface's show() method.
	 * Called once in the very beginning. Sets up everything 
	 * required for the game.
	 */
	public void show() {
		camera.setToOrtho(false, screenWidth, screenHeight);
		try {
			config();
		} catch (FileNotFoundException e) {
			System.out.println("config.yaml missing.");
		} catch (IOException e) {
			System.out.println("config.yaml improperly configured.");
		}
		modeSetup();
		playerSetup();
		Gdx.input.setInputProcessor(player);
		/*
		 * ObstacleCreator constructor for reference.
		 * ObstacleCreator(float boundaryWidth, float y, float ms, String obstacleImg)
		 */
		obstacleMaker = new ObstacleCreator(leftBoundary, obstacleSpawnHeight, obstacleSpeed, "img/obstacle.png");
		text = new BitmapFont();
	}
	
	/**
	 * Responsible for all game settings.
	 * Reads config.yml and sets the game settings to such.
	 * If config.yml does not exist, it is recreated from the backup with
	 * all original settings.
	 * @throws IOException
	 */
	private void config() throws IOException {
		File configFile = new File("config.yml");
		if (!configFile.exists()) {
			System.out.println("Restoring config");
			restoreConfig();
		}
		Object temp = Yaml.load(configFile);
		configMap = (Map) temp;
		
//		leftBoundary = ((Double) configMap.get("boundaryWidth")).floatValue();
//		rightBoundary = screenWidth - leftBoundary;
		playerWidth = ((Double) configMap.get("playerWidth")).floatValue();
		playerHeight = ((Double) configMap.get("playerHeight")).floatValue();
		playerSpeed = ((Double) configMap.get("playerSpeed")).floatValue();
		zonedSpeed = (screenWidth - (2 * leftBoundary)) / 3;
		startingX = screenWidth/2 - playerWidth/2;
		startingY = ((Double) configMap.get("startingY")).floatValue();
		obstacleDistribution = ((String) configMap.get("obstacleDistribution"));
		minObstacleWidth = ((Double) configMap.get("minimumObstacleWidth")).floatValue();
		obstacleSpeed = ((Double) configMap.get("obstacleSpeed")).floatValue();
		obstacleSpawnHeight = ((Double) configMap.get("obstacleSpawnHeight")).floatValue();
		obstacleAccelerationBoolean = ((Boolean) configMap.get("obstacleAccelerationBoolean"));
		obstacleAccelerationRate = ((Double) configMap.get("obstacleAccelerationRate")).floatValue();
		waveTime = ((Double) configMap.get("waveTime")).longValue(); 
		rewardValue = ((Integer) configMap.get("rewardValue"));
		gameMode = ((String) configMap.get("gameMode"));
		zoned = ((boolean) configMap.get("zoned"));
		scoreTime = ((Double) configMap.get("scoreTime")).longValue();
	}
	
	/**
	 * Method currently in progress. Maybe just keep backup file outside of jar.
	 * Remakes the config.yml file from a backup if it is lost.
	 * Restores to original settings.
	 * @throws IOException
	 */
	private void restoreConfig() throws IOException {
		File backupFile = new File("config/config.yml");
		File newFile = new File("config.yml");
		Files.copy(backupFile.toPath(), newFile.toPath());
	}
	
	/**
	 * Helper method of show().
	 * Responsible for setting up which game mode the game is being played in
	 * based on the gameMode setting in the config.yml file.
	 */
	private void modeSetup() {
		switch (gameMode) {
		case "permadeath":
			//to do
			break;
		case "penalty":
			//to do
			break;
		}
	}
	
	/**
	 * Helper method of show().
	 * Responsible for player object construction with the appropriate
	 * parameters based on the config.yml file.
	 */
	private void playerSetup() {
		float speed = playerSpeed;
		if (zoned) {
			speed = zonedSpeed; 
		}
		/* 
		 * Player constructor for reference.
		 * Player(float x, float y, float width, float height, float ms, boolean zone, String playerImg) 
		 */
		player = new Player(startingX, startingY, playerWidth, playerHeight, speed, zoned, "img/player.png");
	}
	
	/**
	 * Implementation of Screen interface's show() method.
	 * render(float delta) is called constantly by the game to quickly
	 * refresh the graphics and react accordingly to player input.
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(198/255f, 198/255f, 198/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		switch (state) {
		case RUN:
			if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				pause();
			}
			updateMotion();
			collisionUpdate();
			checkBounds();
			updateScore();
			newWave();
			break;
		case PAUSE:
			if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				resume();
			} 
			break;
		}
		draw();
		
	}
	
	/**
	 * Helper method of render(float delta).
	 * Responsible for checking if any collisions occur between the player and an obstacle/reward.
	 * In the event of an object-player collision, this method will react accordingly based on
	 * the object type the player collided with.
	 */
	private void collisionUpdate() {
		for (Obstacle_OLD obs : wave) {
			if (Intersector.overlaps(player.hitBox, obs)) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new GameMain());
			}
		}
	}
	
	/**
	 * Helper method of render(float delta).
	 * Responsible for updating player and obstacle locations based on their respective
	 * movement speeds. updateMotion() removes any obstacles that have left the play field.
	 */
	private void updateMotion() {
		player.updateMotion();
		for (Obstacle_OLD obs: wave) {
			obs.updateMotion();
			if ((obs.y + (obs.height / 2)) < 0f) {
				obs.dispose();
			}
		}
	}
	
	/**
	 * Helper method of render(float delta).
	 * Responsible for all drawing done in render(float delta).
	 */
	private void draw() {
		batch.begin();
		text.draw(batch, Integer.toString(score), 50f, 200f);
		if (state == State.PAUSE) {
			text.draw(batch, "Paused", screenWidth/2 - 30, 450);
		}
		player.playerSprite.draw(batch);
		for (Obstacle_OLD obs : wave) {
			obs.obstacleSprite.draw(batch);
		}
		batch.end();
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(0f, topBoundary, screenWidth, 800f - topBoundary);
		shapeRenderer.end();
	}
	
	/**
	 * Helper method of render(float delta).
	 * Responsible for creating a new wave of obstacles/rewards based on whether or not
	 * sufficient time (waveTime) has passed since the last wave.
	 */
	private void newWave() {
		if (TimeUtils.nanoTime() - lastWaveTime > waveTime) {
			wave.addAll(obstacleMaker.createObstacleWave(zoned));
			lastWaveTime = TimeUtils.nanoTime();
		}
	}
	
	private void updateScore() {
		if (TimeUtils.nanoTime() - lastScoreTime > scoreTime) {
			score += 1;
			lastScoreTime = TimeUtils.nanoTime();
		}
	}
	
	/**
	 * Checks if the player is out of bounds.
	 * Resets player's x-coordinate to keep player within bounds. 
	 */
	public void checkBounds() {
		if (player.getX() < leftBoundary) {
			if (zoned) {
				player.setX(leftBoundary + playerWidth);
			} else {
				player.setX(leftBoundary);
			}
		} 
		if (player.getX() + playerWidth > rightBoundary) {
			if (zoned) {
				player.setX(rightBoundary - (2 * playerWidth));
			} else {
				player.setX(rightBoundary - playerWidth);
			}
		}
	}
	
	/**
	 * Implementation of Screen interface's resize() method.
	 * Not in use as of now.
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Implementation of Screen interface's pause() method.
	 * Sets the game state (state) to State.PAUSE, which affects 
	 * the render(float delta) method accordingly.
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		state = State.PAUSE;
	}
	
	/**
	 * Implementation of Screen interface's resume() method.
	 * Sets the game state (state) to State.RUN, which affects
	 * the render(float delta) method accordingly.
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		state = State.RUN;
	}
	
	/**
	 * Implementation of Screen interface's hide() method.
	 * Not in use as of now.
	 */
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
	
	@Override
	/**
	 * Implementation of Screen interface's dispose() method.
	 * dispose() is called when the program is exited. It removes all game related objects
	 * that Java's built in garbage collector cannot handle itself. 
	 */
	public void dispose() {
		// TODO Auto-generated method stub
		batch.dispose();
		player.playerSprite.getTexture().dispose();
		for (Obstacle_OLD obs : wave) {
			obs.dispose();
		}
		text.dispose();
	}

}
