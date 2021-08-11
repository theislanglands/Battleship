package battleship.domain;

import java.util.HashMap;
import java.util.Map;

public class BattleshipGame {

    public static Map<String, Integer> ships = new HashMap<>();
    public static int noOfShips;

    private int round = 0;
    public Player[] player = new Player[2];

    public BattleshipGame() {
        intilializeShips();
        initializePlayers();
    }

    private void intilializeShips(){
        ships.put("Carrier", 5);
        ships.put("Battleship", 4);
        ships.put("Cruiser", 3);
        ships.put("Submarine", 3);
        ships.put("Destoyer", 2);
        noOfShips = ships.size();
    }

    private void initializePlayers(){
        player[0] = new Player(true);
        player[1] = new Player(false);
    }

    // GETTERS & SETTERS
    public int getNoOfShips() {
        return noOfShips;
    }

    public void setNoOfShips(int noOfShips) {
        noOfShips = noOfShips;
    }

    public int getRound() {
        return round;
    }

    public void increaseRoundCount() {
        round++;
    }
}
