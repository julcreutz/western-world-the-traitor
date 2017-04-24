package ww.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ww.game.state.Play;
import ww.game.state.Title;

public class Game extends ApplicationAdapter {
	public static TextureRegion[][] gfx;
	public static Texture _title;
	public static Texture press_space;
	public static Sound gun;
	public static Sound hit;
	public static Music intro;

	public static Vector2 mouse;

	public static Play play;
	public static Title title;
	public static int state;

	@Override
	public void create () {
		gfx = TextureRegion.split(new Texture("gfx.png"), 16, 16);
		_title = new Texture("title.png");
		press_space = new Texture("press_space.png");
		gun = Gdx.audio.newSound(Gdx.files.internal("gun.wav"));
		hit = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
		intro = Gdx.audio.newMusic(Gdx.files.internal("intro.mp3"));

		mouse = new Vector2();

		play = new Play();
		title = new Title();
		state = 1;
	}

	@Override
	public void render () {
		switch (state) {
			case 0:
				play.update();
				play.render();
				break;
			case 1:
				title.update();
				title.render();
				break;
		}
	}

	@Override
	public void resize(int w, int h) {
		play.resize(w, h);
		title.resize(w, h);
	}
	
	@Override
	public void dispose () {
		gfx[0][0].getTexture().dispose();
		_title.dispose();
		press_space.dispose();
		gun.dispose();
		hit.dispose();
		intro.dispose();
		play.dispose();
		title.dispose();
	}
}
