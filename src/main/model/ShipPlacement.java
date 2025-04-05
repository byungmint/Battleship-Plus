package model;

import model.exceptions.PreExistingException;
import model.exceptions.NotStraightException;
import model.exceptions.WrongSizeException;

import java.util.ArrayList;

// for valid placement of ships, both random and by player's input

public class ShipPlacement {
    ArrayList<Tile> shipPlacement;  // holds a list of Tiles with ships, does not require valid placement
    ArrayList<Integer> tempCoords;  // holds a temporary list of coordinates for comparing

    // EFFECTS: constructs empty potential ship holder to add tiles to
    public ShipPlacement() {
        shipPlacement = new ArrayList<>();
        tempCoords = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: add Tile to ShipPlacement
    public void addShipTile(Tile tile) {
        shipPlacement.add(tile);
    }

    // EFFECTS: throws no exceptions if valid placement for a ship with given size, otherwise thrown
    public void checkValidPlacement(int size) throws WrongSizeException, PreExistingException, NotStraightException {
        if ((shipPlacement.size() != size) || (shipPlacement.isEmpty())) {
            throw new WrongSizeException();
        }
        for (Tile tile : shipPlacement) {
            if (tile.getHasShip()) {
                throw new PreExistingException();
            }
        }

        boolean isYSame = isYTheSame();
        boolean isXSame = isXTheSame();

        if (isYSame == isXSame && size != 1) {
            throw new NotStraightException();
        } else if (isYSame) {
            for (Tile tile : shipPlacement) {
                tempCoords.add(tile.getXcoord());
            }
        } else {
            for (Tile tile : shipPlacement) {
                tempCoords.add(tile.getYcoord());
            }
        }

        sortTempCoords();
        isConsecutive();
    }

    // EFFECTS: return true if X coordinates remain the same for all of shipPlacement
    public boolean isXTheSame() {
        boolean result = true;
        int intialXcoord = shipPlacement.get(0).getXcoord();

        for (Tile tile : shipPlacement) {
            if (tile.getXcoord() != intialXcoord) {
                result = false;
                break;
            }
        }
        return result;
    }

    // EFFECTS: return true if Y coordinates remain the same for all of shipPlacement
    public boolean isYTheSame() {
        boolean result = true;
        int intialYcoord = shipPlacement.get(0).getYcoord();

        for (Tile tile : shipPlacement) {
            if (tile.getYcoord() != intialYcoord) {
                result = false;
                break;
            }
        }
        return result;
    }

    // MODIFIES: this
    // EFFECTS: sorts tempCoords from smallest to largest, using bubble sort
    public void sortTempCoords() {
        boolean sorted = false;

        int tempInt;

        while (!sorted) {
            sorted = true;
            for (int index = 0; index <= tempCoords.size() - 2; index++) {
                if (tempCoords.get(index + 1) < tempCoords.get(index)) {
                    sorted = false;
                    tempInt = tempCoords.get(index);
                    tempCoords.set(index, tempCoords.get(index + 1));
                    tempCoords.set(index + 1, tempInt);
                }
            }
        }
    }

    // EFFECTS: throws an exception if tempCoords are not consecutive numbers
    public void isConsecutive() throws NotStraightException {
        int tempInt = tempCoords.get(0);
        for (int index = 0; index <= tempCoords.size() - 1; index++) {
            if (tempCoords.get(index) != tempInt) {
                throw new NotStraightException();
            }
            tempInt++;
        }
    }


    // MODIFIES: this
    // EFFECTS: set all Tile's hasShip in ShipPlacement to true
    public void placeShip() {
        for (Tile tile : shipPlacement) {
            tile.setHasShip(true);
        }
    }

    // EFFECTS: return true if empty
    public boolean isEmpty() {
        return shipPlacement.isEmpty();
    }

    // MODIFIES: this
    // EFFECTS: clears arrays for reuse
    public void setEmpty() {
        shipPlacement.clear();
        tempCoords.clear();
    }
}
