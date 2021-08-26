package battleship.presentation;

import battleship.domain.BattleshipGame;
import battleship.domain.Board;
import battleship.domain.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;


import java.awt.*;
import java.io.IOException;

import static battleship.domain.BattleshipGame.gridSize;
import static battleship.domain.BattleshipGame.noOfShips;

public class PrimaryController {
    // fleetbox
    public GridPane fleetBox;
    public HBox[] shipsHBox = new HBox[noOfShips];

    // board
    public TilePane board;
    ImageView[][] boardContent = new ImageView[BattleshipGame.gridSize][BattleshipGame.gridSize];

    int picSize = 45 - gridSize;;
    Image shipCursor;
    private int selectedShip = -1;
    private int shipsPlaced = 0;


    @FXML
    public void initialize() {
        App.game.intilializeShips(App.game.getNoOfShips());
        initializeBoard();
        fleetBox.setVgap(15);
        fleetBox.setHgap(15);

        showFleet();
    }

    private void showFleet() {

        for (int shipNr = 0; shipNr < noOfShips; shipNr++) {
            // shiplength & name
            int length = App.game.player[0].getShip()[shipNr].getLength();
            String name = App.game.player[0].getShip()[shipNr].getName();
            // array of tilepanes matching ship nr.
            shipsHBox[shipNr] = new HBox();

            // adding shiptiles to tilepane
            for (int j = 0; j < length; j++) {
                // initializing boattile
                ImageView boatTile = (new ImageView(App.cellImage[Board.SHIP]));
                boatTile.setFitHeight(picSize);
                boatTile.setFitWidth(picSize);
                // tilføjer skibs tile til tilepane
                shipsHBox[shipNr].getChildren().add(boatTile);
            }
            // Tileføjes TilePane med skib + navn til GridPane (fleetBox)
            Label shipLabel = new Label(name);
            shipLabel.setFont(new Font(21));

            fleetBox.add(shipsHBox[shipNr], 0, shipNr);
            fleetBox.add(shipLabel, 1, shipNr);
        }
    }

    @FXML
    private void switchToSecondary() throws IOException {
        Sounds.play(Sounds.NOTIFICATION);
        App.setRoot("Secondary");
    }

    @FXML
    public void quitBtnHandler(ActionEvent actionEvent) {
        Sounds.play(Sounds.CLICK);
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

    public void clickOnBoatHandler(MouseEvent mouseEvent) {

        // TODO: check om skib allerede er placeret!

        // hvis der ikke allerede er valgt et skib
        if (selectedShip == -1) {

            // finder Node der er klikket på
            Node clickedNode = mouseEvent.getPickResult().getIntersectedNode();
            // finder parent til Node
            Node parent = clickedNode.getParent();

            // søger op i node hieraki indtil vi finder fleetBox
            while (parent != fleetBox) {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }

            // henter row & column Index
            int colIndex = GridPane.getColumnIndex(clickedNode);
            selectedShip = GridPane.getRowIndex(clickedNode);
            System.out.println("Mouse clicked cell: " + colIndex + " And: " + selectedShip);
            // row index = selected ship

            // getting ship as Image
            shipCursor = shipsHBox[selectedShip].snapshot(new SnapshotParameters(), null);
            // setting cursor to shipCursor
            App.getScene().setCursor(new ImageCursor(shipCursor,(gridSize/2),(gridSize/2)));
            // hiding selected ship
            shipsHBox[selectedShip].setVisible(false);

            // playing clicksound
            Sounds.play(Sounds.CLICK);
        }
    }

    public void flipShipOrientation(){

        // TODO: bug in rotation of even numbered ships - go back to original if horisontal
        if (selectedShip != -1) {
            App.game.player[0].getShip()[selectedShip].flipOrientation();

            // flipping cursor
            // make new with V-box?
            ImageView iw = new ImageView(shipCursor);
            iw.setRotate(90);
            Image rotatedCursor = iw.snapshot(new SnapshotParameters(), null);
            shipCursor = rotatedCursor;
            App.getScene().setCursor(new ImageCursor(shipCursor,(gridSize/2),(gridSize/2)));
        }
    }
    private Image makeTransparent(Image inputImage) {

        final int TOLERANCE_THRESHOLD = 0xFF;

        int W = (int) inputImage.getWidth();
        int H = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(W, H);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int argb = reader.getArgb(x, y);

                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                if (r >= TOLERANCE_THRESHOLD
                        && g >= TOLERANCE_THRESHOLD
                        && b >= TOLERANCE_THRESHOLD) {
                    argb &= 0x00FFFFFF;
                }

                writer.setArgb(x, y, argb);
            }
        }

        return outputImage;
    }

    public void keyPressed(KeyEvent keyEvent) throws IOException {
        String pressedKey = keyEvent.getText();

        // check if flip!
        if (pressedKey.equalsIgnoreCase("f")) {
            Sounds.play(Sounds.CLICK);
            flipShipOrientation();
        }

        // chick if r
        if (pressedKey.equalsIgnoreCase("r")) {
            App.game.player[0].randomShipPlacement();
            App.getScene().setCursor(Cursor.DEFAULT);
            switchToSecondary();


        }
    }

    public void placeShip(MouseEvent mouseEvent) throws IOException {

        // checking if a ship is selected
        if (selectedShip != -1) {
            Point chosenPoint;
            // get clicked coordinate
            // getting chosenPoint
            int col = (int) mouseEvent.getX() / picSize - 1;
            int row = (int) mouseEvent.getY() / picSize - 1;
            chosenPoint = new Point(row, col);
            System.out.println(chosenPoint);

            // TODO : lave metode i battleship game til det her!
            // check if able to place
            boolean canPlace = App.game.player[0].getShip()[selectedShip].canPlace(chosenPoint, App.game.player[0].getBoard());

            if (!canPlace) {
                Sounds.play(Sounds.ERROR);
            }

            if (canPlace) {
                System.out.println("can place");

                // PLacing ship
                System.out.println("isHor " + App.game.player[0].getShip()[selectedShip].isHorizontal());
                App.game.player[0].placeShip(selectedShip, chosenPoint);

                // updating board
                updateBoard();

                // resetting selectedShip
                selectedShip = -1;

                // changing cursor to default
                App.getScene().setCursor(Cursor.DEFAULT);

                // updating placement count
                shipsPlaced++;

                // checking if all ships are placed
                if (shipsPlaced == noOfShips) {
                    switchToSecondary();
                }

                // click sound
                Sounds.play(Sounds.CLICK);
            }
        }
    }

    private void updateBoard() {
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {

                int valueAtPoint = App.game.player[Player.PLAYER].getBoard().getValue(new Point(row, column));

                boardContent[row][column].setImage(App.cellImage[valueAtPoint]);
            }
        }
    }
}