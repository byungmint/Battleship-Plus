package model;

// a single tile in a board game

public class Tile {
    private int xcoord;                // x coordinate of tile, starts from 1, from the top.
    private int ycoord;                // y coordinate of tile, starts from 1, from the left.
    private boolean isMarked;          // if attacked or not
    private boolean hasShip;           // if the tile holds a ship

    // EFFECT: creates a Tile with a location assigned, unmarked and without ships
    public Tile(int x, int y, boolean isMarked, boolean hasShip) {
        xcoord = x;
        ycoord = y;
        this.isMarked = false;
        this.hasShip = false;
    }

    // getters
    public int getXcoord() {
        return xcoord;
    }

    public int getYcoord() {
        return ycoord;
    }

    public boolean getIsMarked() {
        return isMarked;
    }

    public boolean getHasShip() {
        return hasShip;
    }

    // setters
    public void setIsMarked(boolean isMarked) {
        this.isMarked = isMarked;
    }

    public void setHasShip(boolean hasShip) {
        this.hasShip = hasShip;
    }


}
