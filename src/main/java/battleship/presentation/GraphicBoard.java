package battleship.presentation;

import battleship.domain.Board;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

public class GraphicBoard {

    public static Image[] cellImage = new Image[10];
    public static Image aimCursor;

    int gridSize;
    int picSize;
    ImageView[][] content;
    TilePane grid;

    Set<Integer> hide = new HashSet<>();
    Integer owner;

    public GraphicBoard(int gridSize, int picSize, ImageView[][] content, TilePane grid) {
        if (cellImage[0] == null) {
            initializeCellImages();
        }

        this.gridSize = gridSize;
        this.picSize = picSize;
        this.content = content;
        this.grid = grid;

        initializeGrid();
    }

    public void initializeCellImages() {


        try {
            cellImage[Board.HIT] = new Image(getClass().getResource("hit_cell.png").toURI().toString());
            cellImage[Board.EMPTY] = new Image(getClass().getResource("empty_cell.png").toURI().toString());
            cellImage[Board.SHIP] = new Image(getClass().getResource("ship_cell.png").toURI().toString());
            cellImage[Board.MISS] = new Image(getClass().getResource("miss_cell.png").toURI().toString());
            cellImage[Board.SUNK] = new Image(getClass().getResource("sunk_ship.png").toURI().toString());
            aimCursor = new Image(getClass().getResource("cursor_cell.png").toURI().toString());

        } catch (URISyntaxException e) {
            System.out.println("error loading image files");
            e.printStackTrace();
        }
    }

    private void initializeGrid(){

        // initialize board size

        grid.setMaxWidth(picSize * (gridSize + 1));
        grid.setMinWidth(picSize * (gridSize + 1));

        // Drawing corner
        ImageView corner = new ImageView(cellImage[Board.SHIP]);
        corner.setFitHeight(picSize);
        corner.setFitWidth(picSize);
        grid.getChildren().add(corner);

        // Drawing numbers
        for (int i = 1; i < gridSize + 1; i++) {
            StackPane numbers = new StackPane();
            ImageView addEmpty = new ImageView(cellImage[Board.EMPTY]);
            addEmpty.setFitHeight(picSize);
            addEmpty.setFitWidth(picSize);

            String number = "" + i;
            numbers.getChildren().add(addEmpty);
            numbers.getChildren().add(new Label(number));
            grid.getChildren().add(numbers);
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
            grid.getChildren().add(numbers);

            // adding empty fields
            for (int column = 0; column < gridSize; column++) {
                //content[row][column] = new ImageView(App.cellImage[Board.EMPTY]);
                content[row][column] = new ImageView();
                content[row][column].setFitHeight(picSize);
                content[row][column].setFitWidth(picSize);
                grid.getChildren().add(content[row][column]);
            }
        }
    }

    public void updateBoard() {
        if (owner != null) {
            updateBoard(owner);
        }
    }

    public void updateBoard(int playerNr) {
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {

                int valueAtPoint = App.game.player[playerNr].getBoard().getValue(new Point(row, column));

                if (hide.contains(valueAtPoint)) valueAtPoint = Board.EMPTY;
                content[row][column].setImage(cellImage[valueAtPoint]);
            }
        }
    }

    public void hide(int cellValue) {
        hide.add(cellValue);
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public void createShipInPane(Pane pane, int length, int image, int picSize) {

        // adding shiptiles to tilepane
        for (int j = 0; j < length; j++) {
            // initializing boattile
            ImageView boatTile = (new ImageView(GraphicBoard.cellImage[image]));
            boatTile.setFitHeight(picSize);
            boatTile.setFitWidth(picSize);

            // tilføjer skibs tile til tilepane
            pane.getChildren().add(boatTile);
        }
    }
}
