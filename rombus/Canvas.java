

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Canvas extends JComponent {
    private ArrayList<Drawable> objectPool;
    private final int size = 9; // Количество ячеек по ширине и высоте
    private int cellSize; // Размер ячейки

    private int lastTouchX;
    private int lastTouchY;


    public boolean isRedTurn = true;
    public int redScore = 0;
    public int blueScore = 0;

    public Canvas() {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    public void initGame() {
        // Устанавливаем размер ячеек
        int w = getWidth(); // shirina game polya
        int h = getHeight(); // visota game polya
        if (w > h) {
            cellSize = h / size;
        } else {
            cellSize = w / size;
        }
        // Инициализируем объекты
        objectPool = new ArrayList<Drawable>();
        int p = size / 2;
        Line line;
        Cell cellObj;

        // Создаем клетки
        ArrayList<Cell> cells = new ArrayList<Cell>();
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1; j++) {
                // Ограничиваем пространство ромбом
                if (Math.abs(i - p) + Math.abs(j - p) <= (p - 1)) {
                    cellObj = new Cell(cellSize * i + cellSize / 2,cellSize * j + cellSize / 2, cellSize, this);
                    cells.add(cellObj);
                }
            }
        }

        // Создаем линии, выходящие из пересечений строго внутри ромба
        ArrayList<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1; j++) {
                // Ограничиваем пространство ромбом
                if (Math.abs(i - p) + Math.abs(j - p) < (p - 1)) {
                    // Горизонтальная линия
                    line = new Line(cellSize * i, cellSize * j, cellSize, true,this);
                    for (Cell cell: cells) {
                        if (cell.x == line.x + cellSize / 2 &&
                                (cell.y == line.y + cellSize / 2 ||
                                cell.y == line.y - cellSize / 2 - 1)) {
                            cell.addLine(line);
                        }
                    }
                    lines.add(line);
                    // Вертикальная линия
                    line = new Line(cellSize * i, cellSize * j, cellSize, false,this);
                    for (Cell cell: cells) {
                        if (cell.y == line.y + cellSize / 2 &&
                                (cell.x == line.x + cellSize / 2 ||
                                cell.x == line.x - cellSize / 2 - 1)) {
                            cell.addLine(line);
                        }
                    }
                    lines.add(line);
                }
            }
        }

        // Создаем линии по краям
        int i = 2;
        int j = p + 1;
        for (int k = 0; k < p - 1; k++) {
            // Горизонтальная линия
            line = new Line(cellSize * i, cellSize * j, cellSize, true,this);
            for (Cell cell: cells) {
                if (cell.x == line.x + cellSize / 2 &&
                        (cell.y == line.y + cellSize / 2 ||
                                cell.y == line.y - cellSize / 2 - 1)) {
                    cell.addLine(line);
                }
            }
            lines.add(line);
            // Вертикальная линия
            line = new Line(cellSize * j, cellSize * i, cellSize, false,this);
            for (Cell cell: cells) {
                if (cell.y == line.y + cellSize / 2 &&
                        (cell.x == line.x + cellSize / 2 ||
                                cell.x == line.x - cellSize / 2 - 1)) {
                    cell.addLine(line);
                }
            }
            lines.add(line);

            if (k < p - 2) {
                // Вертикальная линия
                line = new Line(cellSize * j, cellSize * (size - i - 1), cellSize, false,this);
                for (Cell cell: cells) {
                    if (cell.y == line.y + cellSize / 2 &&
                            (cell.x == line.x + cellSize / 2 ||
                                    cell.x == line.x - cellSize / 2 - 1)) {
                        cell.addLine(line);
                    }
                }
                lines.add(line);
                // Горизонтальная линия
                line = new Line(cellSize * (size - i - 1), cellSize * j, cellSize, true,this);
                for (Cell cell: cells) {
                    if (cell.x == line.x + cellSize / 2 &&
                            (cell.y == line.y + cellSize / 2 ||
                                    cell.y == line.y - cellSize / 2 - 1)) {
                        cell.addLine(line);
                    }
                }
                lines.add(line);
            }
            i++;
            j++;
        }
        // Добавляем клетки и линии в пул объектов
        objectPool.addAll(cells);
        objectPool.addAll(lines);
        isRedTurn = true;
    }

    @Override
    protected void processMouseEvent(MouseEvent mouseEvent){
        super.processMouseEvent(mouseEvent);
        int onmask = InputEvent.BUTTON1_DOWN_MASK;
        // proverka najatiya na levuyu knopky
        if (mouseEvent.getButton() == MouseEvent.BUTTON1 && mouseEvent.getID() == MouseEvent.MOUSE_PRESSED){
            int x = mouseEvent.getX(); // koordinata x klika
            int y = mouseEvent.getY(); //  koordinata y klika

            lastTouchX = x;
            lastTouchY = y;
            this.repaint();
            // проходим по всем объектам и проверяем нажали ли мы на них
            for (Drawable object : objectPool) {
                if (object.isClicked(x, y)) {
                    object.onClick(x, y);
                    break;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        drawGrid(graphics);
        drawBorder(graphics);
        // Отрисовываем каждый объект
        for (Drawable object : objectPool) {
            object.render(graphics);
        }
        // Показать точку нажатия (дебаг онли)
//        graphics.drawOval(lastTouchX, lastTouchY, 5, 5);
    }
    // Нарисовать сетку
    private void drawGrid(Graphics graphics) {
        graphics.setColor(new Color(118, 178, 255));
        graphics.setPaintMode();
        for (int i = 0; i < size; i++) {
            graphics.drawLine(0, cellSize * i, cellSize * size, cellSize * i );
            graphics.drawLine(cellSize * i, 0, cellSize * i, cellSize * size );
        }
    }
    // Нарисовать границу вокруг ромба
    private void drawBorder(Graphics graphics) {
        // Устанавливаем толщину линии
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setStroke(new BasicStroke(5));

        graphics.setColor(Color.BLACK);
        int offset = 1; // Отступ (в клетках)
        int p = (size - offset * 2) / 2;
        int x1, y1, x2, y2;
        for (int i = 0; i <= p; i++) {
            x1 = (i + offset) * cellSize;
            y1 = (p - i + offset) * cellSize;
            x2 = x1 + cellSize;
            y2 = y1;

            // Горизонтальные
            g2.draw(new Line2D.Float(x1, y1, x2, y2));
            g2.draw(new Line2D.Float(size*cellSize - x1, y1, size*cellSize - x2, y2));
            g2.draw(new Line2D.Float(x1, size*cellSize - y1, x2, size*cellSize - y2));
            g2.draw(new Line2D.Float(size*cellSize - x1, size*cellSize - y1, size*cellSize - x2, size*cellSize - y2));

            // Вертикальные линии
            g2.draw(new Line2D.Float(y1, x1, y2, x2));
            g2.draw(new Line2D.Float(size*cellSize - y1, x1, size*cellSize - y2, x2));
            g2.draw(new Line2D.Float(y1, size*cellSize - x1, y2, size*cellSize - x2));
            g2.draw(new Line2D.Float(size*cellSize - y1, size*cellSize - x1, size*cellSize - y2, size*cellSize - x2));
        }
    }
}
