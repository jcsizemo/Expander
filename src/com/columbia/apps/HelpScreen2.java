package com.columbia.apps;

import java.util.List;

import com.columbia.framework.App;
import com.columbia.framework.Graphics;
import com.columbia.framework.Input.TouchEvent;
import com.columbia.framework.Screen;

public class HelpScreen2 extends Screen {
	
	public HelpScreen2(App app) {
		super(app);
	}

	@Override
	public void update(float deltaTime) {
		
		List<TouchEvent> touchEvents = app.getInput().getTouchEvents();
		app.getInput().getKeyEvents();
		
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x > 256 && event.y > 416) {
					app.setScreen(new HelpScreen3(app));
					return;
				}
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = app.getGraphics();
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.help2, 64, 100);
		g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);
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
