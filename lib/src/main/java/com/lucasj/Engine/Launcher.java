package com.lucasj.Engine;

import org.lwjgl.Version;

import com.lucasj.Engine.Core.EngineManager;
import com.lucasj.Engine.Core.WindowManager;
import com.lucasj.Engine.Core.Utils.Consts;

import _3DProject.TestGame.TestGame;

public class Launcher {

	private static WindowManager window;
	//private static EngineManager engine;
	private static TestGame game;
	
	public static void main(String[] args) {
		System.out.println(Version.getVersion());
		
		window = new WindowManager(Consts.TITLE, 1920, 1080, true);
		game = new TestGame();
		EngineManager engine = new EngineManager();
		
		try {
			engine.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static WindowManager getWindow() {
		return window;
	}
	
	public static TestGame getGame() {
		return game;
	}
	
}
