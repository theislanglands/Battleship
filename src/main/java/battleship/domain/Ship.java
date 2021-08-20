package battleship.domain;

import java.awt.*;

public class Ship {

    // Attributes
    private Point position = new Point();    // pos (x,y) på grid;
    int length;                    // str. skib 2-5  (2,3,3,4,5)
    boolean horizontal;              // afgør om skibet er horisontalt / vertikalt
    int hits = 0;
    String name = "unnamed ship";

    // CONSTRUCTORS
    public Ship(int length) {
        this.length = length;
    }

    public Ship(int length, String name) {
        this.length = length;
        this.name = name;
        this.horizontal= true;
    }

    public Ship(int length, boolean horizontal, String name) {
        this.length = length;
        this.horizontal = horizontal;
        this.name = name;
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
                if (erHor) board.grid[position.x][position.y + i] = 1;
                if (!erHor) board.grid[position.x + i][position.y] = 1;
            }
        }
    }

    public boolean canPlace(Point position, Board board) {

        // tjekker for ugyldig x & y værdier
        if (position.x < 0 || position.x > board.getGridSize() - 1) return false;
        ;
        if (position.y < 0 || position.y > board.getGridSize() - 1) return false;
        ;

        // tjekker for længden af skibet går ud over spillebraet
        if (horizontal && position.y > board.getGridSize() - length) return false;
        ;
        if (!horizontal && position.x > board.getGridSize() - length) return false;
        ;

        // Tjekker om skibet ramler ind i andet skib (husk luft omkring +-1)

        if (horizontal) {         // tjekker horisontale skibe
            for (int i = 0; i < length; i++) {
                // tjekker om der er skib på sammepladeser
                if (board.grid[position.x][position.y + i] == 1) return false;

                // tjekker om der er skibe under
                if (position.x < board.getGridSize() - 1 &&
                        board.grid[position.x + 1][position.y + i] == 1) return false;

                // tjekker om der er skibe over
                if (position.x > 0 &&
                        board.grid[position.x - 1][position.y + i] == 1) return false;

                // tjekker om der er skibe tv
                if (position.y > 0 &&
                        board.grid[position.x][position.y - 1 + i] == 1) return false;

                // tjekker om der er skibe th
                if (position.y <= board.getGridSize() - 1 - length &&
                        board.grid[position.x][position.y + 1 + i] == 1) return false;
            }

            // TJEKKER  hjørner
            // tjekker venstre top - SAMME BÅDE HOR & VER
            if (position.y > 0 && position.x > 0 &&
                    board.grid[position.x - 1][position.y - 1] == 1) return false;

            // tjekker højre top
            if (position.y < board.getGridSize() - 1 - length && position.x > 0 &&
                    board.grid[position.x - 1][position.y + length] == 1) return false;

            // tjekker venstre bund
            if (position.x < board.getGridSize() - 1 && position.y > 0 &&
                    board.grid[position.x + 1][position.y - 1] == 1) return false;

            // tjekker højre bund
            if (position.y <= board.getGridSize() - 1 - length && position.x < board.getGridSize() - 1 &&
                    board.grid[position.x + 1][position.y + length] == 1) return false;
        }

        // tjekker vertikale skibe
        if (!horizontal) {
            for (int i = 0; i < length; i++) {
                // tjekker om der er skib på sammepladeser
                if (board.grid[position.x + i][position.y] == 1) return false;

                // tjekker om der er skibe under
                if (position.x <= board.getGridSize() - 1 - length &&
                        board.grid[position.x + 1 + i][position.y] == 1) return false;

                // tjekker om der er skibe over
                if (position.x > 0 &&
                        board.grid[position.x - 1 + i][position.y] == 1) return false;

                // tjekker om der er skibe tv
                if (position.y > 0 &&
                        board.grid[position.x + i][position.y - 1] == 1) return false;

                // tjekker om der er skibe th
                if (position.y < board.getGridSize() - 1 &&
                        board.grid[position.x + i][position.y + 1] == 1) return false;
            }

            // TJEKKER  hjørner
            // tjekker venstre top - SAMME BÅDE HOR & VER
            if (position.y > 0 && position.x > 0 &&
                    board.grid[position.x - 1][position.y - 1] == 1) return false;

            // tjekker højre top
            if (position.y < board.getGridSize() - 1 && position.x > 0 &&
                    board.grid[position.x - 1][position.y + 1] == 1) return false;

            // tjekker venstre bund
            if (position.y > 0 && position.x < board.getGridSize() - 1 - length &&
                    board.grid[position.x + length][position.y - 1] == 1) return false;

            // tjekker højre bund
            if (position.y < board.getGridSize() - 1 && position.x < board.getGridSize() - 1 - length &&
                    board.grid[position.x + length][position.y + 1] == 1) return false;
        }

        //System.out.println("skibet kan placeres ");
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

    public void setPosition(Point position) {
        this.position = position;
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void updateHitCount() {
        hits++;
    }

    public void flipOrientation(){
        if (horizontal){
            horizontal = false;
        } else {
            horizontal = true;
        }
    }

    @Override
    public String toString() {
        return "Name " + name + "\n" +
                "Length: " + length + "\n" +
                "Horizontal: " + horizontal + "\n" +
                "Position: (" + position.x + "," + position.y + ")\n";

    }
}
