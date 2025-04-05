package persistance;

import java.io.*;

import model.AllBoards;
import model.Board;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// to save to a json file the current state of the game

public class Reader {
    JSONParser parser;
    Object object;
    JSONObject jsonObject;

    // Method of parsing and typecasting from https://javainterviewpoint.com/read-json-java-jsonobject-jsonarray/

    // EFFECTS: sets up Reader, creates jsonObject based on given file
    public Reader(File file) throws IOException, ParseException {
        parser = new JSONParser();
        object = parser.parse(new FileReader(file));
        jsonObject = (JSONObject) object;
    }

    // EFFECTS: returns player index based on json object
    public int jsonGetPlayerIndex() {
        long tempLong = (long) jsonObject.get("playerIndex");
        return (int) tempLong;
    }

    // EFFECTS: constructs allBoards based on json object
    public AllBoards jsonGetBoards() {
        AllBoards allBoards = new AllBoards();
        JSONArray jsonArrayForBoards = (JSONArray) jsonObject.get("allBoards");

        for (Object jsonArrayForBoard : jsonArrayForBoards) {
            JSONArray jsonArrayForTiles = (JSONArray) jsonArrayForBoard;
            Board board = new Board();
            board.fillBoard();

            for (Object jsonArrayForTile : jsonArrayForTiles) {
                JSONArray tile = (JSONArray) jsonArrayForTile;

                long xcoordLong = (long) tile.get(0);
                int xcoord = (int) xcoordLong;
                long ycoordLong = (long) tile.get(1);
                int ycoord = (int) ycoordLong;
                boolean isMarked = (boolean) tile.get(2);
                boolean hasShip = (boolean) tile.get(3);

                board.tileAt(xcoord, ycoord).setIsMarked(isMarked);
                board.tileAt(xcoord, ycoord).setHasShip(hasShip);
            }
            allBoards.addBoard(board);
        }
        return allBoards;
    }

    // EFFECTS: returns false if given file is empty
    public boolean isEmpty(File file) {
        return (file.length()) == 0;
    }
}
