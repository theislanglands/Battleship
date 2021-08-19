package battleship.presentation;

import battleship.domain.BattleshipGame;
import battleship.domain.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;

import static battleship.domain.BattleshipGame.gridSize;
import static battleship.domain.BattleshipGame.noOfShips;

public class PrimaryController {

    public GridPane fleetBox;
    public Label ship1Label;
    public TilePane[] shipsTilePane = new TilePane[noOfShips];
    public TilePane shipTilePane = new TilePane();

    public TilePane board;

    public Label shiplabel;
    int picSize = 35;
    ImageView[][] boardContent = new ImageView[BattleshipGame.gridSize][BattleshipGame.gridSize];


    @FXML
    public void initialize() {
    initializeBoard();

    showFleet();
    }

    private void showFleet() {

        for (int shipNr = 0; shipNr < noOfShips; shipNr++) {
            int length = App.game.player[0].getShip()[shipNr].getLength();
            String name = App.game.player[0].getShip()[shipNr].getName();
            shipsTilePane[shipNr] = new TilePane();

            // adding ship
            for (int j = 0; j < length; j++) {
                ImageView boatTile = (new ImageView(App.cellImage[Board.SHIP]));
                boatTile.setFitHeight(picSize);
                boatTile.setFitWidth(picSize);

                // tilepane med skibe
                shipsTilePane[shipNr].getChildren().add(boatTile);
            }
            // label med navn
            shiplabel = new Label(name);

            fleetBox.add(shipsTilePane[shipNr], 0, shipNr);
            fleetBox.add(shiplabel, 1, shipNr);
            //ship1Label.setText(name);
        }
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("Secondary");
    }

    @FXML
    public void quitBtnHandler(ActionEvent actionEvent) {
        System.exit(0);
    }


    private void initializeBoard() {

        //board.setPrefColumns(gridSize + 1);
        // board.setPrefRows(gridSize + 1);
        board.setMaxWidth(picSize * (gridSize + 1));
        board.setMinWidth(picSize * (gridSize + 1));

        // Drawing corner
        ImageView corner = new ImageView(App.cellImage[Board.SHIP]);
        corner.setFitHeight(picSize);
        corner.setFitWidth(picSize);
        board.getChildren().add(corner);

        // Drawing numbers
        for (int i = 1; i < gridSize + 1; i++) {
            StackPane numbers = new StackPane();
            ImageView addEmpty = new ImageView(App.cellImage[Board.EMPTY]);
            addEmpty.setFitHeight(picSize);
            addEmpty.setFitWidth(picSize);

            String number = "" + i;
            numbers.getChildren().add(addEmpty);
            numbers.getChildren().add(new Label(number));
            board.getChildren().add(numbers);
        }

        // Drawing Rows
        for (int row = 0; row < gridSize; row++) {

            // adding letter
            StackPane numbers = new StackPane();
            ImageView addEmpty = new ImageView(App.cellImage[Board.EMPTY]);
            addEmpty.setFitHeight(picSize);
            addEmpty.setFitWidth(picSize);

            String letter = "" + (char) (65 + row);
            numbers.getChildren().add(addEmpty);
            numbers.getChildren().add(new Label(letter));
            board.getChildren().add(numbers);

            // adding empty fields
            for (int column = 0; column < gridSize; column++) {
                boardContent[row][column] = new ImageView(App.cellImage[Board.EMPTY]);
                boardContent[row][column].setFitHeight(picSize);
                boardContent[row][column].setFitWidth(picSize);
                board.getChildren().add(boardContent[row][column]);
            }
        }
    }
}