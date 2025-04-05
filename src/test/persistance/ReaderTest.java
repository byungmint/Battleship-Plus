package persistance;

import model.AllBoards;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

public class ReaderTest {
    private static final String TEST_FILE = "./data/testReader.json";

    AllBoards allBoards = new AllBoards();
    Reader reader;

    @BeforeEach
    public void setUp() throws IOException, ParseException {
        reader = new Reader(new File(TEST_FILE));
    }

    @Test
    public void testReadPlayerIndex() {
        assertEquals(1, reader.jsonGetPlayerIndex());
    }

    @Test
    public void testReadBoards() {
        allBoards.setAllBoards(reader.jsonGetBoards());
        assertTrue(allBoards.getBoard(0).tileAt(2,2).getIsMarked());
        assertTrue(allBoards.getBoard(0).tileAt(3,3).getIsMarked());
        assertTrue(allBoards.getBoard(0).tileAt(4,4).getIsMarked());

        assertTrue(allBoards.getBoard(reader.jsonGetPlayerIndex()).tileAt(1,1).getHasShip());
        assertTrue(allBoards.getBoard(reader.jsonGetPlayerIndex()).tileAt(2,1).getHasShip());
        assertTrue(allBoards.getBoard(reader.jsonGetPlayerIndex()).tileAt(3,1).getHasShip());

        assertFalse(allBoards.getBoard(2).tileAt(8,8).getIsMarked());
        assertTrue(allBoards.getBoard(2).tileAt(8,8).getHasShip());
        assertTrue(allBoards.getBoard(2).tileAt(7,8).getIsMarked());
        assertFalse(allBoards.getBoard(2).tileAt(7,8).getHasShip());
        assertFalse(allBoards.getBoard(2).tileAt(8,7).getIsMarked());
        assertFalse(allBoards.getBoard(2).tileAt(8,7).getHasShip());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty(new File(TEST_FILE)));

        File emptyFile = new File("./data/emptyFile.json");
        assertTrue(reader.isEmpty(emptyFile));
    }
}
