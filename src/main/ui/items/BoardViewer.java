package ui.items;

import ui.GameRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardViewer extends JPanel {
    private ArrayList<JPanel> cells;

    // EFFECTS: constructs main board viewer
    public BoardViewer() {
        cells = new ArrayList<>();
        int cellNumber = GameRenderer.CELL_NUM;
        int cellSize = GameRenderer.CELL_SIZE;

        this.setLayout(new GridLayout(cellNumber, cellNumber));
        this.setPreferredSize(new Dimension(cellNumber * cellSize, cellNumber * cellSize));

        for (int i = 0; i < GameRenderer.CELL_NUM * GameRenderer.CELL_NUM; i++) {
            JPanel cell = new JPanel();
            cell.setBorder(GameRenderer.blackLine);
            cell.setBackground(Color.BLUE);
            this.add(cell, i);
            cells.add(cell);
        }
    }

    // getter
    // EFFECTS: return cells
    public ArrayList<JPanel> getCells() {
        return cells;
    }

}
