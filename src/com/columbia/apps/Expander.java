package com.columbia.apps;

import com.columbia.framework.Screen;
import com.columbia.framework.impl.ExpanderApp;

public class Expander extends ExpanderApp {

	
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}

}
