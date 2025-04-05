package persistance;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import model.AllBoards;
import model.Board;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ui.Game;

// to use a json file to setup current state of game

public class Writer {
    FileWriter writer;

    // EFFECTS: sets up writer
    public Writer(File file) throws IOException {
        writer = new FileWriter(file);
    }

    // MODIFIES: file
    // EFFECTS: writes playerIndex and the current state of all boards into a json file
    public void writeToJson(AllBoards allBoards) throws IOException {
        JSONObject finalJson = new JSONObject();

        finalJson.put("playerIndex", allBoards.getIndexOfPlayersBoard());
        finalJson.put("allBoards", writeAllBoards(allBoards));

        writer.write(finalJson.toJSONString());
        writer.close();
    }

    // EFFECTS: writes all boards in json
    public JSONArray writeAllBoards(AllBoards allBoards) {
        JSONArray jsonAllBoards = new JSONArray();

        for (int counter = 0; counter < allBoards.size(); counter++) {
            jsonAllBoards.add(writeTiles(allBoards.getBoard(counter)));
        }

        return jsonAllBoards;
    }

    // EFFECTS: write the x, y, isMarked, hasShip of every tile with a ship or marked of given board in json
    public JSONArray writeTiles(Board board) {
        JSONArray jsonBoard = new JSONArray();
        JSONArray jsonTile;

        for (int counter = 0; counter < Game.BOARD_SIZE * Game.BOARD_SIZE; counter++) {
            if (board.tileAt(counter).getHasShip() || board.tileAt(counter).getIsMarked()) {
                jsonTile = new JSONArray();

                jsonTile.add(board.tileAt(counter).getXcoord());
                jsonTile.add(board.tileAt(counter).getYcoord());
                jsonTile.add(board.tileAt(counter).getIsMarked());
                jsonTile.add(board.tileAt(counter).getHasShip());

                jsonBoard.add(jsonTile);
            }
        }

        return jsonBoard;
    }


}
