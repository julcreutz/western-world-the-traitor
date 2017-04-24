package ww.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import ww.entity.Cactus;
import ww.entity.Entity;
import ww.entity.Grass;
import ww.entity.Saloon;
import ww.game.Game;

public class Title extends State {
    public SpriteBatch batch;
    public OrthographicCamera cam;
    public ExtendViewport vp;

    public float time;
    public float fadeIn;

    public Saloon saloon;
    public Array<Entity> entities;

    public float sinTime;

    public Title() {
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 320, 240);
        vp = new ExtendViewport(320, 240, cam);
        vp.apply();

        Game.intro.stop();

        fadeIn = 1;

        entities = new Array<Entity>();
        saloon = new Saloon(MathUtils.random(-160, 384), 32);
        entities.add(saloon);
        for (int x = -10; x < 30; x += 2) {
            if (MathUtils.randomBoolean(.7f)) {
                if (MathUtils.randomBoolean(.5f)) {
                    entities.add(new Cactus(x * 16, 32));
                } else {
                    entities.add(new Grass(x * 16, 32));
                }
            }
        }
    }

    @Override
    public void update() {
        cam.position.x = 160 + MathUtils.sinDeg(sinTime) * 160;
        cam.update();
        time += 2 * Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) {
            Game.state = 0;
            Game.play.reset();
        }

        if (fadeIn > 0) {
            fadeIn -= Gdx.graphics.getDeltaTime();
        }
        fadeIn = MathUtils.clamp(fadeIn, 0, 1);

        for (int i = 0; i < entities.size; i++) {
            entities.get(i).update(Game.play);
        }

        sinTime += 5 * Gdx.graphics.getDeltaTime();

        saloon.ox = cam.position.x;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        // Sky
        for (int x = -10; x < 30; x++) {
            for (int y = 2; y < 15; y++) {
                batch.draw(Game.gfx[1][4], x * 16, y * 16);
            }
        }

        // Sand
        for (int x = -10; x < 30; x++) {
            batch.draw(Game.gfx[1][0], x * 16, 16);
        }

        for (int i = 0; i < entities.size; i++) {
            entities.get(i).render(batch);
        }

        batch.draw(Game._title, cam.position.x - 160, 0);
        if ((int) time % 2 == 0) {
            batch.draw(Game.press_space, cam.position.x - 160, 0);
        }

        if (fadeIn > 0) {
            batch.setColor(1, 1, 1, MathUtils.round(fadeIn * 5f) / 5f);
            for (int x = -10; x < 30; x++) {
                for (int y = 0; y < 15; y++) {
                    batch.draw(Game.gfx[3][12], x * 16 + cam.position.x - 160 - (vp.getWorldWidth() - 320) / 2, y * 16 + cam.position.y - 120);
                }
            }
        }
        batch.setColor(1, 1, 1, 1);
        batch.end();
    }

    @Override
    public void resize(int w, int h) {
        vp.update(w, h);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
