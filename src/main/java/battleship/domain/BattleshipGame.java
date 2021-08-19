package battleship.domain;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BattleshipGame {

    public static Map<String, Integer> ships = new HashMap<>();
    public static int noOfShips;
    public static int gridSize = 10;

    private int round = 0;
    public Player[] player = new Player[2];

    private int winner = -1;

    public BattleshipGame() {
        intilializeShips();
        initializePlayers();
    }

    public static Point transformToPoint(String input) {
        // Omregner input ex b4 til et Point objekt

        int xPos, yPos;

        // beregner x-Pos
        input = input.toUpperCase();
        xPos = -65 + (int) input.charAt(0);

        /*
        // beregner y-pos
        if (input.length() == 4) yPos = 9; // ved 10!
        else yPos = (int) -49 + input.charAt(1);
        */

        yPos = Integer.parseInt(input.substring(1,2));

        return new Point(xPos, yPos);
    }

    public static String transformToCoordinate(Point point) {
        String result;
        result = Character.toString(65 + point.x);
        result += point.y + 1;
        return result;
    }

    private void intilializeShips() {
        ships.put("Carrier", 5);
        ships.put("Battleship", 4);
        ships.put("Cruiser", 3);
        ships.put("Submarine", 3);
        ships.put("Destoyer", 2);
        noOfShips = ships.size();
    }

    private void initializePlayers() {
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

    public int placeShot(int playerNr, Point shotPoint) {
        //shot at player playerNr board at a given point

        int shotValue = player[playerNr].getBoard().getValue(shotPoint);

        if (shotValue == Board.EMPTY) {
            System.out.println("**  SPLASH!!  **");
            player[playerNr].getBoard().setValue(shotPoint, Board.MISS);
        }

        // tjekker om der rammes
        if (shotValue == Board.SHIP) {
            System.out.println("**  BANG  **");
            player[playerNr].getBoard().setValue(shotPoint, Board.HIT);
        }
        return shotValue;
    }

    public int checkSunkenShip(int playerNr, Point shotPoint) {
        // check sunken ship
        int sunkenShip = player[playerNr].saveHit(shotPoint);

        // if ship is sunk
        if (sunkenShip > -1) {
            System.out.println("You have sunk your opponents " + player[playerNr].getShip()[sunkenShip].getName());
            updateWinner();

            if (playerNr == Player.PLAYER) {
                // fo marks not to shot around a ship
                player[Player.PLAYER].aiMarkingsWhenSunk(sunkenShip);
                System.out.println(player[Player.PLAYER].getBoard());
                player[Player.COMPUTER].setAiStatus(0);
            }
        }

        return sunkenShip;
    }

    public void updateWinner(){
        if (player[0].allSunk()) winner = 1;
        if (player[1].allSunk()) winner = 0;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        BattleshipGame.gridSize = gridSize;
    }
}
