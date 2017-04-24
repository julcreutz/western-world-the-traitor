package ww.game.state;

public abstract class State {
    public abstract void update();
    public abstract void render();
    public abstract void resize(int w, int h);
    public abstract void dispose();
}
