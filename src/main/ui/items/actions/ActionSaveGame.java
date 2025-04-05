package ui.items.actions;

import ui.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;

// To handle user input for save button
public class ActionSaveGame extends AbstractAction {
    private Game game;

    // MODIFIES: this
    // EFFECTS: constructs the Save menu item
    public ActionSaveGame(Game game) {
        super("Save");
        this.game = game;
    }

    // MODIFIES: game
    // EFFECTS: saves game if clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        game.saveAllBoards();
    }
}
