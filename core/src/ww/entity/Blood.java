package ww.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import ww.game.Game;
import ww.game.state.Play;

public class Blood extends Entity {
    public float scale;

    public Blood(float x, float y) {
        super(x, y);
        hb = new Rectangle(pos.x, pos.y, 1, 1);
        vel.set(MathUtils.random(-96, 96), MathUtils.random(100, 200));

        scale = MathUtils.random(1, 2);
    }

    @Override
    public void update(Play play) {
        hb.setPosition(pos.x + vel.x * Gdx.graphics.getDeltaTime(), pos.y);
        for (int i = 0; i < play.walls.size; i++) {
            if (hb.overlaps(play.walls.get(i))) {
                if (vel.x > 0) {
                    pos.x = play.walls.get(i).x - hb.width;
                } else if (vel.x < 0) {
                    pos.x = play.walls.get(i).x + play.walls.get(i).width;
                }
                vel.x = 0;
            }
        }

        vel.y -= 600 * Gdx.graphics.getDeltaTime();

        hb.setPosition(pos.x, pos.y + vel.y * Gdx.graphics.getDeltaTime());
        for (int i = 0; i < play.walls.size; i++) {
            if (hb.overlaps(play.walls.get(i))) {
                if (vel.y > 0) {
                    pos.y = play.walls.get(i).y - hb.height;
                } else if (vel.y < 0) {
                    pos.y = play.walls.get(i).y + play.walls.get(i).height;
                }
                vel.y = 0;
                vel.x = 0;
            }
        }

        pos.add(vel.x * Gdx.graphics.getDeltaTime(), vel.y * Gdx.graphics.getDeltaTime());
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(Game.gfx[0][5], pos.x, pos.y, 8, 8, 16, 16, scale, scale, 0);
    }
}
