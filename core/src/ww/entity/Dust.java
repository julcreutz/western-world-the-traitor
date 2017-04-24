package ww.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import ww.game.Game;
import ww.game.state.Play;

public class Dust extends Entity {
    public float time;
    public float scale;
    public float rot;

    public Dust(float x, float y) {
        super(x, y);
        scale = MathUtils.random(1, 2);
        rot = MathUtils.random(360);
    }

    @Override
    public void update(Play play) {
        time += Gdx.graphics.getDeltaTime();
        if (time > .5f) {
            play.entities.removeValue(this, false);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(1, 1, 1, (.5f - time) / .5f);
        batch.draw(Game.gfx[1][6], pos.x, pos.y, .5f, .5f, 16, 16, scale * (1 + time / .5f), scale * (1 + time / .5f), rot);
        batch.setColor(1, 1, 1, 1);
    }
}
