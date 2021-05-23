
import javax.swing.*;
import java.awt.*;

public class Line extends Drawable {
    private final int size;
    public final boolean isHorizontal;
    public boolean isVisible;
    public Color color;

    public Line(int x, int y, int size, boolean isHorizontal, Canvas context) {
        super(x, y, context);
        this.size = size;
        this.isHorizontal = isHorizontal;
        this.isVisible = false;
        this.color = Color.blue;
    }

    public Line(int x, int y, int size, boolean isHorizontal, boolean isVisible, Color color, Canvas context) {
        super(x, y, context);
        this.size = size;
        this.isHorizontal = isHorizontal;
        this.isVisible = isVisible;
        this.color = color;
    }

    @Override
    public void render(Graphics graphics) {
        if (!isVisible) {
            return;
        }
        // Показать коллайдер (debug only)
//        for (int i = 0; i < context.getHeight(); i += 5) {
//            for (int j = 0; j < context.getWidth(); j += 5) {
//                if (isClicked(j, i)) {
//                    graphics.drawOval(j, i, 3, 3);
//                }
//            }
//        }
        graphics.setColor(color);
        if (isHorizontal) {
            graphics.drawLine(x, y, x + size, y);
        } else {
            graphics.drawLine(x, y, x, y + size);
        }
    }

    @Override
    public boolean isClicked(int x, int y) {
        if (isHorizontal) {
            return Math.abs(size * (this.x + size / 2 - x)) + Math.abs(size * (this.y - y)) < size * size / 2 && Math.abs(this.y - y) < size / 4;
        } else {
            return Math.abs(size * (this.x - x)) + Math.abs(size * (this.y + size / 2 - y)) < size * size / 2 && Math.abs(this.x - x) < size / 4;
        }
    }

    @Override
    public void onClick(int x, int y) {
        if (isVisible) {
            return;
        }
        if (context.isRedTurn) {
            color = Color.RED;
        } else {
            color = Color.BLUE;
        }
        isVisible = true;
        context.isRedTurn = !context.isRedTurn;
        context.repaint();
    }

    public boolean isVisible() {
        return isVisible;
    }
}
