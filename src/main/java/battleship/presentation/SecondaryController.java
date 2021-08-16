package battleship.presentation;

import battleship.domain.Board;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

public class SecondaryController {

    int gridSize = 10;
    int picSize = 45;
    Image[] cellImage = new Image[10];
    ImageView[][] cellContent = new ImageView[gridSize][gridSize];

    @FXML
    TilePane board = new TilePane();


    // String imageFile = "battleship/presentation/images/miss_cell.png";

    @FXML
    public void initialize() {
        // loading images
        initializeCellImages();
        // setting size of board
        initializeBoardSize();
        // drawing board
        drawGrid(gridSize, picSize);
        // testing shot
        testShot();
    }

    private void testShot() {
        cellContent[1][4].setImage(cellImage[Board.MISS]);
        cellContent[9][9].setImage(cellImage[Board.HIT]);
        cellContent[5][5].setImage(cellImage[Board.SHIP]);
        cellContent[3][3].setImage(cellImage[5]);
    }

    private void initializeBoardSize() {
        board.setPrefColumns(gridSize + 1);
        board.setPrefRows(gridSize + 1);
        board.setMaxWidth(picSize * (gridSize + 1));
        board.setMinWidth(picSize * (gridSize + 1));
    }

    private void initializeCellImages() {
        System.out.println(getClass().getResource("hit_cell.png"));

        try {
            cellImage[Board.HIT] = new Image(getClass().getResource("hit_cell.png").toURI().toString());
            cellImage[Board.EMPTY] = new Image(getClass().getResource("empty_cell.png").toURI().toString());
            cellImage[Board.SHIP] = new Image(getClass().getResource("ship_cell.png").toURI().toString());
            cellImage[Board.MISS] = new Image(getClass().getResource("miss_cell.png").toURI().toString());
            cellImage[5] = new Image(getClass().getResource("error_cell.png").toURI().toString());

        } catch (URISyntaxException e) {
            System.out.println("image files not found");
            e.printStackTrace();
        }
    }


    private void drawGrid(int gridSize, int picSize) {

        // Drawing corner
        ImageView corner = new ImageView(cellImage[Board.SHIP]);
        corner.setFitHeight(picSize);
        corner.setFitWidth(picSize);
        board.getChildren().add(corner);

        // Drawing numbers 1-10
        for (int i = 1; i<gridSize+1; i++) {
            StackPane numbers = new StackPane();
            ImageView addEmpty = new ImageView(cellImage[Board.EMPTY]);
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
            ImageView addEmpty = new ImageView(cellImage[Board.EMPTY]);
            addEmpty.setFitHeight(picSize);
            addEmpty.setFitWidth(picSize);

            String letter = "" + (char) (65 + row);
            numbers.getChildren().add(addEmpty);
            numbers.getChildren().add(new Label(letter));
            board.getChildren().add(numbers);

            // adding empty fields

            for (int column = 0; column < gridSize; column++) {
                cellContent[row][column] = new ImageView(cellImage[Board.EMPTY]);
                cellContent[row][column].setFitHeight(picSize);
                cellContent[row][column].setFitWidth(picSize);

                board.getChildren().add(cellContent[row][column]);
            }
        }
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("Primary");
    }

    @FXML
    public void clickOnBoard(MouseEvent mouseEvent) {
        System.out.println(mouseEvent);

        Point chosenPoint;


        int x = (int) mouseEvent.getX() / picSize -1 ;
        int y = (int) mouseEvent.getY() / picSize -1 ;
        System.out.println("x: "+x);
        System.out.println("y:" + y);
        //chosenPoint =


    }
}
