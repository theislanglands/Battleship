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

        // Help text for placing ships
        System.out.println("\nhello " + GAME.player[0].getName() + "\n");
        System.out.println("Place your ships with position and v/h for vertical or horisontal\n "
                + "ex: b4h, or type R for random placement");

        // Placerer spillers skibe
        for (int i = 0; i < GAME.getNoOfShips(); i++) {

            boolean placedCorrect = false;

            while (!placedCorrect) {

                boolean validInput = false;

                while (!validInput) {

                    System.out.println(GAME.player[0].getBoard().printShipsAndHits());

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
                if (GAME.player[0].getShip()[i].canPlace(BattleshipGame.transformToPoint(input), GAME.player[0].getBoard())) {
                    // placerer skib
                    GAME.player[0].getShip()[i].place(GAME.player[0].getBoard(), BattleshipGame.transformToPoint(input), GAME.player[0].getShip()[i].isHorizontal());
                    placedCorrect = true;
                }

                // beder om ny indtastning
                else {
                    System.out.print("\nYou can't place your ship there, try again!\n");
                }
            }
        }

        System.out.println(GAME.player[0].getBoard().printShipsAndHits());
    }

    private int startRounds() {

        // HER STARTER RUNDER!
        int playerTurn;

        while (winner == -1) {
            GAME.increaseRoundCount();
            playerTurn = (GAME.getRound() - 1) % 2;
            int shotValue = 0;
            Point shotPoint = null;


            System.out.println("Round " + GAME.getRound() + " "
                    + GAME.player[playerTurn].getName() + "'s turn");

            // spillers tur
            if (playerTurn == 0) {

                // Placerer skud menneske (metode i spiller?
                System.out.println("Your Shots");
                System.out.println(GAME.player[1].getBoard().printShots());
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

                    if (winner == 2) break;

                    // henter status på punktet på modstanders bræt hvor der skydes
                    shotPoint = BattleshipGame.transformToPoint(input);

                    //shotValue = GAME.player[1].getGrid().getValue(shotPoint);
                    shotValue = GAME.placeShot(1, shotPoint);

                    // tjekker at der ikke er skudt før på punktet
                    if (shotValue == Board.HIT || shotValue == Board.MISS) {
                        System.out.print("\nError: You allready hit this point, try again: ");
                    } else {
                        correctShot = true;
                    }

                    // tjekker om der rammes forbi
                    if (shotValue == Board.EMPTY) {
                        System.out.println("**  SPLASH!!  **");
                    }

                    // tjekker om der rammes
                    if (shotValue == Board.SHIP) {
                        System.out.println("**  BANG  **");

                        // check sunken ship
                        int sunkenShip = GAME.player[1].saveHit(shotPoint);
                        if (sunkenShip > -1) {
                            System.out.println("You have sunk your opponents " + GAME.player[1].getShip()[sunkenShip].getName());
                        }
                    }
                }
            } // slut spillers tur


            // computer turn
            if (playerTurn == 1) {

                shotPoint = GAME.player[1].aiShot(GAME.player[0].getBoard());

                System.out.println("Computer shoots at: " + BattleshipGame.transformToCoordinate(shotPoint));

                shotValue = GAME.player[0].getBoard().getValue(shotPoint);

                if (shotValue == Board.EMPTY) {
                    System.out.println("** SPLASH!  **");
                    GAME.player[0].getBoard().setValue(shotPoint, Board.MISS);
                }

                if (shotValue == Board.SHIP) {
                    System.out.println("** BANG!  **");
                    GAME.player[0].getBoard().setValue(shotPoint, Board.HIT);
                    GAME.player[1].setLastHit(shotPoint);

                    // indstil aiStatus ved første hit, og gemmer punktet!
                    if (GAME.player[1].getAiStatus() == 0) {
                        GAME.player[1].setAiStatus(1);
                    }

                    int sunkenShip = GAME.player[0].saveHit(shotPoint);

                    if (sunkenShip > -1) {
                        System.out.println("You have sunk your opponents " + GAME.player[0].getShip()[sunkenShip].getName());
                        // indstiller til random shot.
                        GAME.player[1].setAiStatus(0);
                        // skal indstille AI rund om skibet til 4!, hvis de ikke allerede er 3!
                        GAME.player[0].aiMarkingsWhenSunk(sunkenShip);
                    }
                }

                System.out.println("Your ships");
                System.out.println(GAME.player[0].getBoard().printShipsAndHits());


            } // end computer turn

            // checker om der er en vinder
            if (GAME.player[1].allSunk()) winner = 0;
            if (GAME.player[0].allSunk()) winner = 1;
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

    private boolean checkValidInput(String input) {

        // tjekker om input er "R"
        if (input.equals("R")) return true;

        // tjekker om input er Qh
        if (input.equals("Qh")) return true;

        // tjekker om input er for langt
        if (input.length() > 4) return false;

        // tjekker om input slutter med V eller H
        input = input.toUpperCase();
        if (!(input.endsWith("V") || input.endsWith("H"))) return false;

        // tjekker om input starter med A-J
        int firstChar = input.charAt(0);
        if (65 > firstChar || firstChar > 74) return false;

        // tjekker om midten er tal mellem 1 og 9
        int midChar = -48 + input.charAt(1);
        if (midChar < 1 || midChar > 9) return false;

        // tjekker om midten er 10
        return input.length() != 4 || input.startsWith("10", 1);
    }
}
