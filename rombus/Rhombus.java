

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Rhombus {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Rhombus"); // glavnoe menu
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //knopka zakrtia
        frame.setSize(600, 650); //size okna
        frame.setLayout(new BorderLayout()); //menedzer komponivki
        frame.setLocationRelativeTo(null); // chtobi okno bilo po centru
        frame.setResizable(false);

        Canvas canvas = new Canvas();
        frame.add(canvas);

        System.out.println("Starting...");
        frame.setVisible(true); // vidimost` okna;
        canvas.initGame();
    }
}
