package model;

import java.util.ArrayList;
import java.util.Random;

import static ui.Game.THREE_TILE_SHIPS;
import static ui.Game.FOUR_TILE_SHIPS;
import static ui.Game.FIVE_TILE_SHIPS;

// stores all boards - the player's boards and the boards of however may opponents - as well as which is the player's

public class AllBoards {
    int numOfOpponents;      // number of computer opponents
    int indexOfPlayersBoard; // random index of player; dictates player's turn

    public ArrayList<Board> allBoards;
    public Random rand;

    public AllBoards() {
        allBoards = new ArrayList<>();
        rand = new Random();
    }

    // getters
    public int getIndexOfPlayersBoard() {
        return indexOfPlayersBoard;
    }

    // setters
    public void setIndexOfPlayersBoard(int num) {
        this.indexOfPlayersBoard = num;
    }

    public void setNumOfOpponents(int num) {
        numOfOpponents = num;
    }

    // EFFECTS: returns number of boards in allBoards
    public int size() {
        return allBoards.size();
    }

    // REQUIRES: non empty numOfOpponents
    // EFFECTS: returns board at given index
    public Board getBoard(int index) {
        return allBoards.get(index);
    }

    // REQUIRES: numOfOponents > 0
    // MODIFIES: this
    // EFFECTS: adds given number of randomly generated boards to allBoards.
    public void addComputerBoards() {
        for (int counter = 0; counter < numOfOpponents; counter++) {
            allBoards.add(counter, new Board());
            allBoards.get(counter).fillBoard();

            for (int threeShip = 0; threeShip < THREE_TILE_SHIPS; threeShip++) {
                allBoards.get(counter).autoAddShip(3);
            }
            for (int fourShip = 0; fourShip < FOUR_TILE_SHIPS; fourShip++) {
                allBoards.get(counter).autoAddShip(4);
            }
            for (int fiveShip = 0; fiveShip < FIVE_TILE_SHIPS; fiveShip++) {
                allBoards.get(counter).autoAddShip(5);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: sets up player's board to random index of allBoards, stores index of player's board
    public void initializePlayersBoard() {
        setIndexOfPlayersBoard(rand.nextInt(allBoards.size() + 1));

        allBoards.add(indexOfPlayersBoard, new Board());
    }

    // EFFECTS: returns true if player is the only one left
    public boolean isVictor() {
        boolean result = true;

        for (int index = 0; index < allBoards.size(); index++) {
            if (index != getIndexOfPlayersBoard() && (allBoards.get(index).isStillPlaying())) {
                result = false;
            }

        }
        return result;
    }

    // MODIFIES: this
    // EFFECTS: adds a given board to allBoards
    public void addBoard(Board board) {
        allBoards.add(board);
    }

    // MODIFIES: this
    // EFFECTS: when given allBoards, clears and adds all boards to allBoards
    public void setAllBoards(AllBoards allBoards) {
        this.allBoards.clear();

        for (int index = 0; index < allBoards.size(); index++) {
            this.addBoard(allBoards.getBoard(index));
        }
    }
}
