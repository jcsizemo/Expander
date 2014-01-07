package com.columbia.framework.impl;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.columbia.expander.R;
import com.columbia.framework.App;
import com.columbia.framework.FileIO;
import com.columbia.framework.Graphics;
import com.columbia.framework.Input;
import com.columbia.framework.Screen;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public abstract class ExpanderApp extends Activity implements App {
	
	AndroidFastRenderView renderView;
	Graphics graphics;
	Input input;
	FileIO fileIO;
	Screen screen;
	WakeLock wakeLock;

    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		int frameBufferWidth = isLandscape ? 480 : 320;		// target width
		int frameBufferHeight = isLandscape ? 320 : 480;	// target height
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		int sdkVersion = Build.VERSION.SDK_INT;
		float scaleX = 0;
		float scaleY = 0;
		if (sdkVersion < 13) {
			scaleX = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
			scaleY = (float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();
		}
		else {
			getWindowManager().getDefaultDisplay().getSize(size);
			scaleX = (float) frameBufferWidth / size.x;
			scaleY = (float) frameBufferHeight / size.y;
		}
		
		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(getAssets());
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		screen = getStartScreen();
		setContentView(renderView);
		
//        Intent intent = new Intent(this,LocationService.class);
//        setContentView(R.layout.activity_main);
    }
    
    public void onResume() {
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}
	
	
	public void onPause() {
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();
		
		if (isFinishing()) {
			screen.dispose();
		}
	}
	
	
	public Input getInput() {
		return input;
	}
	
	
	public FileIO getFileIO() {
		return fileIO;
	}
	
	
	public Graphics getGraphics() {
		return graphics;
	}
	
	public void setScreen(Screen screen) {
		if (screen == null) {
			throw new IllegalArgumentException("Screen must not be null!");
		}
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}
	
	public Screen getCurrentScreen() {
		return screen;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
