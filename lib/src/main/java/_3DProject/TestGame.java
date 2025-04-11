package _3DProject;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.lucasj.Engine.Launcher;
import com.lucasj.Engine.Core.Camera;
import com.lucasj.Engine.Core.ILogic;
import com.lucasj.Engine.Core.MouseInput;
import com.lucasj.Engine.Core.ObjectLoader;
import com.lucasj.Engine.Core.RenderManager;
import com.lucasj.Engine.Core.WindowManager;
import com.lucasj.Engine.Core.Entity.Entity;
import com.lucasj.Engine.Core.Entity.Model;
import com.lucasj.Engine.Core.Entity.Texture;
import com.lucasj.Engine.Core.Utils.Consts;

public class TestGame implements ILogic {

	private static final float CAMERA_MOVE_SPEED = 0.05f;
	
	private final RenderManager renderer;
	private final ObjectLoader loader;
	private final WindowManager window;
	
	private Camera camera;
	private Entity entity;
	
	Vector3f cameraInc;
	
	public TestGame() {
		renderer = new RenderManager();
		window = Launcher.getWindow();
		loader = new ObjectLoader();
		camera = new Camera();
		cameraInc = new Vector3f(0, 0, 0);
	}
	
	@Override
	public void init() throws Exception {
		renderer.init();
		
		
		Model model = loader.loadOBJModel("/models/block.obj");
		model.setTexture(new Texture(loader.loadTexture("textures/dirt.png")));
		entity = new Entity(model, new Vector3f(0, 0, -5), new Vector3f(0, 0, 0), 2);
	}

	@Override
	public void input() {
		cameraInc.set(0, 0, 0);
		if(window.isKeyPressed(GLFW.GLFW_KEY_W))
			cameraInc.z = -1;
		if(window.isKeyPressed(GLFW.GLFW_KEY_S))
			cameraInc.z = 1;
		if(window.isKeyPressed(GLFW.GLFW_KEY_A))
			cameraInc.x = 1;
		if(window.isKeyPressed(GLFW.GLFW_KEY_D))
			cameraInc.x = -1;
		if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
			cameraInc.y = -1;
		if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE))
			cameraInc.y = 1;
	}

	@Override
	public void update(float interval, MouseInput mouseInput) {
		// Handle camera rotation
	    if (mouseInput.isRightButtonPress()) {
	        Vector2f rotVec = mouseInput.getDisplayVec();
	        camera.moveRotation(
	            rotVec.x * Consts.MOUSE_SENSITIVITY,
	            rotVec.y * Consts.MOUSE_SENSITIVITY,
	            0
	        );
	    }

	    // Create input vector based on your axis configuration:
	    // x: strafe (left/right), y: forward/backward, z: vertical (up/down)
	    Vector3f inputMovement = new Vector3f(
	        cameraInc.x,  // X: Strafe left/right
	        cameraInc.y,  // Y: Forward/backward
	        cameraInc.z   // Z: Vertical up/down
	    );

	    // Get camera rotation angles
	    Vector3f rotation = camera.getRotation();
	    float yawRadians = (float) Math.toRadians(rotation.y);
	    float pitchRadians = (float) Math.toRadians(rotation.x);

	    // Create rotation matrices
	    Matrix4f yawRotation = new Matrix4f().rotateY(yawRadians);
	    Matrix4f pitchRotation = new Matrix4f().rotateX(pitchRadians);

	    // Calculate directional vectors
	    Vector3f rightDir = yawRotation.transformDirection(new Vector3f(1, 0, 0));
	    Vector3f forwardDir = yawRotation.transformDirection(new Vector3f(0, 0, -1));
	    Vector3f upDir = new Vector3f(0, 1, 0);

	    // Apply pitch rotation to forward vector
	    forwardDir = pitchRotation.transformDirection(forwardDir);

	    // Combine movement components
	    Vector3f finalMovement = new Vector3f();
	    finalMovement.add(rightDir.mul(inputMovement.x));     // Strafe
	    finalMovement.add(forwardDir.mul(inputMovement.z));    // Forward/backward
	    finalMovement.add(upDir.mul(inputMovement.y));         // Vertical

	    // Apply movement speed
	    finalMovement.mul(CAMERA_MOVE_SPEED);

	    // Update camera position
	    camera.movePosition(finalMovement.x, finalMovement.y, -finalMovement.z);
	}

	@Override
	public void render() {
		if(window.isResize()) {
			GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResize(true);
		}
		
		window.setClearColor(0.83f, 0.94f, 1.0f, 0.0f);
		renderer.render(entity, camera);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		loader.cleanup();
	}

	
	
}
