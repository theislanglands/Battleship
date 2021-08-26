package battleship.presentation;

import battleship.domain.BattleshipGame;
import battleship.domain.Board;
import battleship.domain.Player;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;

public class GamePlayController {

    public Label headerText;
    public Label mainStatusLabel;
    public Label hitLabel;

    public Button quitBtn;
    public Button saveBtn;
    public Button settingsBtn;

    // pop up
    public final Stage endDialogStage = new Stage();
    Scene popUpScene = null;
    public Button yesBtn;
    public Button noBtn;

    int gridSize = App.game.getGridSize();
    int leftPicSize = 45 - gridSize;
    int rightPicSize = 45 - gridSize - 5;
    public Point chosenPoint = null;

    ImageView[][] leftContent = new ImageView[gridSize][gridSize];
    ImageView[][] rightContent = new ImageView[gridSize][gridSize];

    GraphicBoard leftBoard, rightBoard;

    final int COMPUTER = 1;
    final int PLAYER = 0;

    // TODO change this according to pic size!
    ImageCursor ship_cursor = new ImageCursor(App.cursor, 16, 16);

    int playerTurn = 0;

    // holds the ship_nr of recent sunk ship, -1 if none
    int sunkenShip = -1;
    int shotValue;

    @FXML
    TilePane rightGrid = new TilePane();

    @FXML
    TilePane leftGrid = new TilePane();


    @FXML
    public void initialize() {

        // initializing grids
        leftBoard = new GraphicBoard(gridSize,leftPicSize,leftContent,leftGrid);
        leftBoard.hide(Board.AI);
        leftBoard.hide(Board.SHIP);
        leftBoard.setOwner(Player.COMPUTER);
        leftBoard.updateBoard();

        rightBoard = new GraphicBoard(gridSize, rightPicSize, rightContent,rightGrid);
        rightBoard.hide(Board.AI);
        rightBoard.setOwner(Player.PLAYER);
        rightBoard.updateBoard();

        // starting the roundCount (to 1!)
        App.game.increaseRoundCount();

        // TODO do something about  this!
        endDialogStage.initModality(Modality.WINDOW_MODAL);
        endDialogStage.initStyle(StageStyle.UNDECORATED);
        endDialogStage.setAlwaysOnTop(true);
        endDialogStage.setResizable(false);
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
        if (sunkenShip != -1) waitingTime = 5;

        // creating a pause
        PauseTransition pause = new PauseTransition(Duration.seconds(waitingTime));

        // handler for pause finished
        pause.setOnFinished(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {

                // checker om der er en vinder
                if (App.game.getWinner() != -1) {
                    endGame();
                } else {

                    // changing player

                    // resetting sunkenShip
                    sunkenShip = -1;

                    if (playerTurn == PLAYER) {
                        // enables shot on grid
                        leftGrid.setMouseTransparent(false);
                        // increases round counter
                        App.game.increaseRoundCount();
                        headerText.setText("Battleships Round " + (App.game.getRound()));
                    }

                    if (playerTurn == COMPUTER) {
                        nextTurn();
                    }
                }
            }
        });

        pause.play();
    }

    private void nextTurn(){
        int opponentPlayerNr = (playerTurn + 1) % 2;

        if (playerTurn == COMPUTER){
            chosenPoint = App.game.player[COMPUTER].aiShot(App.game.player[PLAYER].getBoard(), App.game.player[PLAYER].longestShipLength());
            // update label
            mainStatusLabel.setText("Computer shoots at: " + BattleshipGame.transformToCoordinate(chosenPoint));
        }

        // getting value at chosenPoint (from opponents board)
        shotValue = App.game.placeShot(opponentPlayerNr, chosenPoint);

        if (shotValue == Board.EMPTY) {
            Sounds.play(Sounds.SPLASH);
            hitLabel.setText("-- Miss --");
        }

        if (shotValue == Board.SHIP) {
            Sounds.play(Board.SHIP);
            hitLabel.setText("-- Hit --");

            // check if ship is sunken
            sunkenShip = App.game.checkSunkenShip(opponentPlayerNr, chosenPoint);

            // if ship is sunken
            if (sunkenShip != -1) {
                Sounds.play(Sounds.SUNK);

                if (playerTurn == PLAYER) {
                    mainStatusLabel.setText("You have sunk computers " + App.game.player[COMPUTER].getShip()[sunkenShip].getName());
                }

                if (playerTurn == COMPUTER) {
                    mainStatusLabel.setText("Computer has sunk your " + App.game.player[PLAYER].getShip()[sunkenShip].getName());
                }
            }
        }

        if (shotValue == Board.EMPTY || shotValue == Board.SHIP) {

            leftBoard.updateBoard();
            rightBoard.updateBoard();

            changePlayer();

        } else {
            Sounds.play(Sounds.ERROR);
            mainStatusLabel.setText("You've allready shot at this point!");
        }
    }

    private void endGame() {
        System.out.println("Game Over");
        if (App.game.getWinner() == PLAYER) {
            Sounds.play(Sounds.NOTIFICATION);
            mainStatusLabel.setText("You have won");
            hitLabel.setText("");
        }
        if (App.game.getWinner() == COMPUTER) {
            Sounds.play(Sounds.NOTIFICATION);
            mainStatusLabel.setText("You have lost");
            hitLabel.setText("");
        }

        leftGrid.setMouseTransparent(true);
        saveBtn.setDisable(true);
        quitBtn.setDisable(true);
        settingsBtn.setDisable(true);


        playAgainPopUp();
    }

    private void playAgainPopUp() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("endGamePopUp.fxml"));

        try {
            popUpScene = new Scene(fxmlLoader.load(), 400, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }

        endDialogStage.setTitle("GAME OVER");
        endDialogStage.setScene(popUpScene);
        endDialogStage.show();


    }

    public void endDialogHandler(ActionEvent actionEvent) {

        Sounds.play(Sounds.CLICK);

        if (actionEvent.getSource().equals(noBtn)) {
            System.out.println("no btn");
            // Platform.exit();
            System.exit(0);
        }

        if (actionEvent.getSource().equals(yesBtn)) {

            // closing popup - stage!
            Stage stage = (Stage) yesBtn.getScene().getWindow();
            stage.close();

            // starting new game!
            try {
                App.setRoot("Battleship_settings");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void changeToAimCursor(MouseEvent mouseEvent) {

        if (mouseEvent.getEventType().getName().equals("MOUSE_ENTERED")) {
            App.getScene().setCursor(ship_cursor);
        }

        if (mouseEvent.getEventType().getName().equals("MOUSE_EXITED")) {
            App.getScene().setCursor(Cursor.DEFAULT);
        }
    }

    @FXML
    public void saveBtnHandler(ActionEvent event) {
        Sounds.play(Sounds.ERROR);
    }

    @FXML
    public void settingBtnHandler(ActionEvent event) {
        Sounds.play(Sounds.ERROR);
        App.game.setWinner(1);
    }

    @FXML
    public void quitBtnHandler(ActionEvent actionEvent) {
        System.exit(0);
    }
}

// TODO: refacter EndGamePopUp to own class
// TODO: display enemy ships remaining
// TODO: when a ship is sunk, make it red - new picture (Board.SUNK)