package ui;

import model.Board;
import model.AllBoards;
import model.exceptions.PreExistingException;
import model.exceptions.NotStraightException;
import model.exceptions.WrongSizeException;
import org.json.simple.parser.ParseException;
import persistance.Reader;
import persistance.Writer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class Game {
    public static int BOARD_SIZE = 9;                 // number of coordinates in one side of square
    public static int THREE_TILE_SHIPS = 1;           // number of three tile ships
    public static int FOUR_TILE_SHIPS = 1;            // number of four tile ships
    public static int FIVE_TILE_SHIPS = 1;            // number of five tile ships
    public static int MAX_OPPONENTS = 7;              // maximum number of opponents
    public static int DELAY_AMOUNT = 2000;       // delay between each operation
    public static final String GAME_FILE = "./data/pastGame.json";

    private AllBoards allBoards;
    private boolean isAdditionSuccessful;
    private boolean isGameOngoing;
    private int indexOfPlayersBoard;
    private boolean hasPastGame;
    private Reader reader;
    private GameRenderer gameRenderer;
    private HashSet<Integer> tempSetOfUserInput;

    // EFFECTS: runs the game method
    public Game() {
        File file = new File(GAME_FILE);
        allBoards = new AllBoards();
        gameRenderer = new GameRenderer(this);
        tempSetOfUserInput = new HashSet<>();

        try {
            reader = new Reader(file);
            hasPastGame = true;
        } catch (IOException e) {
            // this is due to programming error
        } catch (ParseException e) {
            hasPastGame = false;
        } finally {
            startGame();
        }
    }

    // MODIFIES: this
    // EFFECTS: sets up boards and plays game until someone's victory
    public void startGame() {
        loadOrSetUp();

        while (isGameOngoing) {
            pauseGame();
            for (int whoseTurn = 0; whoseTurn < allBoards.size(); whoseTurn++) {
                if (!isGameOngoing || allBoards.isVictor()) {
                    break;
                }
                takeTurn(whoseTurn);
                delay();
            }
            isGameOngoing = (allBoards.getBoard(indexOfPlayersBoard).isStillPlaying() && !allBoards.isVictor());
        }

        finish();
    }

    // MODIFIES: this
    // EFFECTS: gives option of setting allBoards if save is detected; else, initialize new opponents and player board
    public void loadOrSetUp() {
        int response;

        if (hasPastGame) {
            response = JOptionPane.showConfirmDialog(null,
                    "Past game detected! Do you wish to load?", "Alert!", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                allBoards = reader.jsonGetBoards();
                indexOfPlayersBoard = reader.jsonGetPlayerIndex();
                gameRenderer.addToChat("Past game loaded!");
                gameRenderer.updatePlayersBoard(allBoards.getBoard(indexOfPlayersBoard));
            } else {
                setUpNewGame();
            }
        } else {
            setUpNewGame();
        }
        allBoards.setIndexOfPlayersBoard(indexOfPlayersBoard);
        printOrder();
        gameRenderer.updateJList(allBoards);
        isGameOngoing = true;
    }

    // MODIFIES: this
    // EFFECTS: starts new game
    public void setUpNewGame() {
        setupOpponentNum();
        setupPlayerBoard();
    }

    // MODIFIES: this
    // EFFECTS: sets up number of opponents the player would be facing
    public void setupOpponentNum() {
        boolean successful = false;
        String input;
        gameRenderer.addToChat("Creating new game...");
        while (!successful) {
            input = JOptionPane.showInputDialog(null, "Number of opponents you want to face?");
            if (turnStringToInteger(input) > 0 && turnStringToInteger(input) <= Game.MAX_OPPONENTS) {
                int num = turnStringToInteger(input);
                allBoards.setNumOfOpponents(num);
                allBoards.addComputerBoards();
                gameRenderer.addToChat(input + " opponent(s) has been added.");
                successful = true;
            } else {
                gameRenderer.addToChat("At least 1 opponent, and less than " + Game.MAX_OPPONENTS + ", please!");
            }
        }
    }

    // EFFECTS: if given string is a valid integer, return that integer; else return 0
    public int turnStringToInteger(String str) {
        if (str != null) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    // MODIFIES: this
    // EFFECTS: add all the ships to the player's board according to input; adds player's board to allBoards
    public void setupPlayerBoard() {
        gameRenderer.addToChat("Now add your ships to the board!");

        allBoards.initializePlayersBoard();
        indexOfPlayersBoard = allBoards.getIndexOfPlayersBoard();
        allBoards.getBoard(indexOfPlayersBoard).fillBoard();

        for (int threeShip = 0; threeShip < THREE_TILE_SHIPS; threeShip++) {
            addOneShip(3);
        }
        for (int fourShip = 0; fourShip < FOUR_TILE_SHIPS; fourShip++) {
            addOneShip(4);
        }
        for (int fiveShip = 0; fiveShip < FIVE_TILE_SHIPS; fiveShip++) {
            addOneShip(5);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a single ship of given size based on user input
    public void addOneShip(int size) {
        gameRenderer.addToChat("Place a ship " + size + " tiles long!");
        isAdditionSuccessful = false;

        while (!isAdditionSuccessful) {
            tempSetOfUserInput = gameRenderer.userInputOnBoard(size);

            for (int num : tempSetOfUserInput) {
                allBoards.getBoard(indexOfPlayersBoard).addTestingShipPart(num);
            }

            goThroughAddingExceptions(size);
        }
        gameRenderer.addToChat(" > Successfully added!");
        resetInput();
        gameRenderer.updatePlayersBoard(allBoards.getBoard(indexOfPlayersBoard));
    }

    // MODIFIES: this
    // EFFECTS: try adding ships, isAdditionSuccessful only set true if no exceptions are thrown
    public void goThroughAddingExceptions(int size) {
        try {
            allBoards.getBoard(indexOfPlayersBoard).tryAddingShip(size);
            isAdditionSuccessful = true;
        } catch (PreExistingException e) {
            gameRenderer.addToChat(" > Ships can't overlap!");
        } catch (NotStraightException e) {
            gameRenderer.addToChat(" > Ships has to be in a straight line!");
        } catch (WrongSizeException e) {
            gameRenderer.addToChat(" > Non valid ship size, try again!");
        } finally {
            resetInput();
            gameRenderer.updatePlayersBoard(allBoards.getBoard(indexOfPlayersBoard));
        }
    }

    // MODIFIES: this
    // EFFECTS: resets user input
    private void resetInput() {
        tempSetOfUserInput.clear();
        gameRenderer.setUserSelectedCellsListEmpty();
    }

    // REQUIRES: non empty allBoards
    // MODIFIES: Boards within allBoards
    // EFFECTS: if the board at given index is a computer, they take their turn; otherwise, the player takes their turn
    //                 player can choose to save at the beginning of their turn
    public void takeTurn(int currentTurn) {
        if (currentTurn == allBoards.getIndexOfPlayersBoard()) {
            gameRenderer.addToChat("  > Your turn!");
            if (allBoards.getBoard(allBoards.getIndexOfPlayersBoard()).isStillPlaying()) {
                attackOthers();
            } else {
                isGameOngoing = false;
            }

        } else {
            autoAttackOthers(currentTurn);
        }
    }

    // REQUIRES: non empty allBoards
    // MODIFIES: this
    // EFFECTS: player takes their turn, attacking all other opponents
    public void attackOthers() {
        for (int opponent = 0; opponent < allBoards.size(); opponent++) {
            if (opponent != allBoards.getIndexOfPlayersBoard() && allBoards.getBoard(opponent).isStillPlaying()) {
                gameRenderer.addToChat("You are attacking Computer " + (opponent + 1) + "!");
                gameRenderer.setSelectedBoard(opponent);
                gameRenderer.updateBoard();
                userInputAttack(allBoards.getBoard(opponent));
                gameRenderer.updateBoard();
                alwaysDelay();
            }
        }
    }


    // Using Integer.parseInt with try and catch inspired by https://www.baeldung.com/java-check-string-number

    // REQUIRES: non empty allBoards, userInput is valid
    // MODIFIES: this
    // EFFECTS: player attacks a boards based on userInput, tile cannot be already marked
    public void userInputAttack(Board board) {
        boolean attackCompleted = false;

        while (!attackCompleted) {
            tempSetOfUserInput = gameRenderer.userInputOnBoard(1);

            if (tempSetOfUserInput.size() == 1) {
                int tempIndex = -1;
                for (int index : tempSetOfUserInput) {
                    tempIndex = index;
                }

                attackCompleted = board.mark(tempIndex, board);
                if (attackCompleted && board.tileAt(tempIndex).getHasShip()) {
                    gameRenderer.addToChat(" > You hit a ship!");
                } else if (attackCompleted) {
                    gameRenderer.addToChat(" > Nothing was hit.");
                }
            }
            resetInput();
            gameRenderer.updateBoard();
        }
    }

    // REQUIRES: non empty allBoards
    // MODIFIES: this
    // EFFECTS: computer attacks every board but their own
    public void autoAttackOthers(int current) {
        if (allBoards.getBoard(current).isStillPlaying()) {
            gameRenderer.addToChat(" > Computer " + (current + 1) + "'s turn!");
            for (int target = 0; target < allBoards.size(); target++) {
                if (target != current && allBoards.getBoard(target).isStillPlaying()) {
                    gameRenderer.setSelectedBoard(target);
                    gameRenderer.updateBoard();
                    delay();
                    allBoards.getBoard(target).autoMark(allBoards.getBoard(target));

                    if (target == allBoards.getIndexOfPlayersBoard()) {
                        gameRenderer.addToChat("Computer " + (current + 1) + " attacks you.");
                        if (!allBoards.getBoard(allBoards.getIndexOfPlayersBoard()).isStillPlaying()) {
                            isGameOngoing = false;
                        }
                    } else {
                        gameRenderer.addToChat("Computer " + (current + 1) + " attacks Computer " + (target + 1) + ".");
                    }
                    gameRenderer.updateBoard();
                    delay();
                }
            }
        } else {
            gameRenderer.addToChat("<!> Computer " + (current + 1) + " no longer has any ships!");
        }
    }

    // EFFECTS: prints out order of turns
    public void printOrder() {
        gameRenderer.addToChat("The turn order is: ");
        for (int index = 0; index < allBoards.size(); index++) {
            if (index == allBoards.getIndexOfPlayersBoard()) {
                gameRenderer.addToChat("Your turn!");
            } else {
                gameRenderer.addToChat("Computer " + (index + 1) + "!");
            }
        }
        gameRenderer.addToChat("And let the game begin!\n");
    }

    // MODIFIES: this
    // EFFECTS: pauses game in between turns, and allows the player to save or continue
    public void pauseGame() {
        gameRenderer.pauseGame();
        boolean isGameUnpaused = false;

        while (!isGameUnpaused) {
            isGameUnpaused = !gameRenderer.continueGame.getAction().isEnabled();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // no exception expected
            }
        }

        gameRenderer.save.setEnabled(false);
    }

    // EFFECTS: saves current state of allBoards to GAME_FILE if user input is "save"
    public void saveAllBoards() {
        try {
            Writer writer = new Writer(new File(GAME_FILE));
            writer.writeToJson(allBoards);
        } catch (IOException e) {
            // this is due to programming error
        }
    }

    // EFFECTS: finishes game, prints out outcome and deletes file
    public void finish() {
        if (allBoards.isVictor()) {
            gameRenderer.addToChat("Congratulations, you won!");
        } else {
            gameRenderer.addToChat("You no longer have any ships. You've been defeated!");
        }

        Writer writer = null;
        try {
            writer = new Writer(new File(GAME_FILE));
        } catch (IOException e) {
            // this is due to programming error
        }

        (new File(GAME_FILE)).delete();
    }

    // MODIFIES: this
    // EFFECTS: delays next action by DELAY_AMOUNT
    public void delay() {
        int setSpeed = gameRenderer.speedScale.getValue();
        try {
            Thread.sleep(DELAY_AMOUNT * setSpeed);
        } catch (InterruptedException e) {
            // no exception should be thrown
        }
    }

    // EFFECTS: always delays, regardless of DELAY_AMOUNT
    public void alwaysDelay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // no exception should be thrown
        }
    }

    // getter for indexOfPlayersBoard
    // EFFECTS: returns indexOfPlayersBoard
    public int getIndexOfPlayersBoard() {
        return indexOfPlayersBoard;
    }

    // getter for a particular board of allBoard
    // EFFECTS: returns board of allBoards in given index
    public Board getBoard(int index) {
        return allBoards.getBoard(index);
    }
}
