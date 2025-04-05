package ui.items;

import ui.Game;
import ui.GameRenderer;
import ui.items.actions.ActionPauseGame;
import ui.items.actions.ActionSaveGame;

import javax.swing.*;
import java.awt.*;

public class MyMenuBar extends JMenuBar {

    // EFFECTS: construct menu bar
    public MyMenuBar(Game game, GameRenderer gameRenderer) {
        Dimension size = new Dimension(GameRenderer.WIDTH + GameRenderer.PADDINGX, GameRenderer.JMENUBAR_HEIGHT);
        this.setPreferredSize(size);
        JMenu options = new JMenu("Options");

        gameRenderer.continueGame = new JMenuItem(new ActionPauseGame());
        gameRenderer.save = new JMenuItem(new ActionSaveGame(game));

        JMenu speed = new JMenu("Adjust Speed");

        gameRenderer.speedScale = new JSlider(0,3,1);
        gameRenderer.speedScale.setPaintLabels(true);
        gameRenderer.speedScale.setMajorTickSpacing(1);
        speed.add(new JLabel("Seconds before next action: "));
        speed.add(gameRenderer.speedScale);

        this.add(options);
        this.add(gameRenderer.continueGame);
        options.add(gameRenderer.save);
        options.add(speed);

        gameRenderer.save.setEnabled(false);
        gameRenderer.continueGame.setEnabled(false);
    }
}
