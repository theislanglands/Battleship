package battleship.domain;

import java.awt.*;

public class Ship {

    // Attributes
    private Point position = new Point();    // pos (x,y) på grid;
    int length;                    // str. skib 2-5  (2,3,3,4,5)
    boolean horizontal;              // afgør om skibet er horisontalt / vertikalt
    int hits = 0;
    String name;

    // CONSTRUCTORS
    public Ship(int length, String name) {
        this.length = length;
        this.name = name;
        this.horizontal= true;
    }

    // METHODS
    public void place(Board board, Point position) {
        place(board, position, horizontal);
    }

    public void place(Board board, Point position, boolean erHor) {
        this.horizontal = erHor;
        if (canPlace(position, board)) {
            this.position = position;
            for (int i = 0; i < length; i++) {
                if (erHor) board.grid[position.x][position.y + i] = Cell.SHIP;
                if (!erHor) board.grid[position.x + i][position.y] = Cell.SHIP;
            }
        }
    }



    public boolean canPlace(Point position, Board board) {

        // tjekker for ugyldig x & y værdier
        if (position.x < 0 || position.x > board.getGridSize() - 1) return false;
        if (position.y < 0 || position.y > board.getGridSize() - 1) return false;


        // tjekker for længden af skibet går ud over spillebræt
        if (horizontal && position.y > board.getGridSize() - length) return false;
        if (!horizontal && position.x > board.getGridSize() - length) return false;

        // Tjekker om skibet ramler ind i andet skib (husk luft omkring +-1)
        if (horizontal) {         // tjekker horisontale skibe
            for (int i = 0; i < length; i++) {
                // tjekker om der er skib på samme pladser
                if (board.grid[position.x][position.y + i] == Cell.SHIP) return false;

                // tjekker om der er skibe under
                if (position.x < board.getGridSize() - 1 &&
                        board.grid[position.x + 1][position.y + i] == Cell.SHIP) return false;

                // tjekker om der er skibe over
                if (position.x > 0 &&
                        board.grid[position.x - 1][position.y + i] == Cell.SHIP) return false;

                // tjekker om der er skibe tv
                if (position.y > 0 &&
                        board.grid[position.x][position.y - 1 + i] == Cell.SHIP) return false;

                // tjekker om der er skibe th
                if (position.y <= board.getGridSize() - 1 - length &&
                        board.grid[position.x][position.y + 1 + i] == Cell.SHIP) return false;
            }

            // TJEKKER  hjørner
            // tjekker venstre top - SAMME BÅDE HOR & VER
            if (position.y > 0 && position.x > 0 &&
                    board.grid[position.x - 1][position.y - 1] == Cell.SHIP) return false;

            // tjekker højre top
            if (position.y < board.getGridSize() - 1 - length && position.x > 0 &&
                    board.grid[position.x - 1][position.y + length] == Cell.SHIP) return false;

            // tjekker venstre bund
            if (position.x < board.getGridSize() - 1 && position.y > 0 &&
                    board.grid[position.x + 1][position.y - 1] == Cell.SHIP) return false;

            // tjekker højre bund
            if (position.y <= board.getGridSize() - 1 - length && position.x < board.getGridSize() - 1 &&
                    board.grid[position.x + 1][position.y + length] == Cell.SHIP) return false;
        }

        // tjekker vertikale skibe
        if (!horizontal) {
            for (int i = 0; i < length; i++) {
                // tjekker om der er skib på samme pladser
                if (board.grid[position.x + i][position.y] == Cell.SHIP) return false;

                // tjekker om der er skibe under
                if (position.x <= board.getGridSize() - 1 - length &&
                        board.grid[position.x + 1 + i][position.y] == Cell.SHIP) return false;

                // tjekker om der er skibe over
                if (position.x > 0 &&
                        board.grid[position.x - 1 + i][position.y] == Cell.SHIP) return false;

                // tjekker om der er skibe tv
                if (position.y > 0 &&
                        board.grid[position.x + i][position.y - 1] == Cell.SHIP) return false;

                // tjekker om der er skibe th
                if (position.y < board.getGridSize() - 1 &&
                        board.grid[position.x + i][position.y + 1] == Cell.SHIP) return false;
            }

            // TJEKKER  hjørner
            // tjekker venstre top - SAMME BÅDE HOR & VER
            if (position.y > 0 && position.x > 0 &&
                    board.grid[position.x - 1][position.y - 1] == Cell.SHIP) return false;

            // tjekker højre top
            if (position.y < board.getGridSize() - 1 && position.x > 0 &&
                    board.grid[position.x - 1][position.y + 1] == Cell.SHIP) return false;

            // tjekker venstre bund
            if (position.y > 0 && position.x < board.getGridSize() - 1 - length &&
                    board.grid[position.x + length][position.y - 1] == Cell.SHIP) return false;

            // tjekker højre bund
            if (position.y < board.getGridSize() - 1 && position.x < board.getGridSize() - 1 - length &&
                    board.grid[position.x + length][position.y + 1] == Cell.SHIP) return false;
        }

        return true;
    }

    public boolean isSunk() { // afgør om skibet er sunket
        return (hits == length);
    }

    public boolean updateHitCount(Point point) {
        // opdaterer træf tæller hvis punktet tilhører skib. returnerer true hvis skibet synkes
        for (int i = 0; i < length; i++) {

            // checker horisontalt
            if (horizontal && point.equals(new Point(position.x, position.y + i))) {
                hits++;
                if (hits == length) return true;
            }

            // checker vertikalt
            if (!horizontal && point.equals(new Point(position.x + i, position.y))) {
                hits++;
                if (hits == length) return true;
            }
        }
        return false;
    }

    // getter & setters
    public Point getPosition() {
        return position;
    }

    public void setHorizontal(boolean isHorizontal) {
        this.horizontal = isHorizontal;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public void flipOrientation(){
        horizontal = !horizontal;
    }

    @Override
    public String toString() {
        return "Name " + name + "\n" +
                "Length: " + length + "\n" +
                "Horizontal: " + horizontal + "\n" +
                "Position: (" + position.x + "," + position.y + ")\n";

    }
}
