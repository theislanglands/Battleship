package battleship.domain;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class BattleshipGame {

    public static Map<String, Integer> ships = new LinkedHashMap<>();
    public static int noOfShips;
    public static int gridSize;

    private int round = 0;
    public Player[] player = new Player[2];

    private int winner = -1;

    public BattleshipGame(int noOfShips, int gridSize) {
        setGridSize(gridSize);
        setNoOfShips(noOfShips);
        intilializeShips(noOfShips);
        System.out.println(ships);
        initializePlayers();
    }

    public static Point transformToPoint(String input) {
        // Omregner input ex b4 til et Point objekt
        int xPos, yPos;

        input = input.toUpperCase();
        xPos = -65 + (int) input.charAt(0);
        yPos = Integer.parseInt(input.substring(1,2));

        return new Point(xPos, yPos);
    }

    public static String transformToCoordinate(Point point) {
        String result;
        result = Character.toString(65 + point.x);
        result += point.y + 1;
        return result;
    }

    public void intilializeShips(int noOfShips) {
        // TODO do somehing about thies, an enum with ship names & length
        // SHIP length 1 could be named patrol!
        System.out.println("init ships noOfShips" + noOfShips);
        switch (noOfShips) {
            case 4:
                ships.put("Battleship", 4);
                ships.put("Cruiser", 3);
                ships.put("Destoyer", 2);
                ships.put("Submarine", 1);
                break;

            case 5:
                ships.put("Carrier", 5);
                ships.put("Battleship", 4);
                ships.put("Cruiser", 3);
                ships.put("Submarine", 3);
                ships.put("Destroyer", 2);
                break;

            case 7:
                ships.put("Carrier", 5);
                ships.put("Battleship", 4);
                ships.put("Cruiser", 3);
                ships.put("Destoyer 1", 2);
                ships.put("Destoyer 2", 2);
                ships.put("Submarine 1", 1);
                ships.put("Submarine 2", 1);
                break;

            case 10:
                ships.put("Battleship", 4);
                ships.put("Cruiser 1", 3);
                ships.put("Cruiser 2", 3);
                ships.put("Destoyer 1", 2);
                ships.put("Destoyer 2", 2);
                ships.put("Destoyer 3", 2);
                ships.put("Submarine 1", 1);
                ships.put("Submarine 2", 1);
                ships.put("Submarine 3", 1);
                ships.put("Submarine 4", 1);
                break;
        }
    }

    private void initializePlayers() {
        player[Player.PLAYER] = new Player(true);
        player[Player.COMPUTER] = new Player(false);
    }

    // GETTERS & SETTERS
    public int getNoOfShips() {
        return noOfShips;
    }

    public static void setNoOfShips(int noOfShips) {
        BattleshipGame.noOfShips = noOfShips;
    }

    public int getRound() {
        return round;
    }

    public void increaseRoundCount() {
        round++;
    }

    public Cell placeShot(int playerNr, Point shotPoint) {
        //shot at player playerNr board at a given point

        Cell shotValue = player[playerNr].getBoard().getValue(shotPoint);

        if (shotValue == Cell.EMPTY) {
            System.out.println("**  SPLASH!!  **");
            player[playerNr].getBoard().setValue(shotPoint, Cell.MISS);
        }

        // tjekker om der rammes
        if (shotValue == Cell.SHIP) {
            System.out.println("**  BANG  **");
            player[playerNr].getBoard().setValue(shotPoint, Cell.HIT);
        }

        return shotValue;
    }

    public int checkSunkenShip(int playerNr, Point shotPoint) {
        // check sunken ship
        int sunkenShip = player[playerNr].saveHit(shotPoint);

        // if ship is sunk
        if (sunkenShip > -1) {
            System.out.println("You have sunk your opponents " + player[playerNr].getShip()[sunkenShip].getName());
            player[playerNr].markAsSunk(sunkenShip);

            updateWinner();

            if (playerNr == Player.PLAYER) {
                // marks not to shot around a ship
                player[Player.PLAYER].aiMarkings(sunkenShip);
                player[Player.COMPUTER].setAiStatus(0);
            }
        }

        return sunkenShip;
    }

    public void updateWinner(){
        if (player[Player.PLAYER].allSunk()) winner = 1;
        if (player[Player.COMPUTER].allSunk()) winner = 0;
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

    public void setAiLevel(int aiLevel) {
        player[Player.COMPUTER].setAiLevel(aiLevel);
    }

}
