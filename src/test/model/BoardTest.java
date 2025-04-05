package model;

import model.exceptions.PreExistingException;
import model.exceptions.NotStraightException;
import model.exceptions.WrongSizeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ui.Game.BOARD_SIZE;

class BoardTest {
    Board board;
    ShipPlacement shipPlacement;
    int totalSize;

    @BeforeEach
    public void setup(){
        board = new Board();
        shipPlacement = new ShipPlacement();

        board.fillBoard();
        totalSize = (BOARD_SIZE * BOARD_SIZE);
    }

    @Test
    public void testFillWithEmpty() {
        assertEquals(1, board.tileAt(0).getXcoord());
        assertEquals(1, board.tileAt(0).getYcoord());

        assertEquals(totalSize, board.size());
        assertEquals(BOARD_SIZE, board.tileAt(totalSize - 1).getXcoord());
        assertEquals(BOARD_SIZE, board.tileAt(totalSize - 1).getYcoord());

        assertTrue(board.shipPlacement.isEmpty());
    }

    @Test
    public void testTileAt() {
        assertEquals(1, board.tileAt(1,1).getXcoord());
        assertEquals(1, board.tileAt(1,1).getYcoord());

        assertEquals(BOARD_SIZE, board.tileAt(BOARD_SIZE, BOARD_SIZE).getXcoord());
        assertEquals(BOARD_SIZE, board.tileAt(BOARD_SIZE, BOARD_SIZE).getYcoord());

        board.addTestingShipPart(1,1, board);
        board.addTestingShipPart(1);
        try {
            board.tryAddingShip(2);
            // no exceptions should be thrown
        } catch (WrongSizeException | NotStraightException | PreExistingException e) {
            fail();
        }

        assertTrue(board.tileAt(1,1).getHasShip());
        assertTrue(board.tileAt(2,1).getHasShip());
        assertTrue(board.tileAt(0).getHasShip());
        assertTrue(board.tileAt(1).getHasShip());
    }

    @Test
    public void testMultipleShips() {
        board.addTestingShipPart(2,3, board);
        board.addTestingShipPart(2,1, board);
        board.addTestingShipPart(2,4, board);
        board.addTestingShipPart(2,2, board);
        board.addTestingShipPart(2,5, board);
        try {
            board.tryAddingShip(5);
            // no exceptions should be thrown
        } catch (WrongSizeException | NotStraightException | PreExistingException e) {
            fail();
        }

        board.addTestingShipPart(2,1, board);
        board.addTestingShipPart(1,1, board);
        board.addTestingShipPart(3,1, board);
        try {
            board.tryAddingShip(3);
            fail();
        } catch (WrongSizeException | NotStraightException e) {
            fail();
        } catch (PreExistingException e) {
            // AlreadyExistingException should be thrown
        }

        board.addTestingShipPart(3,6, board);
        board.addTestingShipPart(2,6, board);
        board.addTestingShipPart(1,6, board);
        try {
            board.tryAddingShip(3);
            // no exceptions should be thrown
        } catch (WrongSizeException | NotStraightException | PreExistingException e) {
            fail();
        }

        board.addTestingShipPart(1,1, board);
        try {
            board.tryAddingShip(1);
            // no exceptions should be thrown
        } catch (WrongSizeException | NotStraightException | PreExistingException e) {
            fail();
        }
    }

    @Test
    public void testImpossibleShips() {
        board.addTestingShipPart(1,1, board);
        board.addTestingShipPart(5,1, board);
        board.addTestingShipPart(2,1, board);

        try {
            board.tryAddingShip(3);
            fail();
        } catch (WrongSizeException | PreExistingException e) {
            fail();
        } catch (NotStraightException e) {
            // NotStraightException should be thrown
        }

        try {
            board.tryAddingShip(0);
            fail();
        } catch (NotStraightException | PreExistingException e) {
            fail();
        } catch (WrongSizeException e) {
            // WrongSizeException should be thrown
        }

        board.tileAt(3,3).setHasShip(true);
        board.addTestingShipPart(3,3, board);
        try {
            board.tryAddingShip(1);
            fail();
        } catch (NotStraightException | WrongSizeException e) {
            fail();
        } catch (PreExistingException e) {
            // AlreadyExistingException should be thrown
        }

        board.addTestingShipPart(2,2, board);
        try {
            board.tryAddingShip(2);
            fail();
        } catch (NotStraightException | PreExistingException e) {
            fail();
        } catch (WrongSizeException e) {
            // WrongSizeException should be thrown
        }

        board.addTestingShipPart(2,2, board);
        board.addTestingShipPart(1,1, board);
        try {
            board.tryAddingShip(2);
            fail();
        } catch (PreExistingException | WrongSizeException e) {
            fail();
        } catch (NotStraightException e) {
            // NotStraightException should be thrown
        }
    }

    @Test
    public void testShootingXY() {
        board.addTestingShipPart(1, 2, board);
        board.addTestingShipPart(2, 2, board);
        board.addTestingShipPart(3, 2, board);
        board.addTestingShipPart(4, 2, board);
        try {
            board.tryAddingShip(4);
            // no exceptions should be thrown
        } catch (WrongSizeException | NotStraightException | PreExistingException e) {
            fail();
        }

        assertTrue(board.mark(3,1, board));
        assertFalse(board.tileAt(3,1).getHasShip());

        assertTrue(board.mark(3,2, board));
        assertTrue(board.tileAt(3,2).getHasShip());

        assertFalse(board.mark(3,1, board));
    }

    @Test
    public void testShootingIndex() {
        board.addTestingShipPart(0);
        board.addTestingShipPart(1);
        board.addTestingShipPart(2);
        board.addTestingShipPart(3);
        try {
            board.tryAddingShip(4);
            // no exceptions should be thrown
        } catch (WrongSizeException | NotStraightException | PreExistingException e) {
            fail();
        }

        assertTrue(board.mark(5, board));
        assertFalse(board.tileAt(5).getHasShip());
        assertTrue(board.tileAt(5).getIsMarked());

        assertFalse(board.tileAt(3).getIsMarked());
        assertTrue(board.mark(3, board));
        assertTrue(board.tileAt(3).getHasShip());
        assertTrue(board.tileAt(3).getIsMarked());

        assertFalse(board.mark(3,board));
        assertFalse(board.mark(5,board));
    }

    @Test
    public void testAutoShooting() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            board.autoAddShip(BOARD_SIZE);
        }

        assertEquals(BOARD_SIZE * BOARD_SIZE, board.getNumOfUnsunkShips());

        for (int shootCounter = 1; shootCounter <= BOARD_SIZE * BOARD_SIZE; shootCounter++){
            board.autoMark(board);
        }
        assertEquals(0, board.getNumOfUnsunkShips());
    }

    @Test
    public void testAutoGenerateBoard() {
        board.autoAddShip(4);

        if (board.isHorizontalTest) {
            assertTrue(board.tileAt(board.randomChangingTest, board.randomConstantTest).getHasShip());
            assertTrue(board.tileAt(board.randomChangingTest + 1, board.randomConstantTest).getHasShip());
            assertTrue(board.tileAt(board.randomChangingTest + 2, board.randomConstantTest).getHasShip());
            assertTrue(board.tileAt(board.randomChangingTest + 3, board.randomConstantTest).getHasShip());
        } else {
            assertTrue(board.tileAt(board.randomConstantTest, board.randomChangingTest).getHasShip());
            assertTrue(board.tileAt(board.randomConstantTest, board.randomChangingTest + 1).getHasShip());
            assertTrue(board.tileAt(board.randomConstantTest, board.randomChangingTest + 2).getHasShip());
            assertTrue(board.tileAt(board.randomConstantTest, board.randomChangingTest + 3).getHasShip());
        }
        assertEquals(4, board.getNumOfUnsunkShips());

        for (int count = 0; count < 5; count++) {
            board.autoAddShip(3);
        }
        assertEquals(19, board.getNumOfUnsunkShips());
    }

    @Test
    public void testGetNumOfSunkShips() {
        board.autoAddShip(4);
        assertEquals(4, board.getNumOfUnsunkShips());

        board.autoAddShip(3);
        assertEquals(7, board.getNumOfUnsunkShips());
    }

    @Test
    public void testIsStillPlaying() {
        assertFalse(board.isStillPlaying());

        board.addTestingShipPart(1,1, board);
        try {
            board.tryAddingShip(1);
            // no exceptions should be thrown
        } catch (WrongSizeException | NotStraightException | PreExistingException e) {
            fail();
        }

        assertTrue(board.isStillPlaying());

        board.mark(1,1, board);
        assertFalse(board.isStillPlaying());
    }

}