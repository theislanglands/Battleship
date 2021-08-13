package battleship.presentation;

import battleship.domain.BattleshipGame;
import battleship.domain.Board;

import java.awt.*;
import java.util.Scanner;


// TODO hvisning af bræt, modstander - etc! ting der skal udelades

public class Cli {

    final BattleshipGame GAME = new BattleshipGame();
    Scanner keyboard = new Scanner(System.in);
    String input;
    int winner = -1;


    public Cli() {
    }

    public void startGame() {
        printWelcome();
        placeShips();
        winner = startRounds();
        endGame(winner);
    }

    private void printWelcome() {
        System.out.println("Welcome to battleships");

        // henter og initialiserer spiller navn
        System.out.print("\nWhat is your name? ");
        input = keyboard.nextLine();
        GAME.player[0].setName(input);
    }

    private void placeShips() {

        // Placerer computerens skibe tilfældigt
        GAME.player[1].randomShipPlacement();

        // Help text for placing ships
        System.out.println("\nhello " + GAME.player[0].getName() +"\n");
        System.out.println("Place your ships with position and v/h for vertical or horisontal\n "
                + "ex: b4h, or type R for random placement");

        // Placerer spillers skibe
        for (int i = 0; i < GAME.getNoOfShips(); i++) {

            boolean placedCorrect = false;

            while (!placedCorrect) {

                boolean validInput = false;

                while (!validInput) {

                    System.out.println(GAME.player[0].getGrid());

                    System.out.print("\nPlace your " + GAME.player[0].getShip()[i].getName() +
                            " with length " + GAME.player[0].getShip()[i].getLength() +
                            ": ");

                    input = keyboard.nextLine();
                    validInput = checkValidInput(input);
                    if (!validInput) System.out.println("Error, try again\n");
                }

                if (input.equals("R")) {
                    // Placerer spillerens skibe tilfældigt
                    System.out.println("Placing all ships randomly");
                    GAME.player[0].randomShipPlacement();
                    i = GAME.getNoOfShips();
                    break;
                }

                // sætter orientering på skib
                GAME.player[0].getShip()[i].setHorizontal(HorizontalInput(input));

                // Tjekker om skibet kan placeres
                if (GAME.player[0].getShip()[i].canPlace(transformToPoint(input), GAME.player[0].getGrid())) {
                    // placerer skib
                    GAME.player[0].getShip()[i].place(GAME.player[0].getGrid(), transformToPoint(input), GAME.player[0].getShip()[i].isHorizontal());
                    placedCorrect = true;
                }

                // beder om ny indtastning
                else {
                    System.out.print("\nYou can't place your ship there, try again!\n");
                }
            }
        }

        System.out.println(GAME.player[0].getGrid());
    }

    private int startRounds() {

        // HER STARTER RUNDER!
        int playerTurn;

        while (winner == -1) {
            GAME.increaseRoundCount();
            playerTurn = (GAME.getRound() - 1) % 2;
            int shotValue = 0;
            Point shotPoint = null;


            System.out.print("Round " + GAME.getRound() +
                            "\nPlayer; " + GAME.player[playerTurn].getName() + "'s turn\n\n" );

            // spillers tur
            if (playerTurn == 0) {

                // Placerer skud menneske (metode i spiller?
                System.out.print("place your shot? ");

                boolean correctShot = false;

                while (!correctShot) {

                    // henter input
                    do {
                        input = keyboard.nextLine() + "h"; // indsætter "h" for at bruge checkValidInput metode
                        if (!checkValidInput(input)) {
                            System.out.print("\nError, try again: ");
                        }
                    } while (!checkValidInput(input));

                    if (input.equals("Qh")) {
                        winner = 2; // means quit game
                        break;
                    }

                    // henter status på punktet på modstanders bræt hvor der skydes
                    shotPoint = transformToPoint(input);
                    shotValue = GAME.player[1].getGrid().getValue(shotPoint);

                    // tjekker at der ikke er skudt før på punktet
                    if (shotValue == Board.HIT || shotValue == Board.MISS) {
                        System.out.print("\nError: You allready hit this point, try again: ");
                    } else {
                        correctShot = true;
                    }
                }

                if (winner == 2) break;

                // Dette over i metoede der duer til både computer & human!

                // tjekker om der rammes forbi
                if (shotValue == Board.EMPTY) {
                    System.out.println("SPLASH!!");
                    GAME.player[1].getGrid().setValue(shotPoint, Board.MISS);
                }

                // tjekker om der rammes
                if (shotValue == Board.SHIP) {
                    System.out.println("BANG");
                    GAME.player[1].getGrid().setValue(shotPoint, Board.HIT);
                    int sunkenShip = GAME.player[1].saveHit(shotPoint);
                    if (sunkenShip > -1) {
                        System.out.println("You have sunk your opponents " + GAME.player[1].getShip()[sunkenShip].getName());
                    }
                }
            } // slut spillers tur

            // computer turn
            if (playerTurn == 1) {

                // ingen tidligere træf - skyder tilfældigt!

                    // Ai skud ved træf - to DO tjek det her kald!
                    shotPoint = GAME.player[1].aiShot(GAME.player[0].getGrid());
                    shotValue = GAME.player[0].getGrid().getValue(shotPoint);

                System.out.println("\nComputer Skyder på " + transformToCoordinate(shotPoint));

                if (shotValue == Board.EMPTY) {
                    System.out.println("SPLASH!");
                    GAME.player[0].getGrid().setValue(shotPoint, Board.MISS);
                }

                if (shotValue == Board.SHIP) {
                    System.out.println("BANG!");
                    GAME.player[0].getGrid().setValue(shotPoint, Board.HIT);
                    GAME.player[1].setLastHit(shotPoint);

                    // indstil aiStatus ved første hit, og gemmer punktet!
                    if (GAME.player[1].getAiStatus() == 0) {
                        GAME.player[1].setAiStatus(1);
                    }

                    int sunkenShip = GAME.player[0].saveHit(shotPoint);

                    if (sunkenShip > -1) {
                        System.out.println("You have sunk your opponents " + GAME.player[0].getShip()[sunkenShip].getName());
                        GAME.player[1].setAiStatus(0); // indstiller til random shot.
                    }
                }

            } // end computer turn

            // print boards
            System.out.println("\t     Player\n");
            System.out.println(GAME.player[0].getGrid());
            // System.out.println("\t     Computer\n");
            // System.out.println(GAME.player[1].getGrid());

            // checker om der er en vinder
            if ( GAME.player[1].allSunk() ) winner = 0;
            if ( GAME.player[0].allSunk() ) winner = 1;
        }

        return winner;
    }

    private void endGame(int winner) {
        // Spillet er slut

        if (winner == 2) {
            System.out.println("GAME ENDED - a draw!");
        } else {
            System.out.println(" ** GAME OVER ** ");
            System.out.print(GAME.player[winner].getName() + " is victorious");
            if (winner == 0) System.out.println("\n\ncongratulation!");
            if (winner == 1) System.out.println("\n\nbetter luck next time");
        }
    }

    private boolean HorizontalInput(String input) {
        return input.toUpperCase().endsWith("H");
    }

    public static Point transformToPoint(String input) {
        // Omregner input ex b4 til et Point objekt

        int xPos, yPos;

        // beregner x-Pos
        input = input.toUpperCase();
        xPos = -65 + (int) input.charAt(0);

        // beregner y-pos
        if (input.length() == 4) yPos = 9;
        else yPos = (int) -49 + input.charAt(1);

        return new Point (xPos, yPos);
    }

    public static String transformToCoordinate(Point point) {
        String result;
        result = Character.toString(65+point.x);
        result += point.y + 1;
        return result;
    }

    private boolean checkValidInput(String input) {

        // tjekker om inpur er "R"
        if (input.equals("R")) return true;

        // tjekker om input er Qh
        if (input.equals("Qh")) return true;

        // tjekker om input er for langt
        if (input.length() > 4) return false;

        // tjekker om input slutter med V eller H
        input = input.toUpperCase();
        if ( !(input.endsWith("V") || input.endsWith("H")) ) return false;

        // tjekker om input starter med A-J
        int firstChar = (int) input.charAt(0);
        if (65 > firstChar || firstChar > 74) return false;

        // tjekker om midten er tal mellem 1 og 9
        int midChar = (int) -48 + input.charAt(1);
        if (midChar < 1 || midChar > 9 ) return false;

        // tjekker om midten er 10
        if (input.length() == 4 && !input.substring(1,3).equals("10") ) return false;

        return true;
    }
}
