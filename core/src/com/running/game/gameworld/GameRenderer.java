package com.running.game.gameworld;

import java.math.BigDecimal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.running.game.gameobjects.GameObject;
import com.running.game.gameobjects.Player;
import com.running.game.utilities.AssetLoader;
import com.running.game.utilities.Config;
import com.running.game.utilities.TimeManager;

/**
 * GameRenderer performs the all of the game's visual rendering.
 * Created in GameScreen class. 
 * @author Peter
 *
 */
public class GameRenderer {
	
	/** GameWorld of current game. Game logic is taken care of the gameWorld */
	private GameWorld gameWorld;
	
	/** LibGDX camera for the game. Determines what to display on screen. */
	private OrthographicCamera camera;
	
	/** LibGDX viewport. Relevant in resizing the window. */
	private Viewport viewport;
	
	/** Responsible for drawing sprites and textures */
	private SpriteBatch batch;
	
	/** Font for score text */
	private BitmapFont font;
	
	/** Starting Y-coordinate of in-game text. */
    private float textStartY = 50f;
    
    /** Vertical gap between lines of text */
    private float textGapHeight = 20f;
	
	/** Conversion scale from Box2D units to pixels */
	private float scale;
	
	/** Width of screen in pixels */
	private float width;
	
	/** Height of screen in pixels */
	private float height;
	
	/** X-coordinate of ground and sky bodies */
	private float boundaryX;
	
	/** Y-coordinate of ground body */
	private float groundY;
	
	/** Y-coordinate of sky body */
	private float skyY;
	
	/** Player object */
	private Player player;
	
	/**
	 * Class constructor
	 * @param gameWorld: Current gameWorld object
	 * @param width: Width of screen in pixels
	 * @param height: Height of screen in pixels
	 * @param scale: Conversion scale from Box2D units to pixels
	 */
	public GameRenderer(GameWorld gameWorld, float width, float height, float scale) {
		this.gameWorld = gameWorld;
		this.scale = scale;
		this.width = width;
		this.height = height;
		
		font = new BitmapFont(true);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(true, width, height);
		viewport = new FitViewport(width, height, camera);
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		player = gameWorld.getPlayer();
		
		boundaryCalculations();
	}
	
	/**
	 * Calculates the coordinates of the ground and sky bodies, which are
	 * the boundaries of the playable portion of the screen.
	 */
	private void boundaryCalculations() {
		boundaryX = gameWorld.getGroundBody().getPosition().x * scale;
		groundY = gameWorld.getGroundBody().getPosition().y * scale;
		skyY = gameWorld.getSkyBody().getPosition().y * scale - 42f;
	}
	
	/**
	 * Main render method for rendering visuals.
	 * @param delta
	 */
	public void render(float delta) {
		Gdx.gl.glClearColor(96 / 275f, 96 / 275f, 96 / 275f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        drawPlayer();
        drawObjects();
        drawBoundaries();
        drawText();
        batch.end();
	}
	
	/**
	 * Helper method for render. Draws the player character.
	 */
	private void drawPlayer() {
		float playerX = (player.getX() - (player.getWidth() / 2)) * scale;
		float playerY = (player.getY() - (player.getHeight() / 2)) * scale;
		float playerW = player.getWidth() * scale;
		float playerH = player.getHeight() * scale;
		batch.draw(AssetLoader.playerTexture, playerX, playerY, playerW, playerH);
	}
	
	/**
	 * Helper method for render. Draws the ground and sky.
	 */
	private void drawBoundaries() {
		batch.draw(AssetLoader.boundaryTexture, boundaryX, groundY, width, gameWorld.getBoundaryWidth());
        batch.draw(AssetLoader.boundaryTexture, boundaryX, skyY, width, gameWorld.getBoundaryWidth());
	}
	
	/**
	 * Helper method for render. Draws object waves (obstacles and rewards).
	 */
	private void drawObjects() {
		for (GameObject obj : gameWorld.getWave()) {
        	float objX = (obj.getX() - (obj.getWidth() / 2)) * scale;
        	float objY = (obj.getY() - (obj.getHeight() / 2)) * scale;
        	float objWidth = obj.getWidth() * scale;
        	float objHeight = obj.getHeight() * scale;
        	if (obj.getItemID() == 1) {
        		batch.draw(AssetLoader.obstacleTexture, objX, objY, objWidth, objHeight);
        	} else if (obj.getItemID() == 2) {
        		batch.draw(AssetLoader.rewardTexture, objX, objY, objWidth, objHeight);
        	}
        }
	}
	
	/**
	 * Draws all the in-game text.
	 */
	private void drawText() {
		int count = 0;
		font.draw(batch, "Score: " + Integer.toString(gameWorld.getScore()), 100f, incrementTextY(count++));
        if ("penalty".equals(Config.gameMode)) {
        	font.draw(batch, "Mode: penalty -" + Config.penaltyValue, 100f, incrementTextY(count++));
        	font.draw(batch, "Min Score: " + Config.minScore, 100f, incrementTextY(count++));
        } else {
        	font.draw(batch, "Mode: " + Config.gameMode, 100f, incrementTextY(count++));
        }
        if (Config.timed) {
        	BigDecimal bd = new BigDecimal(Config.timeLimit - TimeManager.getSecondsTime());
        	bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        	double time = bd.doubleValue();
            font.draw(batch, "Time Limit: " + time, 100f, textStartY + (count++) * textGapHeight);
        }
//        font.draw(batch, "FPS: " + Float.toString(1/delta), 100f, textStartY + (count++) * textGapHeight);
	}
	
	/**
	 * Get the y-coordinate of next line of text.
	 * @param count: Counter variable of which line
	 * @return y-coordinate in pixels
	 */
	private float incrementTextY(int count) {
		return textStartY + (count * textGapHeight);
	}
	
	/**
	 * Resizing method. When the game window is expanded/shrunk.
	 * @param width: New width of screen.
	 * @param height: New height of screen.
	 */
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	/**
	 * Dispose game elements Java garbage collector is unable to dispose of.
	 */
	public void dispose() {
		font.dispose();
		batch.dispose();
	}
	
}
