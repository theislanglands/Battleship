package battleship.domain;

import java.awt.Point;
import java.util.Arrays;

/**
 * @author TheisLanglands
 */


public class Board {

    private int gridSize;
    Cell[][] grid;

    public Board(int gridSize) {
        this.gridSize = gridSize;
        this.grid = new Cell[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
                Arrays.fill(this.grid[i], Cell.EMPTY);
        }
    }

    public Cell getValue(Point p) {
        return grid[p.x][p.y];
    }

    public void setValue(Point p, Cell value) {
        grid[p.x][p.y] = value;
    }

    public void clearBoard(){
        this.grid = new Cell[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            Arrays.fill(this.grid[i], Cell.EMPTY);
        }
    }

    public int getGridSize() {
        return gridSize;
    }


    public String printShipsAndHits(){

        String result = "\t\t";
        String symbol;

        // add numbers
        for (int i = 1; i <= gridSize; i++) {
            result += (i + " ");
        }
        result += "\n";

        // adds rows
        for (int i = 0; i < gridSize; i++) {                      // rows i
            result += "\t" + (char) (65 + i) + "\t";        // letters A-J
            for (int j = 0; j < gridSize; j++) {
                symbol = " ";

                if (grid[i][j] == Cell.SHIP) {
                    symbol = "H";
                }
                if (grid[i][j] == Cell.HIT) {
                    symbol = "X";
                }
                if (grid[i][j] == Cell.MISS) {
                    symbol= "O";
                }

                result += symbol + " ";            // content of point
            }
            result += "\n";
        }

        return result;
    }

    public String printShots(){
        String result = "\t\t";
        String symbol;

        // add numbers
        for (int i = 1; i <= gridSize; i++) {
            result += (i + " ");
        }
        result += "\n";

        // adds rows
        for (int i = 0; i < gridSize; i++) {                      // rows i
            result += "\t" + (char) (65 + i) + "\t";        // letters A-J
            for (int j = 0; j < gridSize; j++) {
                symbol = " ";

                if (grid[i][j] == Cell.HIT) {
                    symbol = "X";
                }
                if (grid[i][j] == Cell.MISS) {
                    symbol= "O";
                }

                result += symbol + " ";            // content of point
            }
            result += "\n";
        }

        return result;
    }

    @Override
    public String toString() {

        // prints board
        String result = "\t\t";

        // add numbers
        for (int i = 1; i <= gridSize; i++) {
            result += (i + " ");
        }
        result += "\n";

        // adds rows
        for (int i = 0; i < gridSize; i++) {                      // rows i
            result += "\t" + (char) (65 + i) + "\t";        // letters A-J
            for (int j = 0; j < gridSize; j++) {                  // Columns j
                result += "" + grid[i][j] + " ";            // content of point
            }
            result += "\n";
        }

        return result;
    }
}

