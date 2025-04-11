package _3DProject.TestGame;

import java.util.ArrayList;
import java.util.List;

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

	private static final float MOVEMENT_SPEED = 100f;
	
	private final RenderManager renderer;
	private final ObjectLoader loader;
	private final WindowManager window;
	
	private Player player;
	
	private List<Entity> blocks = new ArrayList<>();
	
	Vector3f cameraInc;
	
	public TestGame() {
		renderer = new RenderManager();
		window = Launcher.getWindow();
		loader = new ObjectLoader();
		Player player = new Player(this);
		cameraInc = new Vector3f(0, 0, 0);
	}
	
	@Override
	public void init() throws Exception {
		renderer.init();
		
		
		Model model = loader.loadOBJModel("/models/block.obj");
		model.setTexture(new Texture(loader.loadTexture("textures/stone.png")));
		
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 16; j++) {
				Entity entity = new Entity(model, new Vector3f(i*4 - 32, 0, 5-j*4+32), new Vector3f(0, 0, 0), 2);
				blocks.add(entity);
			}
		}
	}

	@Override
	public void input() {
		cameraInc.set(0, 0, 0);
		if(window.isKeyPressed(GLFW.GLFW_KEY_W))
			cameraInc.z = -1;
		if(window.isKeyPressed(GLFW.GLFW_KEY_S))
			cameraInc.z = 1;
		if(window.isKeyPressed(GLFW.GLFW_KEY_A))
			cameraInc.x = -1;
		if(window.isKeyPressed(GLFW.GLFW_KEY_D))
			cameraInc.x = 1;
		if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
			cameraInc.y = -1;
		if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE))
			cameraInc.y = 1;
	}

	@Override
	public void update(float interval, MouseInput mouseInput) {
		if(player == null) {
			player = new Player(this);
		}
		player.update(interval);
	    // Handle camera rotation (this part is fine)
	    if (mouseInput.isRightButtonPress()) {
	        Vector2f rotVec = mouseInput.getDisplayVec();
	        player.getCamera().moveRotation(
	            rotVec.x * Consts.MOUSE_SENSITIVITY,
	            rotVec.y * Consts.MOUSE_SENSITIVITY,
	            0
	        );
	    }
	    
	    // Convert the camera's Y-axis rotation (yaw) from degrees to radians
	    float yaw = (float) Math.toRadians(player.getCamera().getRotation().y);
	    
	    // Calculate the intended movement increments for the horizontal (x, z) plane
	    float dx = cameraInc.x * MOVEMENT_SPEED;
	    float dz = cameraInc.z * MOVEMENT_SPEED;
	    
	    // Apply the rotation transformation to adjust movement relative to the camera's facing direction
	    float adjustedX = dx * (float) Math.cos(yaw) - dz * (float) Math.sin(yaw);
	    float adjustedZ = dx * (float) Math.sin(yaw) + dz * (float) Math.cos(yaw);
	    
	    // Vertical movement (y-axis) remains unchanged
	    float dy = cameraInc.y * MOVEMENT_SPEED;
	    
	    // Update camera position with the rotated movement vector
	    Vector3f movementUpdate = new Vector3f(adjustedX, dy, adjustedZ);
	    movementUpdate.mul(interval);
	    player.getCamera().movePosition(movementUpdate.x, movementUpdate.y, movementUpdate.z);
	}

	@Override
	public void render() {
	    if(window.isResize()) {
	        GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
	        window.setResize(true); // Reset the resize flag after handling it
	    }
	    
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

	    window.setClearColor(0.83f, 0.94f, 1.0f, 0.0f);
	    for(Entity entity : blocks) {
	    	renderer.render(entity, player.getCamera());
	    }
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		loader.cleanup();
	}

	public List<Entity> getBlocks() {
		return blocks;
	}

	
	
}
