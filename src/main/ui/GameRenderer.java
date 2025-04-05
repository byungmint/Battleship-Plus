package ui;

import model.AllBoards;
import model.Board;
import ui.items.BoardList;
import ui.items.BoardViewer;
import ui.items.ChatBox;
import ui.items.MyMenuBar;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.*;
import javax.swing.border.Border;


public class GameRenderer extends JFrame {

    public static int CELL_NUM = 9;                           // number of coordinates in one side of square
    public static int CELL_SIZE = 75;                         // size of each cell in board
    public static int PADDINGX = 10;                          // size of padding for width
    public static int PADDINGY = CELL_SIZE + PADDINGX;        // size of padding for height
    public static int TEXT_HEIGHT = CELL_SIZE * 3;            // height of text area and view
    public static int JMENUBAR_HEIGHT = 30;                   // height of JMenuBar
    public static Border blackLine = BorderFactory.createLineBorder(Color.BLACK, 1);  // normal black border
    public static Font font = new Font("Serif", Font.PLAIN, 18);                    // larger font

    public static int WIDTH = (CELL_NUM * CELL_SIZE) + PADDINGX;
    public static int HEIGHT = (CELL_NUM * CELL_SIZE) + TEXT_HEIGHT + JMENUBAR_HEIGHT + PADDINGY;

    private boolean isMouseEnabled;
    public JMenuItem save;
    public JMenuItem continueGame;
    public JSlider speedScale;
    private ChatBox chatBox;
    private BoardList boardList;
    private DefaultListModel<String> listModel;
    private int currentlyDisplaying;
    private GridBagLayout myLayout;
    private GridBagConstraints gbc;
    private ArrayList<JPanel> cells;
    private HashSet<Integer> userSelectedCellsList;
    private Game game;

    // Method of organizing GUI related code inspired by
    //              https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase
    //          Class: SpaceInvaders        Method: SpaceInvaders()

    // MODIFIES: this
    // EFFECTS: sets up window for Battleship, and runs the game
    public GameRenderer(Game game) {
        super("Battleship Mayhem");
        myLayout = new GridBagLayout();
        gbc = new GridBagConstraints();
        this.setLayout(myLayout);
        this.game = game;
        cells = new ArrayList<>();
        userSelectedCellsList = new HashSet<>();
        initializeMainScreen();
    }

    // MODIFIES: this
    // EFFECTS: sets up main JFrame window for the main screen of Battleship
    public void initializeMainScreen() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize((new Dimension(WIDTH, HEIGHT)));
        setResizable(false);

        initializeBoardViewer();
        initializeChatBox();
        initializeBoardList();
        initializeMenuBar();

        centreOnScreen();
        this.pack();
        this.setVisible(true);
    }


    // Method of centering JFrame copied from
    //              https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase
    //          Class: SpaceInvaders         Method: centreOnScreen()
    // MODIFIES: this
    // EFFECTS:  location of frame is set so frame is centred on desktop
    private void centreOnScreen() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }

    // Method of adding object in grid layout copied and modified from
    //              https://stackoverflow.com/questions/30656473/how-to-use-gridbaglayout
    //      In comment by: Antonio Aguilar              Class: Cockatoo
    // MODIFIES: this
    // EFFECTS: adds a component to a given grid layout
    public void addObject(Component component, int gridX, int gridY, int gridW, int gridH) {
        gbc.gridx = gridX;
        gbc.gridy = gridY;

        gbc.gridwidth = gridW;
        gbc.gridheight = gridH;

        myLayout.setConstraints(component, gbc);
        this.add(component);
    }

    // Method of temporarily enabling / disabling mouse listener inspired by
    //              https://stackoverflow.com/questions/14699430/temporarily-disable-mouselistener
    //      In comment by: Hovercraft Full of Eels


    // MODIFIES: this
    // EFFECTS: sets up the JPanel holding the grid, where the board would be viewed
    public void initializeBoardViewer() {
        BoardViewer boardPanel = new BoardViewer();
        isMouseEnabled = false;
        cells = boardPanel.getCells();
        boardPanel.addMouseListener(new MouseHandlerForBoard());
        addObject(boardPanel, 0,0,5,5);
    }

    // MODIFIES: this
    // EFFECTS: sets up the JTextArea, where all the instructions and what's happening will be printed to
    public void initializeChatBox() {
        chatBox = new ChatBox();
        addObject(chatBox.getjscrollPane(),0,6,3,1);
    }

    // MODIFIES: this
    // EFFECTS: sets up the JList, where the player can manually choose which board to view if necessary
    public void initializeBoardList() {
        listModel = new DefaultListModel<>();
        boardList = new BoardList(listModel);

        boardList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                currentlyDisplaying = boardList.getSelectedIndex();
                updateBoard();
            }
        });

        addObject(boardList, 3,6,2,1);
    }

    // MODIFIES: this
    // EFFECTS: changes selected board into board of given index
    public void setSelectedBoard(int index) {
        currentlyDisplaying = index;
        boardList.setSelectedIndex(index);
    }

    // MODIFIES: this
    // EFFECTS: sets up JMenuBar, where the player can save and adjust the amount of time each action waits after
    public void initializeMenuBar() {
        MyMenuBar menuBar = new MyMenuBar(game, this);
        this.setJMenuBar(menuBar);
    }

    // MODIFIES: this
    // EFFECTS: enables saving and continue button for when game is paused in between turns
    public void pauseGame() {
        save.setEnabled(true);
        continueGame.setEnabled(true);
        continueGame.getAction().setEnabled(true);
        addToChat("Momentary ceasefire! Take the chance to view the current state of all boards, "
                + "or save. Press 'Contine' to continue.");
    }

    // MODIFIES: this
    // EFFECTS: updates GUI board with board of given index, automatically deciding if player's board or not
    public void updateBoard() {
        if (game.getIndexOfPlayersBoard() == currentlyDisplaying) {
            updatePlayersBoard(game.getBoard(currentlyDisplaying));
        } else {
            updateComputersBoard(game.getBoard(currentlyDisplaying));
        }
    }

    // MODIFIES: this
    // EFFECTS: updates GUI board with given board and given colors
    public void updateBoard(Board board, Color ship) {
        int rowCounter = 0;
        for (int tileCounter = 0; tileCounter <= board.size() - 1; tileCounter++) {
            rowCounter++;
            if (board.tileAt(tileCounter).getHasShip() && board.tileAt(tileCounter).getIsMarked()) {
                cells.get(tileCounter).setBackground(Color.RED);
            } else if (board.tileAt(tileCounter).getHasShip()) {
                cells.get(tileCounter).setBackground(ship);
            } else if (board.tileAt(tileCounter).getIsMarked()) {
                cells.get(tileCounter).setBackground(Color.CYAN);
            } else {
                cells.get(tileCounter).setBackground(Color.BLUE);
            }

            if (rowCounter == Game.BOARD_SIZE) {
                rowCounter = 0;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: updates GUI board with given board, with ships visible to player
    public void updatePlayersBoard(Board board) {
        updateBoard(board, Color.LIGHT_GRAY);
    }

    // MODIFIES: this
    // EFFECTS: updates GUI board with given board, with ships not visible to player
    public void updateComputersBoard(Board board) {
        updateBoard(board, Color.BLUE);
    }

    // a mouse handler to respond to mouse events within the board
    private class MouseHandlerForBoard extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (!isMouseEnabled) {
                return;
            }
            userSelectedCellsList.add(getCellIndexOfJPanel(e.getPoint()));
            cells.get(getCellIndexOfJPanel(e.getPoint())).setBackground(Color.CYAN);
        }
    }

    // EFFECTS: return the list of given number of selected cells' positions
    public HashSet<Integer> userInputOnBoard(int num) {
        isMouseEnabled = true;
        while (userSelectedCellsList.size() < num) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // this is due to programming error
            }
        }

        isMouseEnabled = false;
        return userSelectedCellsList;
    }

    // MODIFIES: this
    // EFFECTS: sets userSelectedCellsList to empty
    public void setUserSelectedCellsListEmpty() {
        userSelectedCellsList.clear();
    }

    // EFFECTS: returns the index number of the JPanel the mouse is on
    public int getCellIndexOfJPanel(Point point) {
        int index = 0;
        int x = point.x;
        int y = point.y;

        while (x >= CELL_SIZE) {
            x = x - CELL_SIZE;
            index++;
        }
        while (y >= CELL_SIZE) {
            y = y - CELL_SIZE;
            index = index + CELL_NUM;
        }

        return index;
    }

    // MODIFIES: this
    // EFFECTS: Updates JList with the correct boards in given allBoards
    public void updateJList(AllBoards allBoards) {
        for (int i = 0; i < allBoards.size(); i++) {
            if (i == game.getIndexOfPlayersBoard()) {
                listModel.addElement("Player");
            } else {
                listModel.addElement("Computer " + (i + 1));
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds new string to ChatBox on a new line
    public void addToChat(String string) {
        chatBox.append("\n" + string);
        scrollDown();
    }

    // Method of scrolling down to bottom copied from:
    //              https://stackoverflow.com/questions/5147768/scroll-jscrollpane-to-bottom
    // Comment by Decesus
    // MODIFIES: this
    // EFFECTS: scrolls chatBox to bottom
    public void scrollDown() {
        chatBox.setCaretPosition(chatBox.getDocument().getLength());
    }
}
