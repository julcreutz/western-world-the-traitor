package ww.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import ww.game.Game;
import ww.game.state.Play;

public class Grass extends Entity {
    public float time;
    public float speed;
    public float angle;

    public Grass(float x, float y) {
        super(x, y);
        hb = new Rectangle(pos.x, pos.y, 16, 16);
        speed = MathUtils.random(.25f, 1f);
        angle = MathUtils.random(10f, 20f);
    }

    @Override
    public void update(Play play) {
        time += 360 * speed * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(Game.gfx[1][3], pos.x, pos.y, 8, 0, 16, 16, 1, 1, MathUtils.sinDeg(time) * angle);
    }
}
