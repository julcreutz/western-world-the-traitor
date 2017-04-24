package ww.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import ww.game.Game;
import ww.game.state.Play;

public class Saloon extends Entity {
    public float ox;

    public Saloon(float x, float y) {
        super(x, y);
    }

    @Override
    public void update(Play play) {
        ox = play.cam.position.x;
    }

    @Override
    public void render(SpriteBatch batch) {
        for (int x = 0; x <= 5; x++) {
            for (int y = 5; y >= 2; y--) {
                batch.draw(Game.gfx[7 - y][x], pos.x + x * 16 + ox * .5f, pos.y + y * 16 - 32);
            }
        }
    }
}
