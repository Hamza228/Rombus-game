

import javax.swing.*;
import java.awt.*;

public abstract class Drawable {
    public int x, y;
    public final Canvas context;

    public Drawable(int x, int y, Canvas context) {
        this.x = x;
        this.y = y;
        this.context = context;
    }
    public abstract void render(Graphics graphics);

    public boolean isClicked(int x, int y) {
        return false;
    }

    public void onClick(int x, int y) {};
}
