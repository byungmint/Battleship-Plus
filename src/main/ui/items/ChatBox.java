package ui.items;

import ui.GameRenderer;

import javax.swing.*;
import java.awt.*;

public class ChatBox extends JTextArea {

    private JScrollPane jscrollPane;

    // EFFECTS: Constructs chat box
    public ChatBox() {
        this.setEditable(false);
        this.setBorder(GameRenderer.blackLine);
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setFont(GameRenderer.font);
        this.setText("Welcome to Battleship Mayhem!");

        jscrollPane = new JScrollPane();
        Dimension size = new Dimension((GameRenderer.CELL_NUM - 3) * GameRenderer.CELL_SIZE, GameRenderer.TEXT_HEIGHT);

        jscrollPane.setPreferredSize(size);
        jscrollPane.setViewportView(this);
        jscrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jscrollPane.setWheelScrollingEnabled(true);
    }

    // getter
    // EFFECTS: returns the JScrollPane
    public JScrollPane getjscrollPane() {
        return jscrollPane;
    }
}
