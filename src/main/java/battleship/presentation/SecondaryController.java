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
import java.net.URISyntaxException;

public class SecondaryController {

    public Label gameLabel;
    public Label leftStatusLabel;
    public Label computerShotPointLabel;
    public Label rightStatusLabel;
    public Button quitBtn;


    int winner = -1;

    int gridSize = 10;
    int leftPicSize = 35;
    int rightPicSize = 25;
    public Point chosenPoint = null;

    public static Image[] cellImage = new Image[10];
    ImageView[][] leftContent = new ImageView[gridSize][gridSize];
    ImageView[][] rightContent = new ImageView[gridSize][gridSize];

    final int ERROR = 5;
    final int CURSOR = 6;
    final int COMPUTER = 1;
    final int PLAYER = 0;

    ImageCursor ship_cursor;

    int playerTurn = 0;
    int sunkenShip = -1;
    int shotValue;

    private Thread game;

    @FXML
    TilePane rightGrid = new TilePane();

    @FXML
    TilePane leftGrid = new TilePane();


    @FXML
    public void initialize() {

        // loading images
        initializeCellImages();

        // setting size of board
        initializeBoardSize(leftPicSize, gridSize, leftGrid);
        initializeBoardSize(rightPicSize, gridSize, rightGrid);

        // drawing left board
        //initializeGrid(gridSize, leftPicSize, leftGrid);
        // drawing right board
        //initializeGrid(gridSize, rightPicSize, rightGrid);

        initializeLeftGrid();
        initializeRightGrid();


        // generating random ships for left
        App.game.player[0].randomShipPlacement();
        updateRightBoard(App.game.player[PLAYER].getGrid());
        updateLeftBoard(App.game.player[COMPUTER].getGrid());

        // testing
        //testShot();
        /*
        game = new Thread(new runGame());
        game.setDaemon(true);
        game.start();
        //startGame();

         */
    }

    private void startGame()
    {


    }

    public void quitBtnHandler(ActionEvent actionEvent) {
        System.exit(0);
    }

    public class runGame implements Runnable {

        @Override
        public void run() {
            System.out.println("thread running!");

            while (true) {
                if (chosenPoint == null) {
                    System.out.println("player turn");
                    leftGrid.setMouseTransparent(false);
                    gameLabel.setText("Place your shot");
                }

                if (chosenPoint != null) {
                    leftGrid.setMouseTransparent(true);
                    System.out.println("computer turn");
                    computerTurn();
                    /*
                    synchronized (this) {
                        try {
                            //Thread.sleep(sleepTime);
                            wait(3000);
                        } catch (InterruptedException ex) {
                            System.out.println("Interrupted: " + Thread.currentThread());
                        }
                    }

                     */


                }

/*
                synchronized (this) {
                    try {
                        //Thread.sleep(sleepTime);
                        wait(1000);
                    } catch (InterruptedException ex) {
                        System.out.println("Interrupted: " + Thread.currentThread());
                    }
                }

 */

            }
        }
    }

    private void initializeCellImages() {
        System.out.println(getClass().getResource("cursor_cell.png"));

        try {
            cellImage[Board.HIT] = new Image(getClass().getResource("hit_cell.png").toURI().toString());
            cellImage[Board.EMPTY] = new Image(getClass().getResource("empty_cell.png").toURI().toString());
            cellImage[Board.SHIP] = new Image(getClass().getResource("ship_cell.png").toURI().toString());
            cellImage[Board.MISS] = new Image(getClass().getResource("miss_cell.png").toURI().toString());
            cellImage[5] = new Image(getClass().getResource("error_cell.png").toURI().toString());
            cellImage[6] = new Image(getClass().getResource("cursor_cell.png").toURI().toString());
            Image cursor = new Image(getClass().getResource("cursor_cell.png").toURI().toString(), leftPicSize * 0.7, leftPicSize * 0.7, false, false);
            ship_cursor = new ImageCursor(cursor);

        } catch (URISyntaxException e) {
            System.out.println("image files not found");
            e.printStackTrace();
        }
    }

    private void testShot() {
        rightContent[1][4].setImage(cellImage[Board.MISS]);
        rightContent[9][9].setImage(cellImage[Board.HIT]);
        rightContent[5][5].setImage(cellImage[Board.SHIP]);
        rightContent[3][3].setImage(cellImage[5]);
        rightContent[3][7].setImage(cellImage[6]);

        leftContent[0][1].setImage(cellImage[Board.SHIP]);
    }

    private void initializeBoardSize(int picSize, int gridSize, TilePane tilePane) {
        tilePane.setPrefColumns(gridSize + 1);
        tilePane.setPrefRows(gridSize + 1);
        tilePane.setMaxWidth(picSize * (gridSize + 1));
        tilePane.setMinWidth(picSize * (gridSize + 1));
    }

    private void initializeLeftGrid() {
        // Drawing corner
        ImageView corner = new ImageView(cellImage[Board.SHIP]);
        corner.setFitHeight(leftPicSize);
        corner.setFitWidth(leftPicSize);
        leftGrid.getChildren().add(corner);

        // Drawing numbers 1-10
        for (int i = 1; i < gridSize + 1; i++) {
            StackPane numbers = new StackPane();
            ImageView addEmpty = new ImageView(cellImage[Board.EMPTY]);
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
            ImageView addEmpty = new ImageView(cellImage[Board.EMPTY]);
            addEmpty.setFitHeight(leftPicSize);
            addEmpty.setFitWidth(leftPicSize);

            String letter = "" + (char) (65 + row);
            numbers.getChildren().add(addEmpty);
            numbers.getChildren().add(new Label(letter));
            leftGrid.getChildren().add(numbers);

            // adding empty fields
            for (int column = 0; column < gridSize; column++) {
                leftContent[row][column] = new ImageView(cellImage[Board.EMPTY]);
                leftContent[row][column].setFitHeight(leftPicSize);
                leftContent[row][column].setFitWidth(leftPicSize);
                leftGrid.getChildren().add(leftContent[row][column]);
            }
        }
    }

    private void initializeRightGrid() {
        // Drawing corner
        ImageView corner = new ImageView(cellImage[Board.SHIP]);
        corner.setFitHeight(rightPicSize);
        corner.setFitWidth(rightPicSize);
        rightGrid.getChildren().add(corner);

        // Drawing numbers 1-10
        for (int i = 1; i < gridSize + 1; i++) {
            StackPane numbers = new StackPane();
            ImageView addEmpty = new ImageView(cellImage[Board.EMPTY]);
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
            ImageView addEmpty = new ImageView(cellImage[Board.EMPTY]);
            addEmpty.setFitHeight(rightPicSize);
            addEmpty.setFitWidth(rightPicSize);

            String letter = "" + (char) (65 + row);
            numbers.getChildren().add(addEmpty);
            numbers.getChildren().add(new Label(letter));
            rightGrid.getChildren().add(numbers);

            // adding empty fields
            for (int column = 0; column < gridSize; column++) {
                rightContent[row][column] = new ImageView(cellImage[Board.EMPTY]);
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
                    leftContent[row][column].setImage(cellImage[valueAtPoint]);
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
                    rightContent[row][column].setImage(cellImage[valueAtPoint]);
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
            int x = (int) mouseEvent.getX() / leftPicSize - 1;
            int y = (int) mouseEvent.getY() / leftPicSize - 1;
            chosenPoint = new Point(y, x); // DEAL WITH THIS
            System.out.println(chosenPoint);

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
            leftStatusLabel.setText("You have won");
        }
        if (App.game.getWinner() == COMPUTER) {
            leftStatusLabel.setText("You have lost");
        }
        leftGrid.setMouseTransparent(true);
    }

    public void playerTurn(){
        // placing shot
        shotValue = App.game.placeShot(COMPUTER, chosenPoint);
        // update label
        updateLabel(leftStatusLabel,shotValue);

        // check if sunken
        if (shotValue == Board.SHIP) {
            sunkenShip = App.game.checkSunkenShip(COMPUTER, chosenPoint);
            if (sunkenShip != -1) {
                leftStatusLabel.setText("You have sunk computers " + App.game.player[1].getShip()[sunkenShip].getName());
            }
        }

        updateLeftBoard(App.game.player[1].getGrid());
        changePlayer();
    }

    public void computerTurn() {
        System.out.println("computer Turn");

        // Computer's turn
        chosenPoint = App.game.player[COMPUTER].aiShot(App.game.player[PLAYER].getGrid());

        computerShotPointLabel.setText("computer shoots at " + BattleshipGame.transformToCoordinate(chosenPoint));

        shotValue = App.game.placeShot(PLAYER, chosenPoint);
        updateLabel(rightStatusLabel, shotValue);


        if (shotValue == Board.SHIP) {
            sunkenShip = App.game.checkSunkenShip(PLAYER, chosenPoint);
            if (sunkenShip != -1) {
                rightStatusLabel.setText("Computer have sunk your " + App.game.player[PLAYER].getShip()[sunkenShip].getName());
                App.game.player[COMPUTER].setAiStatus(0);
            }
        }

        updateRightBoard(App.game.player[PLAYER].getGrid());
        changePlayer();
    }

    @FXML
    public void rollOver(MouseEvent mouseEvent) {
        System.out.println(mouseEvent.getEventType().getName());

        if (mouseEvent.getEventType().getName().equals("MOUSE_ENTERED")) {
            App.getScene().setCursor(ship_cursor);
        }
        if (mouseEvent.getEventType().getName().equals("MOUSE_EXITED")) {
            App.getScene().setCursor(Cursor.DEFAULT);
        }
    }
}


// TODO: skriv sammen left & right grid med boolean left/right som argument

/*
Task<Void> task = new Task<Void>() {

    @Override
    protected Void call() throws InterruptedException {
        updateMessage("Connecting.");
        Thread.sleep(500);
        updateMessage("Connecting..");
        Thread.sleep(500);
        updateMessage("Connecting...");
        Thread.sleep(500);

        return null;
    }

};

// bind status to task's message
Status.get().bind(task.messageProperty());

// run task on different thread
new Thread(task).start();
 */