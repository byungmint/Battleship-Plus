package persistance;

import model.AllBoards;
import model.Board;
import model.exceptions.PreExistingException;
import model.exceptions.NotStraightException;
import model.exceptions.WrongSizeException;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

public class WriterTest {
    private static final String TEST_FILE = "./data/testWriter.json";

    AllBoards allBoards = new AllBoards();
    Board exampleBoard = new Board();
    Board computerExampleBoard = new Board();

    Writer writer;

    @BeforeEach
    public void setUp() throws IOException {
        writer = new Writer(new File(TEST_FILE));

        computerExampleBoard.fillBoard();
        exampleBoard.fillBoard();

        computerExampleBoard.addTestingShipPart(8,8, computerExampleBoard);
        computerExampleBoard.addTestingShipPart(7,8, computerExampleBoard);
        computerExampleBoard.addTestingShipPart(6,8, computerExampleBoard);

        try {
            computerExampleBoard.tryAddingShip(3);
        } catch (WrongSizeException | NotStraightException | PreExistingException e) {
            fail(); // no exceptions should have been thrown
        }
        allBoards.addBoard(computerExampleBoard);

        exampleBoard.addTestingShipPart(1,5, exampleBoard);
        exampleBoard.addTestingShipPart(2,5, exampleBoard);
        exampleBoard.addTestingShipPart(3,5, exampleBoard);

        try {
            exampleBoard.tryAddingShip(3);
        } catch (WrongSizeException | NotStraightException | PreExistingException e) {
            fail(); // no exceptions should have been thrown
        }

        exampleBoard.mark(1,8, exampleBoard);
        exampleBoard.mark(2,8, exampleBoard);
        allBoards.addBoard(exampleBoard);
        allBoards.setIndexOfPlayersBoard(1);
    }

    @Test
    public void testBufferedWriter() throws IOException, ParseException {
        writer.writeToJson(allBoards);

        AllBoards allBoards = new Reader(new File (TEST_FILE)).jsonGetBoards();
        int playerIndex = new Reader(new File (TEST_FILE)).jsonGetPlayerIndex();

        assertTrue(allBoards.getBoard(playerIndex).tileAt(1,5).getHasShip());
        assertTrue(allBoards.getBoard(playerIndex).tileAt(2,5).getHasShip());
        assertTrue(allBoards.getBoard(playerIndex).tileAt(3,5).getHasShip());
        assertFalse(allBoards.getBoard(playerIndex).tileAt(4,5).getHasShip());
        assertFalse(allBoards.getBoard(playerIndex).tileAt(1,6).getHasShip());

        assertTrue(allBoards.getBoard(playerIndex).tileAt(1,8).getIsMarked());
        assertTrue(allBoards.getBoard(playerIndex).tileAt(2,8).getIsMarked());
        assertFalse(allBoards.getBoard(playerIndex).tileAt(3,8).getIsMarked());

        assertFalse(allBoards.getBoard(0).tileAt(5,8).getHasShip());
        assertTrue(allBoards.getBoard(0).tileAt(6,8).getHasShip());
        assertTrue(allBoards.getBoard(0).tileAt(7,8).getHasShip());
        assertTrue(allBoards.getBoard(0).tileAt(8,8).getHasShip());
    }


}
