package com.columbia.framework.impl;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.columbia.framework.Input;

public class AndroidInput implements Input {

	KeyboardHandler keyHandler;
	TouchHandler touchHandler;

	public AndroidInput(Context context, View view, float scaleX, float scaleY) {
		keyHandler = new KeyboardHandler(view);
		touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
	}

	public boolean isKeyPressed(int keyCode) {
		return keyHandler.isKeyPressed(keyCode);
	}

	public boolean isTouchDown(int pointer) {
		return touchHandler.isTouchDown(pointer);
	}

	public int getTouchX(int pointer) {
		return touchHandler.getTouchX(pointer);
	}

	public int getTouchY(int pointer) {
		return touchHandler.getTouchY(pointer);
	}

	public List<KeyEvent> getKeyEvents() {
		return keyHandler.getKeyEvents();
	}

	public List<TouchEvent> getTouchEvents() {
		return touchHandler.getTouchEvents();
	}

}
