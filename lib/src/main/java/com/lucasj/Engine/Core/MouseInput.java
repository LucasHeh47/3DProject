package com.lucasj.Engine.Core;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.lucasj.Engine.Launcher;

public class MouseInput {
	
	private final Vector2d previousPos, currentPos;
	private final Vector2f displayVec;
	
	private boolean inWindow = false, leftButtonPress = false, rightButtonPress = false;
	
	public MouseInput() {
		previousPos = new Vector2d(-1, -1);
		currentPos = new Vector2d(0, 0);
		displayVec = new Vector2f();
	}
	
	public void init() {
		GLFW.glfwSetCursorPosCallback(Launcher.getWindow().getWindow(), (window, xpos, ypos) -> {
			currentPos.x = xpos;
			currentPos.y = ypos;
		});
		GLFW.glfwSetCursorEnterCallback(Launcher.getWindow().getWindow(), (window, entered) -> {
			inWindow = entered;
		});
		
		GLFW.glfwSetMouseButtonCallback(Launcher.getWindow().getWindow(), (window, button, action, mode) -> {
			leftButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
			rightButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
		});
	}
	
	public void input() {
		displayVec.x = 0;
		displayVec.y = 0;
		if(previousPos.x > 0 && previousPos.y > 0 && inWindow) {
			double x = currentPos.x - previousPos.x;
			double y = currentPos.y - previousPos.y;
			boolean rotatex = x != 0;
			boolean rotatey = y != 0;
			if(rotatex)
				displayVec.y = (float) x;
			if(rotatey)
				displayVec.x = (float) y;
		}
		previousPos.x = currentPos.x;
		previousPos.y = currentPos.y;
	}

	public Vector2f getDisplayVec() {
		return displayVec;
	}

	public boolean isLeftButtonPress() {
		return leftButtonPress;
	}

	public boolean isRightButtonPress() {
		return rightButtonPress;
	}

}
