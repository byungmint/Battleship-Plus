package model;

import model.exceptions.PreExistingException;
import model.exceptions.NotStraightException;
import model.exceptions.WrongSizeException;

import java.util.ArrayList;
import java.util.Random;

import static ui.Game.BOARD_SIZE;

// managing a single board

public class Board {
    ArrayList<Tile> board;
    ShipPlacement shipPlacement;
    Random random;

    public boolean isHorizontalTest;
    public int randomConstantTest;
    public int randomChangingTest;

    // EFFECTS: constructs empty board to add tiles to
    public Board() {
        board = new ArrayList<>();
        shipPlacement = new ShipPlacement();
        random = new Random();
    }

    // MODIFIES: this
    // EFFECTS: fills board with empty tiles; no tiles with ships nor marks
    public void fillBoard() {
        for (int y = 1; y <= BOARD_SIZE; y++) {
            for (int x = 1; x <= BOARD_SIZE; x++) {
                board.add(new Tile(x, y, false, false));
            }
        }
    }

    // REQUIRES: non empty board
    // EFFECTS: return the tile at given index of array
    public Tile tileAt(int index) {
        return board.get(index);
    }

    // REQUIRES: x and y has to be from 1 to BOARD_SIZE, non empty board
    // EFFECTS: return the tile with given coordinates
    public Tile tileAt(int xcoord, int ycoord) {
        int tempIndex = 0;

        while (board.get(tempIndex).getXcoord() != xcoord) {
            tempIndex++;
        }
        while (board.get(tempIndex).getYcoord() != ycoord) {
            tempIndex = tempIndex + BOARD_SIZE;
        }

        return board.get(tempIndex);
    }

    // EFFECTS: return the number of tiles in given board
    public int size() {
        return board.size();
    }

    // REQUIRES: non empty board, tile at x and y of board
    // MODIFIES: this
    // EFFECTS: add Tiles to shipPlacement to test if valid position
    public void addTestingShipPart(int x, int y, Board board) {
        shipPlacement.addShipTile(board.tileAt(x,y));
    }

    // REQUIRES: non empty board, tile at given index
    // MODIFIES: this
    // EFFECTS: add Tiles to shipPlacement to test if valid position
    public void addTestingShipPart(int index) {
        shipPlacement.addShipTile(this.tileAt(index));
    }

    // MODIFIES: this
    // EFFECTS: sets hasShip to true if valid ship position for given size
    public void tryAddingShip(int size) throws WrongSizeException, NotStraightException, PreExistingException {
        try {
            shipPlacement.checkValidPlacement(size);
            shipPlacement.placeShip();
        } finally {
            shipPlacement.setEmpty();
        }
    }

    // REQUIRES: non empty board
    // MODIFIES: this
    // EFFECTS: adds a randomly placed but valid ship of given size to this board
    public void autoAddShip(int size) {
        boolean additionSuccessful = false;

        while (!additionSuccessful) {
            boolean isHorizontal = random.nextBoolean();
            int randomConstant = random.nextInt(BOARD_SIZE) + 1;
            int randomChanging = random.nextInt(BOARD_SIZE - size + 1) + 1;

            if (isHorizontal) {
                for (int counter = 0; counter <= size - 1; counter++) {
                    this.shipPlacement.addShipTile(this.tileAt(randomChanging, randomConstant));
                    randomChanging++;
                }
            } else {
                for (int counter = 0; counter <= size - 1; counter++) {
                    this.shipPlacement.addShipTile(this.tileAt(randomConstant, randomChanging));
                    randomChanging++;
                }
            }

            try {
                this.tryAddingShip(size);
                additionSuccessful = true;
                setTestingVariables(isHorizontal, randomConstant, randomChanging, size);
            } catch (WrongSizeException | NotStraightException | PreExistingException e) {
                // nothing is supposed to happen -> loops again
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: sets up variables made for testing
    public void setTestingVariables(boolean isHorizontal, int randomConstant, int randomChanging, int size) {
        this.isHorizontalTest = isHorizontal;
        this.randomConstantTest = randomConstant;
        this.randomChangingTest = randomChanging - size;
    }


    // REQUIRES: non empty board, tile at x and y
    // EFFECTS: returns isMarked of tile at given x and y coordinate of given board
    public boolean isAlreadyMarked(int x, int y, Board board) {
        return board.tileAt(x,y).getIsMarked();
    }

    // REQUIRES: non empty board, tile at x and y
    // EFFECTS: returns isMarked of tile at given x and y coordinate of given board
    public boolean isAlreadyMarked(int index, Board board) {
        return board.tileAt(index).getIsMarked();
    }

    // REQUIRES: non empty board, tile at x and y
    // MODIFIES: this
    // EFFECTS: "attacks" and marks the tile at given x and y coordinate of given board, returning true if successful
    public boolean mark(int x, int y, Board board) {
        if (!isAlreadyMarked(x, y, board)) {
            board.tileAt(x, y).setIsMarked(true);
            return true;
        }
        return false;
    }

    // REQUIRES: non empty board, tile at x and y
    // MODIFIES: this
    // EFFECTS: "attacks" and marks the tile at given index of given board, returning true if successful
    public boolean mark(int index, Board board) {
        if (!isAlreadyMarked(index, board)) {
            board.tileAt(index).setIsMarked(true);
            return true;
        }
        return false;
    }

    // REQUIRES: non empty board, tile at x and y
    // MODIFIES: this
    // EFFECTS: automatically attack a random valid tile of a given board
    public void autoMark(Board board) {
        int randomX = random.nextInt(BOARD_SIZE) + 1;
        int randomY = random.nextInt(BOARD_SIZE) + 1;

        while (!board.mark(randomX, randomY, board)) {
            randomX = random.nextInt(BOARD_SIZE) + 1;
            randomY = random.nextInt(BOARD_SIZE) + 1;
        }
    }

    // EFFECTS: returns number of Tiles with hasShip true and isMarked false in the board
    public int getNumOfUnsunkShips() {
        int result = 0;

        for (Tile tile : board) {
            if (tile.getHasShip() && !tile.getIsMarked()) {
                result++;
            }
        }

        return result;
    }

    // EFFECTS: returns true if board has any tiles with unsunk ship (hasShip = true, isMarked = false)
    public boolean isStillPlaying() {
        return !(getNumOfUnsunkShips() == 0);
    }

}
