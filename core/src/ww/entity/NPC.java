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

public class NPC extends Entity {
    public Animation<TextureRegion> walking;
    public float stateTime;
    public boolean right;

    public float speed;
    public boolean traitor;

    public int dead;

    public float time;
    public float delay;

    public float moveTime;

    public NPC(float x, float y, boolean traitor) {
        super(x, y);
        hb = new Rectangle(pos.x, pos.y, 16, 16);

        this.traitor = traitor;

        int type = MathUtils.random(1);
        walking = new Animation<TextureRegion>(0, Game.gfx[0][type * 2], Game.gfx[0][1 + type * 2]);
        right = MathUtils.randomBoolean();

        speed = MathUtils.random(32, 96);
        walking.setFrameDuration(.15f / (speed / 64));

        delay = MathUtils.random(3, 5);
    }

    @Override
    public void update(Play play) {
        if (dead == 0) {
            vel.x = speed * (right ? 1 : -1);

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

        if (dead == 1) {
            vel.y = 200;
            dead = 2;
            if (traitor) {
                play.won = true;
            }
        }

        pos.add(vel.x * Gdx.graphics.getDeltaTime(), vel.y * Gdx.graphics.getDeltaTime());

        if (pos.x + 8 < -160 || pos.x + 8 > 480) {
            right = !right;
        }

        stateTime = vel.y > 0 || vel.y < 0 ? walking.getFrameDuration() : stateTime + Gdx.graphics.getDeltaTime();
        if (vel.x == 0) {
            stateTime = 0;
        }

        if (dead > 0) {
            vel.y -= 600 * Gdx.graphics.getDeltaTime();

            hb.setPosition(pos.x, pos.y + vel.y * Gdx.graphics.getDeltaTime() + 5);
            for (int i = 0; i < play.walls.size; i++) {
                if (hb.overlaps(play.walls.get(i))) {
                    if (vel.y > 0) {
                        pos.y = play.walls.get(i).y - hb.height;
                    } else if (vel.y < 0) {
                        pos.y = play.walls.get(i).y + play.walls.get(i).height - 5;
                    }
                    vel.y = 0;
                    vel.x = 0;
                }
            }
        }

        if (traitor) {
            time += Gdx.graphics.getDeltaTime();
            if (time > delay) {
                delay = MathUtils.random(3, 5);
                time = 0;
                play.entities.add(new Shot(pos.x + 8 + 12 * (right ? 1 : -1), pos.y + 7, right, false));
                play.shake = 2;
                play.flash = 1;
                speed = MathUtils.random(32, 96);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (dead > 0) {
            batch.setColor(.5f, .5f, .5f, 1);
        }
        batch.draw(walking.getKeyFrame(stateTime, true), pos.x, pos.y, 8, 8, 16, 16, right ? 1 : -1, 1, dead > 0 ? 90 : 0);
        batch.setColor(1, 1, 1, 1);
    }
}
