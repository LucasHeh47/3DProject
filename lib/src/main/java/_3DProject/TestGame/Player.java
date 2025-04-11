package _3DProject.TestGame;

import java.util.List;

import org.joml.Vector3f;

import com.lucasj.Engine.Core.Camera;
import com.lucasj.Engine.Core.Entity.Entity;

public class Player {
	
	private Camera camera;
	private TestGame game;
	private float size = 1.8f;
	
	public Player(TestGame game) {
		this.game = game;
		camera = new Camera();
	}
	
	public void update(double deltaTime) {
		applyGravity(deltaTime);
		
	}
	
	private void applyGravity(double deltaTime) {
		move(new Vector3f(0.0f, (float) (-32.0f * deltaTime), 0.0f));
	}
	
	private void move(Vector3f changeInPos) {
		if(!checkCollisions()) {
			camera.movePosition(changeInPos.x, changeInPos.y, changeInPos.z);
		}
	}
	
	private boolean checkCollisions() {
	    List<Entity> blocks = game.getBlocks();
	    // Assume the player's collision sphere radius is half of the size
	    float sphereRadius = size / 2.0f;
	    // Get the player's position from the camera (assuming Camera has a getPosition() method)
	    Vector3f playerPos = camera.getPosition();

	    // Loop through each block to see if the player's collision sphere intersects the block's AABB.
	    for (Entity block : blocks) {
	        // Get the block's center position. Adjust if your Entity class uses a different method.
	        Vector3f blockPos = block.getPos();

	        // Define the block's half-size. Here we assume blocks are 4 units in size (half-size = 2)
	        float blockHalfSize = block.getScale(); // adjust as needed based on your model and scale
	        
	        // Calculate the AABB boundaries for the block
	        Vector3f aabbMin = new Vector3f(blockPos).sub(blockHalfSize, blockHalfSize, blockHalfSize);
	        Vector3f aabbMax = new Vector3f(blockPos).add(blockHalfSize, blockHalfSize, blockHalfSize);

	        // Compute the squared distance from the player's center to the AABB
	        float sqDist = 0.0f;

	        // X-axis
	        if (playerPos.x < aabbMin.x) {
	            float diff = aabbMin.x - playerPos.x;
	            sqDist += diff * diff;
	        } else if (playerPos.x > aabbMax.x) {
	            float diff = playerPos.x - aabbMax.x;
	            sqDist += diff * diff;
	        }

	        // Y-axis
	        if (playerPos.y < aabbMin.y) {
	            float diff = aabbMin.y - playerPos.y;
	            sqDist += diff * diff;
	        } else if (playerPos.y > aabbMax.y) {
	            float diff = playerPos.y - aabbMax.y;
	            sqDist += diff * diff;
	        }

	        // Z-axis
	        if (playerPos.z < aabbMin.z) {
	            float diff = aabbMin.z - playerPos.z;
	            sqDist += diff * diff;
	        } else if (playerPos.z > aabbMax.z) {
	            float diff = playerPos.z - aabbMax.z;
	            sqDist += diff * diff;
	        }

	        // If the distance from the player to this AABB is less than the sphere's radius, a collision occurred.
	        if (sqDist < sphereRadius * sphereRadius) {
	            return true;
	        }
	    }
	    
	    return false;
	}


	public Camera getCamera() {
		return camera;
	}
	
	
}
