
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Cell extends Drawable {
    private int size;
    private ArrayList<Line> lines;
    public boolean isFree = true;
    final int[][] GAME_OVER_MSG = {
            {0,1,1,0,0,0,1,1,0,0,0,1,0,1,0,0,0,1,1,0},
            {1,0,0,0,0,1,0,0,1,0,1,0,1,0,1,0,1,0,0,1},
            {1,0,1,1,0,1,1,1,1,0,1,0,1,0,1,0,1,1,1,1},
            {1,0,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,0,0,0},
            {0,1,1,0,0,1,0,0,1,0,1,0,1,0,1,0,0,1,1,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,1,0,0,1,0,0,1,0,0,1,1,0,0,1,1,1,0,0},
            {1,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,0,0,1,0},
            {1,0,0,1,0,1,0,1,0,0,1,1,1,1,0,1,1,1,0,0},
            {1,0,0,1,0,1,1,0,0,0,1,0,0,0,0,1,0,0,1,0},
            {0,1,1,0,0,1,0,0,0,0,0,1,1,0,0,1,0,0,1,0}};
    public Color color = new Color(238, 238, 238);

    public Cell(int x, int y, int size, Canvas context) {
        super(x, y, context);
        this.size = size;
        this.lines = new ArrayList<Line>();
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    @Override
    public void render(Graphics graphics) {
        int visibleLinesAround = 0;
        for (Line line : lines) {
            if (line.isVisible()) {
                visibleLinesAround++;
            }
        }
        if (visibleLinesAround == lines.size() && isFree) {
            if (!context.isRedTurn) {
                color = Color.RED;
                context.redScore++;
            } else {
                color = Color.BLUE;
                context.blueScore++;
            }
            isFree = false;
            System.out.println("Красные: "+Integer.toString(context.redScore)+", Синие: "+Integer.toString(context.blueScore));
            if (context.redScore + context.blueScore == 25){
                for (int y = 0; y < GAME_OVER_MSG.length; y++)
                    for (int x = 0; x < GAME_OVER_MSG[y].length; x++)
                        if (GAME_OVER_MSG[y][x] == 1) {
                            graphics.setColor(Color.white);
                            graphics.fill3DRect(x*11+5, y*11+10, 14, 14, true);
                            if (context.redScore > context.blueScore) {
                                graphics.setColor(Color.red);
                            } else {
                                graphics.setColor(Color.blue);
                            }

                            graphics.fill3DRect(x*11+5, y*11+10, 10, 10, true);
                        }
            }
        }
        if (color.equals(new Color(238, 238, 238))) {
            return;
        }
        graphics.setColor(color);
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 24));
        if (color.equals(Color.RED)) {
            graphics.drawString("X", x - size / 4, y + size / 4);
        } else {
            graphics.drawString("O", x - size / 4, y + size / 4);
        }
    }
}
