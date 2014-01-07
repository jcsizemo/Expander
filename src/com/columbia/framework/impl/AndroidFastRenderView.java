package com.columbia.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
	
	ExpanderApp app;
	Bitmap frameBuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean running = false;

	public AndroidFastRenderView(ExpanderApp game, Bitmap frameBuffer) {
		super(game);
		this.app = game;
		this.frameBuffer = frameBuffer;
		this.holder = getHolder();
	}
	
	public void resume() {
		running = true;
		renderThread = new Thread(this);
		renderThread.start();
	}
	
	public void run() {
		Rect dstRect = new Rect();
		long startTime = System.nanoTime();
		while (running) {
			if (!holder.getSurface().isValid()) {
				continue;
			}
			
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();
			
			app.getCurrentScreen().update(deltaTime);
			app.getCurrentScreen().present(deltaTime);
			
			Canvas canvas = holder.lockCanvas();
			canvas.getClipBounds(dstRect);
			canvas.drawBitmap(frameBuffer, null, dstRect, null);
			holder.unlockCanvasAndPost(canvas);
		}
	}
	
	public void pause() {
		running = false;
		while(true) {
			try {
				renderThread.join();
				break;
			}
			catch (InterruptedException e) {
				
			}
		}
	}
}
