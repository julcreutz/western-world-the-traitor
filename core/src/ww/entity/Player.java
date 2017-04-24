package ww.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import ww.game.Game;
import ww.game.state.Play;

public class Player extends Entity {
    public boolean right;

    public Animation<TextureRegion> walking;
    public float stateTime;

    public float pointerTime;

    public float moveTime;

    public boolean pressing;

    public Player(float x, float y) {
        super(x, y);
        hb = new Rectangle(pos.x, pos.y, 16, 16);

        right = true;

        walking = new Animation<TextureRegion>(.15f, Game.gfx[0][0], Game.gfx[0][1]);
        stateTime = 0;
    }

    @Override
    public void update(Play play) {
        final float acc = .5f;

        vel.x = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched() && Game.mouse.x < 80)) {
            vel.x = -64;
            if (vel.x < 0 && vel.y == 0) {
                right = false;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isTouched() && Game.mouse.x >= 80 && Game.mouse.x < 160)) {
            vel.x = 64;
            if (vel.x > 0 && vel.y == 0) {
                right = true;
            }
        }
        vel.x = MathUtils.clamp(vel.x, -64, 64);

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
            }
        }

        if (pos.x + 8 + vel.x * Gdx.graphics.getDeltaTime() < -160 || pos.x + 8 + vel.x * Gdx.graphics.getDeltaTime() > 480) {
            vel.x = 0;
        }

        pos.add(vel.x * Gdx.graphics.getDeltaTime(), vel.y * Gdx.graphics.getDeltaTime());

        stateTime = vel.x != 0 ? stateTime + Gdx.graphics.getDeltaTime() : 0;
        stateTime = vel.y > 0 || vel.y < 0 ? .2f : stateTime;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || (Gdx.input.isTouched() && Game.mouse.x >= 160 && Game.mouse.x < 320)) {
            if (!pressing) {
                play.entities.add(new Shot(pos.x + 8 + 12 * (right ? 1 : -1), pos.y + 7, right, true));
                play.shake = 2;
                play.flash = 1;
                pressing = true;
            }
        } else {
            pressing = false;
        }

        pointerTime += Gdx.graphics.getDeltaTime() * 360;

        if (vel.x != 0 && vel.y == 0) {
            moveTime += Gdx.graphics.getDeltaTime();
            if (moveTime > .1f) {
                play.entities.add(new Dust(pos.x + 8, pos.y));
                moveTime = 0;
            }
        } else {
            moveTime = 0;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(walking.getKeyFrame(stateTime, true), pos.x, pos.y, 8, 8, 16, 16, right ? 1 : -1, 1, 0);
        batch.draw(Game.gfx[0][6], pos.x, pos.y + 11 + MathUtils.sinDeg(pointerTime) * 1f, 7.5f, 8, 16, 16, MathUtils.sinDeg(pointerTime), 1, 0);
    }
}
