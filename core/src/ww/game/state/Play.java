package ww.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import ww.entity.*;
import ww.game.Game;

public class Play extends State {
    public SpriteBatch batch;
    public OrthographicCamera cam;
    public float shake;
    public ExtendViewport vp;

    public Array<Entity> entities;
    public Array<Rectangle> walls;

    public boolean won;
    public float wonY;

    public boolean gameOver;
    public float gameOverY;

    public int kills;
    public float time;

    public float resetTime;

    public float flash;

    public float fadeOut;
    public float fadeIn;

    public int streak;

    public float camX;

    public long intro;

    public Play() {
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 320, 240);
        vp = new ExtendViewport(320, 240, cam);
        vp.apply();

        reset();

        streak = 0;
    }

    @Override
    public void update() {
        Game.mouse = vp.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        Game.mouse.x += -cam.position.x + 160;

        final float off = 32;
        final float acc = 32;
        if (getPlayer().right && camX < off) {
            camX += acc * Gdx.graphics.getDeltaTime();
        } else if (!getPlayer().right && camX > -off) {
            camX -= acc * Gdx.graphics.getDeltaTime();
        }
        camX = MathUtils.clamp(camX, -off, off);

        if (!won && !gameOver) {
            for (int i = 0; i < entities.size; i++) {
                entities.get(i).update(this);
            }
            time -= Gdx.graphics.getDeltaTime();
        } else if (won) {
            wonY = MathUtils.lerp(wonY, 72, 4 * Gdx.graphics.getDeltaTime());
        } else {
            gameOverY = MathUtils.lerp(gameOverY, 72, 4 * Gdx.graphics.getDeltaTime());
        }
        cam.position.x = getPlayer().pos.x + 8 + camX;
        cam.position.x += MathUtils.random(-shake, shake);
        cam.position.x = MathUtils.clamp(cam.position.x, (vp.getWorldWidth() - 320) * .5f, 320 - (vp.getWorldWidth() - 320) * .5f);
        cam.position.y = 120 + MathUtils.random(-shake, shake);
        cam.update();
        if (shake > 0) {
            shake -= 10 * Gdx.graphics.getDeltaTime();
        }

        if (time < 0) {
            gameOver = true;
            time = 0;
        }
        if (kills >= 2) {
            gameOver = true;
        }

        if (won || gameOver) {
            resetTime += Gdx.graphics.getDeltaTime();
            if (resetTime > 2 && fadeOut <= 0) {
                fadeOut = 1;
            }
            if (resetTime > 2.5f) {
                if (won) {
                    reset();
                    streak++;
                }
                if (gameOver) {
                    reset();
                    Game.intro.stop();
                    Game.state = 1;
                    Game.title.fadeIn = 1;
                    streak = 0;
                }
            }
        }

        if (flash > 0) {
            flash -= 10 * Gdx.graphics.getDeltaTime();
        } else if (flash < 0) {
            flash = 0;
        }

        if (fadeOut > 0) {
            fadeOut -= Gdx.graphics.getDeltaTime() * 2;
        }
        if (fadeOut < 0) {
            fadeOut = 0;
        }
        if (fadeIn > 0) {
            fadeIn -= Gdx.graphics.getDeltaTime() * 2;
        }
        if (fadeIn < 0) {
            fadeIn = 0;
        }
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

        for (int x = 12; x <= 14; x++) {
            batch.draw(Game.gfx[0][x], cam.position.x + (x - 12) * 16 - 160, cam.position.y + 104);
        }
        drawNumber((int) time, cam.position.x - 129, cam.position.y + 107);

        for (int x = 15; x <= 17; x++) {
            batch.draw(Game.gfx[0][x], cam.position.x + (x - 15) * 16 - 160 + 48, cam.position.y + 104);
        }
        drawNumber(2 - kills, cam.position.x - 129 + 56, cam.position.y + 107);

        for (int x = 12; x <= 15; x++) {
            batch.draw(Game.gfx[4][x], cam.position.x + (x - 12) * 16 - 160 + 320 - 4 * 16, cam.position.y + 104);
        }
        drawNumber(streak, cam.position.x + 142, cam.position.y + 107);

        if (won) {
            for (int x = 7; x <= 11; x++) {
                for (int y = 2; y >= 0; y--) {
                    batch.draw(Game.gfx[2 - y][x], cam.position.x + (x - 7) * 16 - 40, cam.position.y + y * 16 + wonY);
                }
            }
        }
        if (gameOver) {
            for (int x = 7; x <= 11; x++) {
                for (int y = 5; y >= 3; y--) {
                    batch.draw(Game.gfx[8 - y][x], cam.position.x + (x - 7) * 16 - 40, cam.position.y + (y - 3) * 16 + gameOverY);
                }
            }
        }

        batch.setColor(1, 1, 1, flash);
        for (int x = -10; x < 30; x++) {
            for (int y = 0; y < 15; y++) {
                batch.draw(Game.gfx[1][5], x * 16 + cam.position.x - 160 - (vp.getWorldWidth() - 320) / 2, y * 16 + cam.position.y - 120);
            }
        }
        batch.setColor(1, 1, 1, 1);

        if (fadeOut > 0) {
            batch.setColor(1, 1, 1, MathUtils.round((1 - fadeOut) * 5f) / 5f);
            for (int x = -10; x < 30; x++) {
                for (int y = 0; y < 15; y++) {
                    batch.draw(Game.gfx[3][12], x * 16 + cam.position.x - 160 - (vp.getWorldWidth() - 320) / 2, y * 16 + cam.position.y - 120);
                }
            }
        }
        batch.setColor(1, 1, 1, 1);

        if (fadeIn > 0) {
            System.out.println(fadeIn);
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

    public Player getPlayer() {
        for (int i = 0; i < entities.size; i++) {
            if (entities.get(i).getClass() == Player.class) {
                return (Player) entities.get(i);
            }
        }
        return null;
    }

    public void drawNumber(int num, float x, float y) {
        String s = Integer.toString(num);
        for (int i = 0; i < s.length(); i++) {
            TextureRegion r = Game.gfx[2][16];
            switch (s.charAt(i)) {
                case '1':
                    r = Game.gfx[1][12];
                    break;
                case '2':
                    r = Game.gfx[1][13];
                    break;
                case '3':
                    r = Game.gfx[1][14];
                    break;
                case '4':
                    r = Game.gfx[1][15];
                    break;
                case '5':
                    r = Game.gfx[1][16];
                    break;
                case '6':
                    r = Game.gfx[2][12];
                    break;
                case '7':
                    r = Game.gfx[2][13];
                    break;
                case '8':
                    r = Game.gfx[2][14];
                    break;
                case '9':
                    r = Game.gfx[2][15];
                    break;
                case '0':
                    r = Game.gfx[2][16];
                    break;
            }
            batch.draw(r, x + i * 8, y);
        }
    }

    public void reset() {
        entities = new Array<Entity>();
        walls = new Array<Rectangle>();
        walls.add(new Rectangle(-160, 16, 640, 16));

        entities.add(new Saloon(MathUtils.random(-160, 384), 32));
        for (int x = -10; x < 30; x += 2) {
            if (MathUtils.randomBoolean(.7f)) {
                if (MathUtils.randomBoolean(.5f)) {
                    entities.add(new Cactus(x * 16, 32));
                } else {
                    entities.add(new Grass(x * 16, 32));
                }
            }
        }

        entities.add(new Player(MathUtils.random(-144, 464), 32));
        for (int i = 0; i < 10 + streak * 3; i++) {
            entities.add(new NPC(MathUtils.random(-144, 464), 32, i == 0));
        }

        wonY = 72 + 48;
        gameOverY = wonY;

        won = false;
        gameOver = false;

        kills = 0;
        time = 30 - streak * 2;
        time = MathUtils.clamp(time, 10, 30);

        resetTime = 0;

        Game.intro.stop();
        Game.intro.play();

        flash = 0;

        fadeIn = 1;
        fadeOut = 0;
    }
}
