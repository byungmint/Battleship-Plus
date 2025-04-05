package ui.items.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

// To handle user input for continue button
public class ActionPauseGame extends AbstractAction {

    // MODIFIES: this
    // EFFECTS: constructs the Continue menu item
    public ActionPauseGame() {
        super("Continue");
    }

    // MODIFIES: this
    // EFFECTS: disables button when clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        setEnabled(false);
    }
}
