package battleship.presentation;

import battleship.domain.BattleshipGame;
import battleship.domain.Board;
import battleship.domain.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

import java.awt.*;
import java.io.IOException;

public class SecondaryController {

    public Label gameLabel;
    public Label leftStatusLabel;
    public Label computerShotPointLabel;
    public Label rightStatusLabel;
    public Button quitBtn;
    public Label mainStatusLabel;


    int gridSize = App.game.getGridSize();
    int leftPicSize = 35;
    int rightPicSize = 35;
    public Point chosenPoint = null;

    public static Image[] cellImage = new Image[10];
    ImageView[][] leftContent = new ImageView[gridSize][gridSize];
    ImageView[][] rightContent = new ImageView[gridSize][gridSize];


    final int COMPUTER = 1;
    final int PLAYER = 0;

    ImageCursor ship_cursor = new ImageCursor(App.cursor,16,16);


    int playerTurn = 0;
    int sunkenShip = -1;
    int shotValue;


    @FXML
    TilePane rightGrid = new TilePane();

    @FXML
    TilePane leftGrid = new TilePane();


    @FXML
    public void initialize() {

        // loading images

        // setting size of board
        initializeBoardSize(leftPicSize, gridSize, leftGrid);
        initializeBoardSize(rightPicSize, gridSize, rightGrid);

        initializeLeftGrid();
        initializeRightGrid();

        // generating random ships for left
        updateRightBoard(App.game.player[PLAYER].getBoard());
        updateLeftBoard(App.game.player[COMPUTER].getBoard());
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
    private void switchToPrimary() throws IOException {
        App.setRoot("Primary");
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
            gameLabel.setText("You shoot at: " + BattleshipGame.transformToCoordinate(chosenPoint));
            playerTurn();
        }
    }

    public void updateLabel (Label label, int shotValue) {
        // updating label according to shot value
        switch(shotValue) {
            case Board.EMPTY:
                label.setText("-- Miss --");
                break;
            case Board.SHIP:
                label.setText("-- Hit --");
                break;
        }
    }

    public void changePlayer(){

        // checker om der er en vinder
        if (App.game.getWinner() != -1) {
            endGame();
        }

        if (playerTurn == COMPUTER) {
            playerTurn = PLAYER;
            System.out.println("Player turn");

        } else {
            playerTurn = Player.COMPUTER;
            computerTurn();
        }
    }

    private void endGame() {
        System.out.println("Game Over");
        if (App.game.getWinner() == PLAYER) {
            mainStatusLabel.setText("You have won");
        }
        if (App.game.getWinner() == COMPUTER) {
            mainStatusLabel.setText("You have lost");
        }
        leftGrid.setMouseTransparent(true);
    }

    public void playerTurn(){
        // placing shot
        shotValue = App.game.placeShot(COMPUTER, chosenPoint);


        if (shotValue == Board.EMPTY || shotValue == Board.SHIP) {
            // update label
            updateLabel(leftStatusLabel, shotValue);

            // check if sunken
            if (shotValue == Board.SHIP) {
                sunkenShip = App.game.checkSunkenShip(COMPUTER, chosenPoint);
                if (sunkenShip != -1) {
                    mainStatusLabel.setText("You have sunk computers " + App.game.player[1].getShip()[sunkenShip].getName());
                }
            }

            updateLeftBoard(App.game.player[1].getBoard());
            changePlayer();
        } else {
            mainStatusLabel.setText("You've allready shot at this point!");
        }
    }

    public void computerTurn() {
        System.out.println("computer Turn");

        // Computer's turn
        chosenPoint = App.game.player[COMPUTER].aiShot(App.game.player[PLAYER].getBoard(), App.game.player[PLAYER].longestShipLength());

        computerShotPointLabel.setText("computer shoots at " + BattleshipGame.transformToCoordinate(chosenPoint));

        shotValue = App.game.placeShot(PLAYER, chosenPoint);
        updateLabel(rightStatusLabel, shotValue);

        if (shotValue == Board.SHIP) {
            sunkenShip = App.game.checkSunkenShip(PLAYER, chosenPoint);
            if (sunkenShip != -1) {
                mainStatusLabel.setText("Computer have sunk your " + App.game.player[PLAYER].getShip()[sunkenShip].getName());
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

    }




}


// TODO: skriv sammen left & right grid med boolean left/right som argument
