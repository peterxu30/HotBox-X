package oldCode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Play implements Screen {
	
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private float gravityY = -9.81f;
	private float playerDensity = 2.5f;
	private float playerFriction = 0.2f;
	private float playerRestitution = 0;
	
	private final float TIMESTEP = 1/60f;
	private final int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		world = new World(new Vector2(0, gravityY), true);
		debugRenderer = new Box2DDebugRenderer();
		
		camera = new OrthographicCamera();
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, 5);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(0.5f);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = playerDensity; 
		fixtureDef.friction = playerFriction;
		fixtureDef.restitution = playerRestitution;
		
		world.createBody(bodyDef).createFixture(fixtureDef);
		
		shape.dispose();
		
//		BodyDef bodyDef1 = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, 0);
		
		ChainShape groundShape = new ChainShape();
		groundShape.createChain(new Vector2[] {new Vector2(-50, 0), new Vector2(50, 0)});
		
//		FixtureDef fixtureDef1 = new FixtureDef();
		fixtureDef.shape = groundShape;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0;
		
		world.createBody(bodyDef).createFixture(fixtureDef);
		
		groundShape.dispose();
		
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(2.25f, 10);
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(0.5f, 1);
		
		fixtureDef.shape = boxShape;
		fixtureDef.friction = 0.75f;
		fixtureDef.restitution = 0.1f;
		fixtureDef.density = 5;
		
		Body box = world.createBody(bodyDef);
		box.createFixture(fixtureDef);
		
		boxShape.dispose();
		
		box.applyAngularImpulse(30, true);
		
				
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
//		Gdx.gl.glClearColor(198/255f, 198/255f, 198/255f, 1);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		debugRenderer.render(world, camera.combined);
		
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		camera.viewportWidth = width / 25;
		camera.viewportHeight = height / 25;
		camera.update();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		dispose();

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		debugRenderer.dispose();
		world.dispose();
		

	}

}
