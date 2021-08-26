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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;

public class SecondaryController {

    public Label headerText;
    public Label gameLabel;
    public Label leftStatusLabel;
    public Label computerShotPointLabel;
    public Label rightStatusLabel;
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


    final int COMPUTER = 1;
    final int PLAYER = 0;

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
        // setting size of board
        initializeBoardSize(leftPicSize, gridSize, leftGrid);
        initializeBoardSize(rightPicSize, gridSize, rightGrid);

        initializeLeftGrid();
        initializeRightGrid();

        // generating random ships for left
        updateRightBoard(App.game.player[PLAYER].getBoard());
        updateLeftBoard(App.game.player[COMPUTER].getBoard());

        App.game.increaseRoundCount();

        endDialogStage.initModality(Modality.WINDOW_MODAL);
        endDialogStage.setAlwaysOnTop(true);
        endDialogStage.setResizable(false);
        endDialogStage.initStyle(StageStyle.UNDECORATED);


    }

    public void quitBtnHandler(ActionEvent actionEvent) {
        System.exit(0);
    }

    private void initializeBoardSize(int picSize, int gridSize, TilePane tilePane) {
        tilePane.setPrefColumns(gridSize + 1);
        tilePane.setPrefRows(gridSize + 1);
        tilePane.setMaxWidth(picSize * (gridSize + 1));
        tilePane.setMinWidth(picSize * (gridSize + 1));
    }

    private void initializeLeftGrid() {
        // Drawing corner
        ImageView corner = new ImageView(App.cellImage[Board.SHIP]);
        corner.setFitHeight(leftPicSize);
        corner.setFitWidth(leftPicSize);
        leftGrid.getChildren().add(corner);

        // Drawing numbers
        for (int i = 1; i < gridSize + 1; i++) {
            StackPane numbers = new StackPane();
            ImageView addEmpty = new ImageView(App.cellImage[Board.EMPTY]);
            addEmpty.setFitHeight(leftPicSize);
            addEmpty.setFitWidth(leftPicSize);

            String number = "" + i;
            numbers.getChildren().add(addEmpty);
            numbers.getChildren().add(new Label(number));
            leftGrid.getChildren().add(numbers);
        }

        // Drawing Rows
        for (int row = 0; row < gridSize; row++) {

            // adding letter
            StackPane numbers = new StackPane();
            ImageView addEmpty = new ImageView(App.cellImage[Board.EMPTY]);
            addEmpty.setFitHeight(leftPicSize);
            addEmpty.setFitWidth(leftPicSize);

            String letter = "" + (char) (65 + row);
            numbers.getChildren().add(addEmpty);
            numbers.getChildren().add(new Label(letter));
            leftGrid.getChildren().add(numbers);

            // adding empty fields
            for (int column = 0; column < gridSize; column++) {
                leftContent[row][column] = new ImageView(App.cellImage[Board.EMPTY]);
                leftContent[row][column].setFitHeight(leftPicSize);
                leftContent[row][column].setFitWidth(leftPicSize);
                leftGrid.getChildren().add(leftContent[row][column]);
            }
        }
    }

    private void initializeRightGrid() {
        // Drawing corner
        ImageView corner = new ImageView(App.cellImage[Board.SHIP]);
        corner.setFitHeight(rightPicSize);
        corner.setFitWidth(rightPicSize);
        rightGrid.getChildren().add(corner);

        // Drawing numbers
        for (int i = 1; i < gridSize + 1; i++) {
            StackPane numbers = new StackPane();
            ImageView addEmpty = new ImageView(App.cellImage[Board.EMPTY]);
            addEmpty.setFitHeight(rightPicSize);
            addEmpty.setFitWidth(rightPicSize);

            String number = "" + i;
            numbers.getChildren().add(addEmpty);
            numbers.getChildren().add(new Label(number));
            rightGrid.getChildren().add(numbers);
        }

        // Drawing Rows
        for (int row = 0; row < gridSize; row++) {

            // adding letter
            StackPane numbers = new StackPane();
            ImageView addEmpty = new ImageView(App.cellImage[Board.EMPTY]);
            addEmpty.setFitHeight(rightPicSize);
            addEmpty.setFitWidth(rightPicSize);

            String letter = "" + (char) (65 + row);
            numbers.getChildren().add(addEmpty);
            numbers.getChildren().add(new Label(letter));
            rightGrid.getChildren().add(numbers);

            // adding empty fields
            for (int column = 0; column < gridSize; column++) {
                rightContent[row][column] = new ImageView(App.cellImage[Board.EMPTY]);
                rightContent[row][column].setFitHeight(rightPicSize);
                rightContent[row][column].setFitWidth(rightPicSize);
                rightGrid.getChildren().add(rightContent[row][column]);
            }
        }
    }

    private void updateLeftBoard(Board board) {
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {

                int valueAtPoint = board.getValue(new Point(row, column));

                if (valueAtPoint == Board.EMPTY
                        || valueAtPoint == Board.MISS
                        || valueAtPoint == Board.HIT) {
                    leftContent[row][column].setImage(App.cellImage[valueAtPoint]);
                }
            }
        }
    }

    private void updateRightBoard(Board board) {
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {

                int valueAtPoint = board.getValue(new Point(row, column));

                if (valueAtPoint == Board.EMPTY
                        || valueAtPoint == Board.SHIP
                        || valueAtPoint == Board.MISS
                        || valueAtPoint == Board.HIT) {
                    rightContent[row][column].setImage(App.cellImage[valueAtPoint]);
                }
            }
        }
    }

    @FXML
    public void clickOnBoard(MouseEvent mouseEvent) {

        if (playerTurn == PLAYER) {
            // getting chosenPoint
            int col = (int) mouseEvent.getX() / leftPicSize - 1;
            int row = (int) mouseEvent.getY() / leftPicSize - 1;
            chosenPoint = new Point(row, col);
            System.out.println(chosenPoint);
            mainStatusLabel.setText("");
            mainStatusLabel.setText("You shoot at: " + BattleshipGame.transformToCoordinate(chosenPoint));
            playerTurn();
        }
    }

    public void updateLabel(Label label, int shotValue) {
        // updating label according to shot value
        switch (shotValue) {
            case Board.EMPTY:
                label.setText("-- Miss --");
                hitLabel.setText(label.getText());
                break;
            case Board.SHIP:
                label.setText("-- Hit --");
                hitLabel.setText(label.getText());
                break;
        }
    }

    public void changePlayer() {

        int waitingTime;

        leftGrid.setMouseTransparent(true);

        if (playerTurn == COMPUTER) {
            waitingTime = 0;
        } else {
            waitingTime = App.getGameSpeed();
        }

        // if a ship is sunk - longer waitingtime
        if (sunkenShip != -1) waitingTime = 5;

        // make delay - // shorter with computer

        // from computer to player - no waiting time
        System.out.println("waiting time " + waitingTime);
        PauseTransition pause = new PauseTransition(Duration.seconds(waitingTime));
        pause.setOnFinished(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                // waiting over!


                // checker om der er en vinder
                if (App.game.getWinner() != -1) {
                    leftGrid.setMouseTransparent(true);
                    Scene scene = leftGrid.getScene();
                    quitBtn.setDisable(true);


                    endGame();
                }

                if (App.game.getWinner() == -1) {
                    // resetting sunkenShip
                    sunkenShip = -1;
                    if (playerTurn == COMPUTER) {
                        playerTurn = PLAYER;
                        leftGrid.setMouseTransparent(false);
                        App.game.increaseRoundCount();
                        headerText.setText("Battleships Round " + (App.game.getRound()));

                    } else {
                        playerTurn = Player.COMPUTER;
                        computerTurn();
                    }
                }
            }
        });

        pause.play();


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

    public void playerTurn() {
        // placing shot
        shotValue = App.game.placeShot(COMPUTER, chosenPoint);

        if (shotValue == Board.EMPTY || shotValue == Board.SHIP) {
            // update label
            updateLabel(leftStatusLabel, shotValue);

            if (shotValue == Board.EMPTY) {
                Sounds.play(Sounds.SPLASH);
            }
            // check if sunken
            if (shotValue == Board.SHIP) {
                Sounds.play(Board.SHIP);
                sunkenShip = App.game.checkSunkenShip(COMPUTER, chosenPoint);
                if (sunkenShip != -1) {
                    Sounds.play(Sounds.SUNK);
                    mainStatusLabel.setText("You have sunk computers " + App.game.player[1].getShip()[sunkenShip].getName());
                }
            }

            updateLeftBoard(App.game.player[1].getBoard());
            changePlayer();
        } else {
            Sounds.play(Sounds.ERROR);
            mainStatusLabel.setText("You've allready shot at this point!");
        }
    }

    public void computerTurn() {
        // Computer's turn
        chosenPoint = App.game.player[COMPUTER].aiShot(App.game.player[PLAYER].getBoard(), App.game.player[PLAYER].longestShipLength());

        mainStatusLabel.setText("Computer shoots at: " + BattleshipGame.transformToCoordinate(chosenPoint));

        shotValue = App.game.placeShot(PLAYER, chosenPoint);
        updateLabel(rightStatusLabel, shotValue);

        if (shotValue == Board.EMPTY) {
            Sounds.play(Board.EMPTY);
        }

        if (shotValue == Board.SHIP) {
            Sounds.play(Board.SHIP);
            sunkenShip = App.game.checkSunkenShip(PLAYER, chosenPoint);
            if (sunkenShip != -1) {
                mainStatusLabel.setText("Computer have sunk your " + App.game.player[PLAYER].getShip()[sunkenShip].getName());
                Sounds.play(Sounds.SUNK);
                App.game.player[COMPUTER].setAiStatus(0);
            }
        }

        updateRightBoard(App.game.player[PLAYER].getBoard());
        changePlayer();
    }

    @FXML
    public void rollOver(MouseEvent mouseEvent) {
        // System.out.println(mouseEvent.getEventType().getName());

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
    }
}

// TODO: skriv sammen left & right grid med boolean left/right som argument
