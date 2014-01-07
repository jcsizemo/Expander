package com.columbia.framework;

public abstract class Screen {
	protected final App app;
	
	public Screen(App app) {
		this.app = app;
	}
	
	public abstract void update(float deltaTime);
	
	public abstract void present(float deltaTime);
	
	public abstract void pause();
	
	public abstract void resume();
	
	public abstract void dispose();
}
