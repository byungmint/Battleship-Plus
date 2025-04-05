package model;

import model.exceptions.PreExistingException;
import model.exceptions.NotStraightException;
import model.exceptions.WrongSizeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Game;

import static org.junit.jupiter.api.Assertions.*;

public class ShipPlacementTest {
    ShipPlacement shipPlacement;

    @BeforeEach
    public void setup() {
        shipPlacement = new ShipPlacement();
    }

    @Test
    public void testIsPlacementValidHorizontal() {
        shipPlacement.addShipTile(new Tile (1, Game.BOARD_SIZE, false, false));
        shipPlacement.addShipTile(new Tile (2, Game.BOARD_SIZE, false, false));
        shipPlacement.addShipTile(new Tile (3, Game.BOARD_SIZE, false, false));
        shipPlacement.addShipTile(new Tile (4, Game.BOARD_SIZE, false, false));

        try {
            shipPlacement.checkValidPlacement(3);
            fail();
        } catch (WrongSizeException e) {
            // WrongSizeException should be thrown
        } catch (PreExistingException | NotStraightException e) {
            fail();
        }

        try {
            shipPlacement.checkValidPlacement(4);
        } catch (WrongSizeException | PreExistingException | NotStraightException e) {
            fail();
        }

        try {
            shipPlacement.checkValidPlacement(5);
            fail();
        } catch (WrongSizeException e) {
            // WrongSizeException should be thrown
        } catch (PreExistingException | NotStraightException e) {
            fail();
        }

    }

    @Test
    public void testIsPlacementValidVertical() {
        shipPlacement.addShipTile(new Tile (Game.BOARD_SIZE, 1, false, false));
        shipPlacement.addShipTile(new Tile (Game.BOARD_SIZE, 2, false, false));
        shipPlacement.addShipTile(new Tile (Game.BOARD_SIZE, 3, false, false));
        shipPlacement.addShipTile(new Tile (Game.BOARD_SIZE, 4, false, false));
        shipPlacement.addShipTile(new Tile (Game.BOARD_SIZE, 5, false, false));

        try {
            shipPlacement.checkValidPlacement(3);
            fail();
        } catch (WrongSizeException e) {
            // WrongSizeException should be thrown
        } catch (PreExistingException | NotStraightException e) {
            fail();
        }

        try {
            shipPlacement.checkValidPlacement(4);
            fail();
        } catch (WrongSizeException e) {
            // WrongSizeException should be thrown
        } catch (PreExistingException | NotStraightException e) {
            fail();
        }

        try {
            shipPlacement.checkValidPlacement(5);
            // should throw no exception
        } catch (WrongSizeException | PreExistingException | NotStraightException e) {
            fail();
        }
    }

    @Test
    public void testIsPlacementValidOverlapping() {
        Tile alreadyHasShip = new Tile(Game.BOARD_SIZE, Game.BOARD_SIZE - 3, false, false);
        alreadyHasShip.setHasShip(true);

        shipPlacement.addShipTile(new Tile (Game.BOARD_SIZE, Game.BOARD_SIZE - 1, false, false));
        shipPlacement.addShipTile(new Tile (Game.BOARD_SIZE, Game.BOARD_SIZE - 2, false, false));

        try {
            shipPlacement.checkValidPlacement(2);
            // should throw no exceptions
        } catch (WrongSizeException | PreExistingException | NotStraightException e) {
            fail();
        }

        shipPlacement.addShipTile(alreadyHasShip);

        try {
            shipPlacement.checkValidPlacement(3);
            fail();
        } catch (WrongSizeException | NotStraightException e) {
            fail();
        } catch (PreExistingException e) {
            // should throw AlreadExistingException
        }
    }

    @Test
    public void testSetHasShipTrue() {
        Tile tile1 = new Tile(Game.BOARD_SIZE, 1, false, false);
        Tile tile2 = new Tile(Game.BOARD_SIZE, 2, false, false);
        Tile tile3 = new Tile(Game.BOARD_SIZE, 3, false, false);
        Tile tile4 = new Tile(Game.BOARD_SIZE, 4, false, false);

        shipPlacement.addShipTile(tile1);
        shipPlacement.addShipTile(tile2);
        shipPlacement.addShipTile(tile3);

        shipPlacement.placeShip();

        assertTrue(tile1.getHasShip());
        assertTrue(tile2.getHasShip());
        assertTrue(tile3.getHasShip());
        assertFalse(tile4.getHasShip());
        assertFalse(shipPlacement.isEmpty());
    }

    @Test
    public void testSortingTempCoords() {
        shipPlacement.addShipTile(new Tile (5, Game.BOARD_SIZE, false, false));
        shipPlacement.addShipTile(new Tile (2, Game.BOARD_SIZE, false, false));
        shipPlacement.addShipTile(new Tile (4, Game.BOARD_SIZE, false, false));
        shipPlacement.addShipTile(new Tile (3, Game.BOARD_SIZE, false, false));

        try {
            shipPlacement.checkValidPlacement(4);
            // should throw no exceptions
        } catch (WrongSizeException | PreExistingException | NotStraightException e) {
            fail();
        }
    }

    @Test
    public void testOneTileShip() {
        shipPlacement.addShipTile(new Tile (1,1, false, false));
        try {
            shipPlacement.checkValidPlacement(1);
            // should throw no exceptions
        } catch (WrongSizeException | PreExistingException | NotStraightException e) {
            fail();
        }
    }

    @Test
    public void testImpossibleShips() {
        shipPlacement.addShipTile(new Tile (5, Game.BOARD_SIZE, false, false));
        shipPlacement.addShipTile(new Tile (5, Game.BOARD_SIZE, false, false));
        shipPlacement.addShipTile(new Tile (5, Game.BOARD_SIZE, false, false));
        shipPlacement.addShipTile(new Tile (5, Game.BOARD_SIZE, false, false));

        try {
            shipPlacement.checkValidPlacement(4);
            fail();
        } catch (WrongSizeException | PreExistingException e) {
            fail();
        } catch (NotStraightException e) {
            // should throw NotStraightException
        }
        shipPlacement.setEmpty();

        shipPlacement.addShipTile(new Tile (1, 1, false, false));
        shipPlacement.addShipTile(new Tile (5, 1, false, false));
        shipPlacement.addShipTile(new Tile (3, 1, false, false));
        shipPlacement.addShipTile(new Tile (2, 1, false, false));

        try {
            shipPlacement.checkValidPlacement(4);
            fail();
        } catch (WrongSizeException | PreExistingException e) {
            fail();
        } catch (NotStraightException e) {
            // should throw NotStraightException
        }
        shipPlacement.setEmpty();

        try {
            shipPlacement.checkValidPlacement(0);
            fail();
        } catch (WrongSizeException e) {
            // WrongSizeException should be thrown
        } catch (PreExistingException | NotStraightException e) {
            fail();
        }
    }
}
