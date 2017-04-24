package ww.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import ww.game.Game;
import ww.game.state.Play;

public class Cactus extends Entity {
    public int height;

    public Cactus(float x, float y) {
        super(x, y);
        height = MathUtils.random(1, 4);
        hb = new Rectangle(pos.x, pos.y, 16, 16 * height);
    }

    @Override
    public void update(Play play) {}

    @Override
    public void render(SpriteBatch batch) {
        for (int y = 0; y < height; y++) {
            batch.draw(Game.gfx[1][1], pos.x, pos.y + 16 * y, 8, 8, 16, 16, 1, 1, 0);
        }
        batch.draw(Game.gfx[1][2], pos.x, pos.y + 16 * height, 8, 8, 16, 16, 1, 1, 0);
    }
}
