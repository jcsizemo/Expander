package com.columbia.apps;

import java.util.List;

import android.graphics.Color;

import com.columbia.framework.App;
import com.columbia.framework.Graphics;
import com.columbia.framework.Input.TouchEvent;
import com.columbia.framework.Pixmap;
import com.columbia.framework.Screen;

public class AppScreen extends Screen {
	
	enum appState{
		Ready,
		Running,
		Paused,
		gameOver
	}
	
	appState state = appState.Ready;
	World world;
	int oldScore = 0;
	String score = "0";
	
	public AppScreen(App app) {
		super(app);
		world = new World();
	}
	
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = app.getInput().getTouchEvents();
		app.getInput().getKeyEvents();
		
		if (state == appState.Ready) {
			updateReady(touchEvents);
		}
		if (state == appState.Running) {
			updateRunning(touchEvents, deltaTime);
		}
		if (state == appState.Paused) {
			updatePaused(touchEvents);
		}
		if (state == appState.gameOver) {
			updategameOver(touchEvents);
		}
	}
	
	private void updateReady(List<TouchEvent> touchEvents) {
		if (touchEvents.size() > 0) {
			state = appState.Running;
		}
	}
	
	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x < 64 && event.y < 64) {
					state = appState.Paused;
					return;
				}
			}
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (event.x < 64 && event.y > 416) {
					world.snake.turnLeft();
				}
				if (event.x > 256 && event.y > 416) {
					world.snake.turnRight();
				}
			}
		}
		
		world.update(deltaTime);
		if (world.gameOver) {
			state = appState.gameOver;
		}
		if (oldScore != world.score) {
			oldScore = world.score;
			score = "" + oldScore;
		}
	}
	
	private void updatePaused(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x > 80 && event.x <= 240) {
					if (event.y > 100 && event.y <= 148) {
						state = appState.Running;
						return;
					}
					if (event.y > 148 && event.y < 196) {
						app.setScreen(new MainMenuScreen(app));
						return;
					}
				}
			}
		}
	}
	
	private void updategameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x >= 128 && event.x <= 192 && event.y >= 200 && event.y <= 264) {
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
		drawWorld(world);
		if (state == appState.Ready) {
			drawReadyUI();
		}
		if (state == appState.Running) {
			drawRunningUI();
		}
		if (state == appState.Paused) {
			drawPausedUI();
		}
		if (state == appState.gameOver) {
			drawgameOverUI();
		}
		
		drawText(g, score, g.getWidth() / 2 - score.length()*20 / 2, g.getHeight() - 42);
	}
	
	private void drawWorld(World world) {
		Graphics g = app.getGraphics();
		Snake snake = world.snake;
		SnakePart head = snake.parts.get(0);
		Stain stain = world.stain;
		
		Pixmap stainPixmap = null;
		if (stain.type == Stain.TYPE_1) {
			stainPixmap = Assets.stain1;
		}
		if (stain.type == Stain.TYPE_2) {
			stainPixmap = Assets.stain2;
		}
		if (stain.type == Stain.TYPE_3) {
			stainPixmap = Assets.stain3;
		}
		int x = stain.x * 32;
		int y = stain.y * 32;
		g.drawPixmap(stainPixmap, x, y);
		
		int len = snake.parts.size();
		for (int i = 1; i < len; i++) {
			SnakePart part = snake.parts.get(i);
			x = part.x * 32;
			y = part.y * 32;
			g.drawPixmap(Assets.tail, x, y);
		}
		
		Pixmap headPixmap = null;
		if (snake.direction == Snake.UP) {
			headPixmap = Assets.headUp;
		}
		if (snake.direction == Snake.LEFT) {
			headPixmap = Assets.headLeft;
		}
		if (snake.direction == Snake.DOWN) {
			headPixmap = Assets.headDown;
		}
		if (snake.direction == Snake.RIGHT) {
			headPixmap = Assets.headRight;
		}
		x = head.x * 32 + 16;
		y = head.y * 32 + 16;
		g.drawPixmap(headPixmap, x - headPixmap.getWidth() / 2, y - headPixmap.getHeight() / 2);
	}
	
	private void drawReadyUI() {
		Graphics g = app.getGraphics();
		
		g.drawPixmap(Assets.ready, 47, 100);
		g.drawLine(0, 416, 480, 416, Color.BLACK);
	}
	
	private void drawRunningUI() {
		Graphics g = app.getGraphics();
		
		g.drawPixmap(Assets.buttons, 0, 0, 64, 128, 64, 64);
		g.drawLine(0, 416, 480, 416, Color.BLACK);
		g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
		g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);
	}
	
	private void drawPausedUI() {
		Graphics g = app.getGraphics();
		
		g.drawPixmap(Assets.pause, 80, 100);
		g.drawLine(0, 416, 480, 416, Color.BLACK);
	}
	
	private void drawgameOverUI() {
		Graphics g = app.getGraphics();
		
		g.drawPixmap(Assets.gameOver, 62, 100);
		g.drawPixmap(Assets.buttons, 128, 200, 0, 128, 64, 64);
		g.drawLine(0, 416, 480, 416, Color.BLACK);
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
		if (state == appState.Running) {
			state = appState.Paused;
		}
		
		if (world.gameOver) {
			Settings.addScore(world.score);
			Settings.save(app.getFileIO());
		}
	}
	
	@Override
	public void resume() {
		
	}
	
	@Override
	public void dispose() {
		
	}

}
