package battleship.presentation;

import battleship.domain.BattleshipGame;
import battleship.domain.Cell;
import battleship.domain.Player;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;

public class GamePlayController {

    final static int COMPUTER = 1;
    final static int PLAYER = 0;

    public Label headerText;
    public Label mainStatusLabel;
    public Label hitLabel;
    public Button quitBtn;
    public Button settingsBtn;
    public FlowPane enemyFleetFpn;

    int gridSize = App.game.getGridSize();

    // left grid
    @FXML
    TilePane leftGrid = new TilePane();
    int leftPicSize = 45 - gridSize;
    ImageView[][] leftContent = new ImageView[gridSize][gridSize];
    GraphicBoard leftBoard;

    @FXML
    TilePane rightGrid = new TilePane();
    int rightPicSize = 45 - gridSize - 10;
    ImageView[][] rightContent = new ImageView[gridSize][gridSize];
    GraphicBoard rightBoard;

    ImageCursor aimCursor = new ImageCursor(GraphicBoard.aimCursor, 16, 16);

    int playerTurn = 0;
    int recentSunkenShip = -1;
    public Point chosenPoint = null;
    Cell shotValue;


    public void initialize() {
        App.game.increaseRoundCount();

        // initializing grids
        leftBoard = new GraphicBoard(gridSize, leftPicSize, leftContent, leftGrid);
        leftBoard.hide(Cell.AI);
        leftBoard.hide(Cell.SHIP);
        leftBoard.setOwner(Player.COMPUTER);
        leftBoard.updateBoard();

        rightBoard = new GraphicBoard(gridSize, rightPicSize, rightContent, rightGrid);
        rightBoard.hide(Cell.AI);
        rightBoard.setOwner(Player.PLAYER);
        rightBoard.updateBoard();

        updateEnemyFleet();
    }

    private void updateEnemyFleet() {
        enemyFleetFpn.getChildren().clear();

        int noOfShips = App.game.getNoOfShips();
        TilePane[] shipTpn = new TilePane[noOfShips];
        int length;
        Cell shipTile;

        for (int enemyShip = 0; enemyShip < noOfShips; enemyShip++) {
            length = App.game.player[Player.COMPUTER].getShip()[enemyShip].getLength();
            shipTpn[enemyShip] = new TilePane();

            if (App.game.player[Player.COMPUTER].getShip()[enemyShip].isSunk()) {
                shipTile = Cell.SUNK;
            } else {
                shipTile = Cell.SHIP;
            }

            rightBoard.createShipInPane(shipTpn[enemyShip], length, shipTile, 20);

            enemyFleetFpn.getChildren().add(shipTpn[enemyShip]);
        }
    }

    @FXML
    public void clickOnLeftBoard(MouseEvent mouseEvent) {

        if (playerTurn == PLAYER) {
            // getting chosenPoint
            int col = (int) mouseEvent.getX() / leftPicSize - 1;
            int row = (int) mouseEvent.getY() / leftPicSize - 1;

            // checking for click on labels
            if (col >= 0 && row >= 0) {
                chosenPoint = new Point(row, col);
                mainStatusLabel.setText("You shoot at: " + BattleshipGame.transformToCoordinate(chosenPoint));
                nextTurn();
            }
        }
    }

    private void changePlayer() {

        // disables mouse on player board
        leftGrid.setMouseTransparent(true);

        // changing playerTurn
        playerTurn = (playerTurn + 1) % 2;

        // setting waiting time.
        int waitingTime = App.getGameSpeed();
        if (playerTurn == PLAYER) waitingTime = 0;
        if (recentSunkenShip != -1) waitingTime = 5;

        // creating a pause
        PauseTransition pause = new PauseTransition(Duration.seconds(waitingTime));

        // handler for pause finished
        pause.setOnFinished(event -> {

            // checker om der er en vinder
            if (App.game.getWinner() != -1) {
                endGame();
            } else {
                // changing player
                // resetting sunkenShip
                recentSunkenShip = -1;

                if (playerTurn == PLAYER) {
                    // enables shot on grid
                    leftGrid.setMouseTransparent(false);
                    App.game.increaseRoundCount();
                    headerText.setText("Battleships Round " + (App.game.getRound()));
                }

                if (playerTurn == COMPUTER) {
                    nextTurn();
                }
            }
        });

        pause.play();
    }

    private void nextTurn() {
        int opponentPlayerNr = (playerTurn + 1) % 2;

        if (playerTurn == COMPUTER) {
            chosenPoint = App.game.player[COMPUTER].aiShot(App.game.player[PLAYER].getBoard(), App.game.player[PLAYER].longestShipLength());
            // update label
            mainStatusLabel.setText("Computer shoots at: " + BattleshipGame.transformToCoordinate(chosenPoint));
        }

        // getting value at chosenPoint (from opponents board)
        shotValue = App.game.placeShot(opponentPlayerNr, chosenPoint);

        if (shotValue == Cell.EMPTY) {
            Sound.play(Sound.Type.SPLASH);
            hitLabel.setText("-- Miss --");
        }

        if (shotValue == Cell.SHIP) {
            Sound.play(Sound.Type.BANG);
            hitLabel.setText("-- Hit --");

            // check if ship is sunken
            recentSunkenShip = App.game.checkSunkenShip(opponentPlayerNr, chosenPoint);

            // if ship is sunken
            if (recentSunkenShip != -1) {
                Sound.play(Sound.Type.SUNK);

                if (playerTurn == PLAYER) {
                    mainStatusLabel.setText("You have sunk computers " + App.game.player[COMPUTER].getShip()[recentSunkenShip].getName());
                    updateEnemyFleet();
                }

                if (playerTurn == COMPUTER) {
                    mainStatusLabel.setText("Computer has sunk your " + App.game.player[PLAYER].getShip()[recentSunkenShip].getName());
                }
            }
        }

        if (shotValue == Cell.EMPTY || shotValue == Cell.SHIP) {

            leftBoard.updateBoard();
            rightBoard.updateBoard();

            changePlayer();

        } else {
            Sound.play(Sound.Type.ERROR);
            mainStatusLabel.setText("You've already shot at this point!");
        }
    }

    private void endGame() {
        System.out.println("Game Over");
        Sound.play(Sound.Type.NOTIFICATION);
        hitLabel.setVisible(false);

        if (App.game.getWinner() == PLAYER) {
            mainStatusLabel.setText("You have won");
        }
        if (App.game.getWinner() == COMPUTER) {
            mainStatusLabel.setText("You have lost");
        }

        showEndDialog();
    }

    private void showEndDialog() {
        // End dialog stage
        Stage endDialogStage = new Stage();
        endDialogStage.initModality(Modality.APPLICATION_MODAL);
        endDialogStage.initStyle(StageStyle.UNDECORATED);
        endDialogStage.setAlwaysOnTop(true);
        endDialogStage.setResizable(false);

        // End dialog scene
        Scene endDialogScene = null;
        FXMLLoader endGamePopUpLoader = new FXMLLoader(App.class.getResource("endGamePopUp.fxml"));
        try {
            endDialogScene = new Scene(endGamePopUpLoader.load(), 400, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // End dialog show
        endDialogStage.setScene(endDialogScene);
        endDialogStage.show();
    }

    @FXML
    public void changeToAimCursor(MouseEvent mouseEvent) {

        if (mouseEvent.getEventType().getName().equals("MOUSE_ENTERED")) {
            App.getScene().setCursor(aimCursor);
        }

        if (mouseEvent.getEventType().getName().equals("MOUSE_EXITED")) {
            App.getScene().setCursor(Cursor.DEFAULT);
        }
    }

    @FXML
    public void settingBtnHandler() {
        Sound.play(Sound.Type.ERROR);
        App.game.setWinner(1);
    }

    @FXML
    public void quitBtnHandler() {
        System.exit(0);
    }
}