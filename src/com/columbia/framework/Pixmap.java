package com.columbia.framework;

import com.columbia.framework.Graphics.PixmapFormat;

public interface Pixmap {
	
	public int getWidth();
	
	public int getHeight();
	
	public PixmapFormat getFormat();
	
	public void dispose();

}
