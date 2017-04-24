package ww.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ww.game.state.Play;

public abstract class Entity {
    public Vector2 pos;
    public Vector2 vel;
    public Rectangle hb;

    public Entity(float x, float y) {
        pos = new Vector2(x, y);
        vel = new Vector2(0, 0);
    }

    public abstract void update(Play play);
    public abstract void render(SpriteBatch batch);
}
