package com.lucasj.Engine.Core.Entity;

public class Model {

	private int id;
	private int vertexCount;
	private Texture texture;
	
	public Model(int id, int vertexCount) {
		this.setId(id);
		this.setVertexCount(vertexCount);
	}
	
	public Model(int id, int vertexCount, Texture texture) {
		this.setId(id);
		this.setVertexCount(vertexCount);
		this.texture = texture;
	}
	
	public Model(Model model, Texture texture) {
		this.setId(model.getId());
		this.setVertexCount(model.getVertexCount());
		this.texture = texture;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
}
