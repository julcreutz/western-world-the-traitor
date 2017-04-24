package ww.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ww.game.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Western World: The Traitor";
		config.width = 320 * 3;
		config.height = 240 * 3;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		config.vSyncEnabled = false;
		new LwjglApplication(new Game(), config);
	}
}
