package com.columbia.framework;

public interface App {
	
	public Input getInput();
	
	public FileIO getFileIO();
	
	public Graphics getGraphics();
	
	public void setScreen(Screen screen);
	
	public Screen getCurrentScreen();
	
	public Screen getStartScreen();

}
