package com.columbia.apps;

import java.util.List;

import com.columbia.framework.App;
import com.columbia.framework.Graphics;
import com.columbia.framework.Input.TouchEvent;
import com.columbia.framework.Screen;

public class HighscoreScreen extends Screen {

	String lines[] = new String[5];
	
	public HighscoreScreen(App app) {
		super(app);
		
		for (int i = 0; i < 5; i++) {
			lines[i] = "" + (i + 1) + ". " + Settings.highscores[i];
		}
	}
	
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = app.getInput().getTouchEvents();
		app.getInput().getKeyEvents();
		
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x < 64 && event.y > 416) {
					app.setScreen(new MainMenuScreen(app));
					return;
				}
			}
		}
	}
	
	@Override
	public void present(float deltaTime) {
		Graphics g = app.getGraphics();
		
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.mainmenu, 64, 20, 0, 42, 196, 42);
		
		int y = 100;
		for (int i = 0; i < 5; i++) {
			drawText(g, lines[i], 20, y);
			y += 50;
		}
		g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
	}
	
	public void drawText(Graphics g, String line, int x, int y) {
		int len = line.length();
		for (int i = 0; i < len; i++) {
			char character = line.charAt(i);
			
			if (character == ' ') {
				x += 20;
				continue;
			}
			
			int srcX = 0;
			int srcWidth = 0;
			if (character == '.') {
				srcX = 200;
				srcWidth = 10;
			}
			else {
				srcX = (character - '0') * 20;
				srcWidth = 20;
			}
			
			g.drawPixmap(Assets.numbers, x, y, srcX, 0, srcWidth, 32);
			x += srcWidth;
		}
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
