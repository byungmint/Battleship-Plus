package ui.items;

import ui.GameRenderer;

import javax.swing.*;
import java.awt.*;

public class BoardList extends JList<String> {

    // EFFECTS: constructs board list
    public BoardList(DefaultListModel<String> listModel) {
        this.setModel(listModel);
        this.setPreferredSize(new Dimension(3 * GameRenderer.CELL_SIZE, GameRenderer.TEXT_HEIGHT));
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setBorder(GameRenderer.blackLine);
    }
}
