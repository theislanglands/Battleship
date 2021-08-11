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

    int[][] grid = new int[10][10];

    // constants for fields in board status
    public final static int EMPTY = 0;
    public final static int SHIP = 1;
    public final static int HIT = 2;
    public final static int MISS = 3;
    public final static int AI = 4;

    public Board() {
    }

    public int getValue(Point p) {
        return grid[p.x][p.y];
    }

    public void setValue(Point p, int value) {
        grid[p.x][p.y] = value;
    }

    public void clearBoard(){
        this.grid = new int[10][10];
    }

    @Override
    public String toString() {

        // prints board
        String result = "\t\t";

        // adds 1-10
        for (int i = 1; i <= 10; i++) {
            result += (i + " ");
        }
        result += "\n";

        // adds rows
        for (int i = 0; i < 10; i++) {                      // rows i
            result += "\t" + (char) (65 + i) + "\t";        // letters A-J
            for (int j = 0; j < 10; j++) {                  // Columns j
                result += "" + grid[i][j] + " ";            // content of point
            }
            result += "\n";
        }

        return result;
    }
}

