package com.lucasj.Engine.Core;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.lucasj.Engine.Launcher;
import com.lucasj.Engine.Core.Entity.Entity;
import com.lucasj.Engine.Core.Utils.Transformation;
import com.lucasj.Engine.Core.Utils.Utils;

public class RenderManager {

	private final WindowManager window;
	private ShaderManager shader;
	
	public RenderManager() {
		window = Launcher.getWindow();
	}
	
	public void init() throws Exception {
		shader = new ShaderManager();
		shader.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
		shader.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
		shader.link();
		shader.createUniform("textureSampler");
		shader.createUniform("transformationMatrix");
		shader.createUniform("projectionMatrix");
		shader.createUniform("viewMatrix");

	}
	
	public void render(Entity entity, Camera camera) {
	    //clear();
	    shader.bind();
	    shader.setUniform("textureSampler", 0);
	    shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix(entity));
	    shader.setUniform("projectionMatrix", window.updateProjectionMatrix());
	    shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));

	    GL30.glBindVertexArray(entity.getModel().getId());
	    GL20.glEnableVertexAttribArray(0);
	    GL20.glEnableVertexAttribArray(1);
	    GL13.glActiveTexture(GL13.GL_TEXTURE0);
	    
	    // Bind the texture
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getId());
	    
	    // Set texture filtering parameters BEFORE drawing
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	    
	    // Draw the elements
	    GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	    
	    // Clean up
	    GL20.glDisableVertexAttribArray(0);
	    GL20.glDisableVertexAttribArray(1);
	    GL30.glBindVertexArray(0);
	    shader.unbind();
	}
	
	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void cleanup() {
		shader.cleanup();
		shader.bind();
	}
}
