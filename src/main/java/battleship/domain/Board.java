package battleship.domain;

import java.awt.Point;

/**
 * @author TheisLanglands
 */

/*
Board for battleship

0 = empty
1 = ship
2 = ship hit
3 = missed shot
4 = plads rundt om skib hvor AI ikke skyder
 */


public class Board {

    private int gridSize;
    int[][] grid;

    // constants for fields in board status
    public final static int EMPTY = 0;
    public final static int SHIP = 1;
    public final static int HIT = 2;
    public final static int MISS = 3;
    public final static int AI = 4;

    public Board() {
        this.gridSize = 10;
        grid = new int[gridSize][gridSize];
    }

    public Board(int gridSize) {
        this.gridSize = gridSize;
        grid = new int[gridSize][gridSize];
    }

    public int getValue(Point p) {
        return grid[p.x][p.y];
    }

    public void setValue(Point p, int value) {
        grid[p.x][p.y] = value;
    }

    public void clearBoard(){
        this.grid = new int[gridSize][gridSize];
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
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

                if (grid[i][j] == SHIP) {
                    symbol = "H";
                }
                if (grid[i][j] == HIT) {
                    symbol = "X";
                }
                if (grid[i][j] == MISS) {
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

        // adds numbers
        for (int i = 1; i <= gridSize; i++) {
            result += (i + " ");
        }
        result += "\n";

        // adds rows
        for (int i = 0; i < gridSize; i++) {                      // rows i
            result += "\t" + (char) (65 + i) + "\t";        // letters A-J
            for (int j = 0; j < gridSize; j++) {
                symbol = " ";

                if (grid[i][j] == HIT) {
                    symbol = "X";
                }
                if (grid[i][j] == MISS) {
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

        // adds numbers
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

