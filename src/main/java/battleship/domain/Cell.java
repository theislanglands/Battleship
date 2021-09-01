package battleship.domain;

public enum Cell {
    EMPTY (0),
    SHIP(1),
    HIT(2),
    MISS(3),
    AI(4),
    SUNK(5);

    int BoardValue;
    Cell(int i) {
        this.BoardValue = i;
    }
}
