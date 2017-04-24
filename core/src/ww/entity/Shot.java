package ww.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import ww.game.Game;
import ww.game.state.Play;

public class Shot extends Entity {
    public float startX;
    public boolean right;
    public float hit;

    public boolean fromPlayer;

    public Shot(float x, float y, boolean right, boolean fromPlayer) {
        super(x, y);
        hb = new Rectangle(pos.x, pos.y, 1, 1);
        startX = x;

        this.right = right;
        this.fromPlayer = fromPlayer;

        Game.gun.play();
    }

    @Override
    public void update(Play play) {
        hb.setPosition(pos.x, pos.y);

        if (hit == 0) {
            for (int i = 0; i < play.entities.size; i++) {
                if (play.entities.get(i).getClass() == NPC.class && hb.overlaps(play.entities.get(i).hb)) {
                    NPC npc = (NPC) play.entities.get(i);
                    if (npc.dead == 0) {
                        hit += Gdx.graphics.getDeltaTime();
                        npc.dead = 1;
                        npc.vel.x = MathUtils.random(128, 256) * (right ? 1 : -1);
                        npc.pos.y++;
                        play.shake = 2;
                        if (!npc.traitor && fromPlayer) {
                            play.kills++;
                        }
                        for (int j = 0; j < 40; j++) {
                            play.entities.add(new Blood(npc.pos.x + 8, npc.pos.y + 8));
                        }
                        Game.hit.play();
                    }
                }
            }
        }

        vel.x = right ? 512 : -512;
        if (hit == 0) {
            pos.add(vel.x * Gdx.graphics.getDeltaTime(), vel.y * Gdx.graphics.getDeltaTime());
        }

        if (pos.x < -160 || pos.x > 480) {
            hit += Gdx.graphics.getDeltaTime();
        }

        if (hit > 0) {
            hit += Gdx.graphics.getDeltaTime();
        }
        if (hit > .1f) {
            play.entities.removeValue(this, false);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        for (float x = Math.min(startX, pos.x); x < Math.max(startX, pos.x); x++) {
            batch.draw(Game.gfx[0][4], x, pos.y);
        }
    }
}
