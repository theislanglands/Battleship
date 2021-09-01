package battleship.presentation;

import battleship.domain.Cell;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphicBoard {

    static Map<Cell, Image> cellImage;
    static Image aimCursor;

    int gridSize;
    int picSize;
    ImageView[][] content;
    TilePane grid;

    Set<Cell> hide = new HashSet<>();
    Integer owner;

    public GraphicBoard(int gridSize, int picSize, ImageView[][] content, TilePane grid) {
        this.gridSize = gridSize;
        this.picSize = picSize;
        this.content = content;
        this.grid = grid;

        if (cellImage == null) {
            initializeImages();
        }

        initializeGrid();
    }

    private void initializeImages() {
        cellImage = new HashMap<>();

        try {
            aimCursor = new Image(getClass().getResource("cursor_cell.png").toURI().toString());

            cellImage.put(Cell.HIT, new Image(getClass().getResource("hit_cell.png").toURI().toString()));
            cellImage.put(Cell.EMPTY, new Image(getClass().getResource("empty_cell.png").toURI().toString()));
            cellImage.put(Cell.SHIP, new Image(getClass().getResource("ship_cell.png").toURI().toString()));
            cellImage.put(Cell.MISS, new Image(getClass().getResource("miss_cell.png").toURI().toString()));
            cellImage.put(Cell.SUNK, new Image(getClass().getResource("sunk_ship.png").toURI().toString()));

        } catch (URISyntaxException | NullPointerException e) {
            System.out.println("error loading image files");
            e.printStackTrace();
        }
    }

    private void initializeGrid(){

        // initialize board size
        grid.setMaxWidth(picSize * (gridSize + 1));
        grid.setMinWidth(picSize * (gridSize + 1));

        // Drawing corner
        ImageView corner = new ImageView(cellImage.get(Cell.SHIP));
        corner.setFitHeight(picSize);
        corner.setFitWidth(picSize);
        grid.getChildren().add(corner);

        // Drawing numbers
        for (int i = 1; i < gridSize + 1; i++) {
            StackPane numbers = new StackPane();
            ImageView addEmpty = new ImageView(cellImage.get(Cell.EMPTY));
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
            ImageView addEmpty = new ImageView(cellImage.get(Cell.EMPTY));
            addEmpty.setFitHeight(picSize);
            addEmpty.setFitWidth(picSize);

            String letter = "" + (char) (65 + row);
            numbers.getChildren().add(addEmpty);
            numbers.getChildren().add(new Label(letter));
            grid.getChildren().add(numbers);

            // adding empty fields
            for (int column = 0; column < gridSize; column++) {
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

                Cell valueAtPoint = App.game.player[playerNr].getBoard().getValue(new Point(row, column));
                if (hide.contains(valueAtPoint)) valueAtPoint = Cell.EMPTY;
                content[row][column].setImage(cellImage.get(valueAtPoint));
            }
        }
    }

    public void hide(Cell cellValue) {
        hide.add(cellValue);
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public void createShipInPane(Pane pane, int length, Cell image, int picSize) {

        // adding shiptiles to tilepane
        for (int j = 0; j < length; j++) {
            // initializing boattile
            ImageView boatTile = (new ImageView(cellImage.get(image)));
            boatTile.setFitHeight(picSize);
            boatTile.setFitWidth(picSize);

            // tilføjer skibs tile til tilepane
            pane.getChildren().add(boatTile);
        }
    }

    public void createShipInPane(Pane pane, int length) {

        // adding shiptiles to pane
        for (int j = 0; j < length; j++) {
            // initializing boattile
            ImageView boatTile = new ImageView(cellImage.get(Cell.SHIP));
            boatTile.setFitHeight(picSize);
            boatTile.setFitWidth(picSize);

            // tilføjer skibs tile til pane (HBOX / VBox)
            pane.getChildren().add(boatTile);
        }
    }
}
