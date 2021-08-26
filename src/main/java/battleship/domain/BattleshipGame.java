package battleship.domain;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class BattleshipGame {

    public static Map<String, Integer> ships = new LinkedHashMap<>();
    public static int noOfShips;
    public static int gridSize;
    private int aiLevel = 1;

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

    public void intilializeShips(int noOfShips) {
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
        player[0] = new Player(true);
        player[1] = new Player(false);
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
/*
        if (playerNr == 1) {
            int x = shotPoint.x;
            int y = shotPoint.y;

            shotPoint = new Point(x,y);
            System.out.println("UP " + player[1].checkNeighbour(player[1].getBoard(), shotPoint, 3,-1,0));

            shotPoint = new Point(x,y);
            System.out.println("DOWN " + player[1].checkNeighbour(player[1].getBoard(), shotPoint, 3,1,0));

            shotPoint = new Point(x,y);
            System.out.println("LEFT " + player[1].checkNeighbour(player[1].getBoard(), shotPoint, 3,0,-1));

            shotPoint = new Point(x,y);
            System.out.println("RIGHT " + player[1].checkNeighbour(player[1].getBoard(), shotPoint, 3,0,1));
        }

 */
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

    public int getAiLevel() {
        return aiLevel;
    }

    public void setAiLevel(int aiLevel) {
        aiLevel = aiLevel;
        player[Player.COMPUTER].setAiLevel(aiLevel);
    }

}
