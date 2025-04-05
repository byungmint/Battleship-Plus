package model;

import model.exceptions.PreExistingException;
import model.exceptions.NotStraightException;
import model.exceptions.WrongSizeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ui.Game.BOARD_SIZE;


public class AllBoardsTest {
    AllBoards allBoards;

    @BeforeEach
    public void setup() {
        allBoards = new AllBoards();
    }

    @Test
    public void testGetBoard() {
        allBoards.setNumOfOpponents(3);
        allBoards.addComputerBoards();

        assertEquals(3, allBoards.size());
    }

    @Test
    public void testStorePlayersBoard() {
        allBoards.setNumOfOpponents(2);
        allBoards.addComputerBoards();

        assertEquals(2, allBoards.size());

        Board board = new Board();
        board.fillBoard();
        allBoards.initializePlayersBoard();

        assertEquals(3, allBoards.size());

        allBoards.getBoard(allBoards.getIndexOfPlayersBoard()).fillBoard();
        assertEquals(BOARD_SIZE * BOARD_SIZE, allBoards.getBoard(allBoards.getIndexOfPlayersBoard()).size());
    }

    @Test
    public void testIsVictorMultiple() {
        allBoards.setNumOfOpponents(2);
        allBoards.addComputerBoards();

        Board board = new Board();
        board.fillBoard();
        board.addTestingShipPart(1,1, board);
        try {
            board.tryAddingShip(1);
            // no exceptions should be thrown
        } catch (WrongSizeException | NotStraightException | PreExistingException e) {
            fail();
        }

        allBoards.initializePlayersBoard();

        assertFalse(allBoards.isVictor());

        for (int i = 0; i < allBoards.size(); i++) {
            for (int shoot = 0; shoot < (BOARD_SIZE * BOARD_SIZE); shoot++) {
                if (i != allBoards.getIndexOfPlayersBoard()) {
                    board = allBoards.getBoard(i);
                    board.autoMark(board);
                }
            }
        }

        assertTrue(allBoards.isVictor());
    }
}
