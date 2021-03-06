package com.cedricmartens.imagebot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cedricmartens.imagebot.ImageBot;


public class DesktopLauncher
{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1200;
		config.height = 400;

		new LwjglApplication(new ImageBot(), config);
	}
}
