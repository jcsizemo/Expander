package com.columbia.apps;

import java.util.List;

import com.columbia.framework.App;
import com.columbia.framework.Graphics;
import com.columbia.framework.Input.TouchEvent;
import com.columbia.framework.Screen;

public class MainMenuScreen extends Screen {
	
	public MainMenuScreen(App app) {
		super(app);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = app.getGraphics();
		List<TouchEvent> touchEvents = app.getInput().getTouchEvents();
		app.getInput().getKeyEvents();
		
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 0, g.getHeight() - 64, 64, 64)) {
				}
				if (inBounds(event, 64, 220, 192, 42)) {
					app.setScreen(new AppScreen(app));
					return;
				}
				if(inBounds(event, 64, 220 + 42, 192, 42)) {
					app.setScreen(new HighscoreScreen(app));
					return;
				}
				if (inBounds(event, 64, 220 + 84, 192, 42)) {
					app.setScreen(new HelpScreen(app));
					return;
				}
			}
		}
	}
	
	private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
		if (event.x > x && event.x < x + width -1 && event.y > y && event.y < y + height - 1) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = app.getGraphics();
		
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.logo, 32, 20);
		g.drawPixmap(Assets.mainmenu, 64, 220);
		if (Settings.soundEnabled) {
			g.drawPixmap(Assets.buttons, 0, 416, 0, 0, 64, 64);
		}
		else {
			g.drawPixmap(Assets.buttons, 0, 416, 64, 0, 64, 64);
		}

	}

	@Override
	public void pause() {
		Settings.save(app.getFileIO());
	}

	@Override
	public void resume() {
		

	}

	@Override
	public void dispose() {
		

	}

}
