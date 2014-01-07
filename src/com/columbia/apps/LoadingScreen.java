package com.columbia.apps;

import com.columbia.framework.App;
import com.columbia.framework.Graphics;
import com.columbia.framework.Graphics.PixmapFormat;
import com.columbia.framework.Screen;

public class LoadingScreen extends Screen {
	
	public LoadingScreen(App app) {
		super(app);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = app.getGraphics();
		Assets.background = g.newPixmap("background.png", PixmapFormat.RGB565);
		Assets.logo = g.newPixmap("logo.png", PixmapFormat.ARGB4444);
		Assets.mainmenu = g.newPixmap("mainmenu.png", PixmapFormat.ARGB4444);
		Assets.buttons = g.newPixmap("buttons.png", PixmapFormat.ARGB4444);
		Assets.help1 = g.newPixmap("help1.png", PixmapFormat.ARGB4444);
		Assets.help2 = g.newPixmap("help2.png", PixmapFormat.ARGB4444);
		Assets.help3 = g.newPixmap("help3.png", PixmapFormat.ARGB4444);
		Assets.numbers = g.newPixmap("numbers2.png", PixmapFormat.ARGB4444);
		Assets.ready = g.newPixmap("ready.png", PixmapFormat.ARGB4444);
		Assets.pause = g.newPixmap("pause.png", PixmapFormat.ARGB4444);
		Assets.gameOver = g.newPixmap("appover.png", PixmapFormat.ARGB4444);
		Assets.headUp = g.newPixmap("headup.png", PixmapFormat.ARGB4444);
		Assets.headLeft = g.newPixmap("headleft.png", PixmapFormat.ARGB4444);
		Assets.headDown = g.newPixmap("headdown.png", PixmapFormat.ARGB4444);
		Assets.headRight = g.newPixmap("headright.png", PixmapFormat.ARGB4444);
		Assets.tail = g.newPixmap("tail.png", PixmapFormat.ARGB4444);
		Assets.stain1 = g.newPixmap("stain1.png", PixmapFormat.ARGB4444);
		Assets.stain2 = g.newPixmap("stain2.png", PixmapFormat.ARGB4444);
		Assets.stain3 = g.newPixmap("stain3.png", PixmapFormat.ARGB4444);
		Settings.load(app.getFileIO());
		app.setScreen(new MainMenuScreen(app));
	}

	@Override
	public void present(float deltaTime) {
		
		
	}

	@Override
	public void pause() {
		
		
	}

	@Override
	public void resume() {
		
		
	}

	@Override
	public void dispose() {
		
		
	}
	

}
