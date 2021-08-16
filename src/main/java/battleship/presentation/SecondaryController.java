package battleship.presentation;

import battleship.domain.Board;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

public class SecondaryController {

    int gridSize = 10;
    int picSize = 60;
    Image[] cellImage = new Image[10];


    @FXML
    TilePane board = new TilePane();

    // String imageFile = "battleship/presentation/images/miss_cell.png";

    @FXML
    public void initialize() {

        // setting size of board
        board.setPrefColumns(gridSize);
        board.setPrefRows(gridSize);
        board.setMaxWidth(picSize * gridSize);
        board.setMinWidth(picSize * gridSize);

        // loading images
        initializeCellImages();
        drawGrid(gridSize, picSize);
    }

    private void initializeCellImages() {
        System.out.println(getClass().getResource("hit_cell.png"));

        try {
            cellImage[Board.HIT] = new Image(getClass().getResource("hit_cell.png").toURI().toString());
            cellImage[Board.EMPTY] = new Image(getClass().getResource("empty_cell.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("Primary");
    }

    private void drawGrid(int gridSize, int picSize) {

        ImageView[][] addImage = new ImageView[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                addImage[i][j] = new ImageView(cellImage[Board.EMPTY]);
                addImage[i][j].setFitHeight(picSize);
                addImage[i][j].setFitWidth(picSize);

                board.getChildren().add(addImage[i][j]);
            }
        }
        addImage[2][0].setImage(cellImage[Board.HIT]);
    }
}
