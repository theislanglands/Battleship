package battleship.presentation;

import battleship.domain.BattleshipGame;
import battleship.domain.Player;
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

public class PlaceShipsController {

    public GridPane fleetBox;

    public TilePane board;
    GraphicBoard graphicBoard;
    ImageView[][] boardContent = new ImageView[BattleshipGame.gridSize][BattleshipGame.gridSize];

    int picSize = 45 - gridSize;
    private int selectedShip = -1;
    private int shipsPlaced = 0;

    @FXML
    public void initialize() {
        graphicBoard = new GraphicBoard(gridSize, picSize, boardContent, board);
        graphicBoard.setOwner(Player.PLAYER);
        graphicBoard.updateBoard();
        showFleet();
    }

    private void showFleet() {
        Pane[] shipHBox = new Pane[noOfShips];

        for (int shipNr = 0; shipNr < noOfShips; shipNr++) {
            // shiplength & name
            int length = App.game.player[Player.PLAYER].getShip()[shipNr].getLength();
            String shipName = App.game.player[Player.PLAYER].getShip()[shipNr].getName();

            // adding ship to fleetbox (GridPane)
            shipHBox[shipNr] = new HBox();
            graphicBoard.createShipInPane(shipHBox[shipNr], length);

            Label shipLabel = new Label(shipName);
            shipLabel.setFont(new Font(21));

            fleetBox.add(shipHBox[shipNr], 0, shipNr);
            fleetBox.add(shipLabel, 1, shipNr);
        }
    }

    public void clickOnBoatHandler(MouseEvent mouseEvent) {

        Node clickedNode = mouseEvent.getPickResult().getIntersectedNode();

        if (clickedNode != fleetBox && selectedShip == -1) {

            // søger op i node-hieraki indtil vi finder fleetBox
            Node parent = clickedNode.getParent();

            while (parent != fleetBox) {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }

            int colIndex = GridPane.getColumnIndex(clickedNode);

            // checks if we are in the column with ships
            if (colIndex == 0) {
                // row index = selected ship
                selectedShip = GridPane.getRowIndex(clickedNode);

                setSelectedShipAsCursor();

                // hide's selected ship!
                fleetBox.getChildren().get((selectedShip * 2)).setVisible(false);

                Sound.play(Sound.Type.CLICK);
            }
        }
    }

    public void setSelectedShipAsCursor() {
        // make cursor with new orientation

        boolean isHorizontal = App.game.player[Player.PLAYER].getShip()[selectedShip].isHorizontal();
        int shipLength = App.game.player[Player.PLAYER].getShip()[selectedShip].getLength();

        Pane newShipCursor = null;
        if (isHorizontal) newShipCursor = new HBox();
        if (!isHorizontal) newShipCursor = new VBox();

        graphicBoard.createShipInPane(newShipCursor, shipLength);
        Image shipCursor = newShipCursor.snapshot(new SnapshotParameters(), null);

        App.getScene().setCursor(new ImageCursor(shipCursor, gridSize / 2, gridSize / 2));
    }

    public void placeShip(MouseEvent mouseEvent) throws IOException {

        // checking if a ship is selected
        if (selectedShip != -1) {
            // getting chosenPoint
            Point chosenPoint;
            int col = (int) mouseEvent.getX() / picSize - 1;
            int row = (int) mouseEvent.getY() / picSize - 1;
            chosenPoint = new Point(row, col);

            System.out.println(chosenPoint);

            // TODO : lave metode i battleship game til det her!

            // check if able to place
            boolean canPlace = App.game.player[Player.PLAYER].getShip()[selectedShip].canPlace(chosenPoint, App.game.player[Player.PLAYER].getBoard());

            if (!canPlace) {
                Sound.play(Sound.Type.ERROR);
            }

            if (canPlace) {
                App.game.player[Player.PLAYER].placeShip(selectedShip, chosenPoint);
                graphicBoard.updateBoard();
                selectedShip = -1;
                App.getScene().setCursor(Cursor.DEFAULT);
                Sound.play(Sound.Type.CLICK);
                shipsPlaced++;

                if (shipsPlaced == noOfShips) {
                    switchToGameplay();
                }
            }
        }
    }

    public void keyPressed(KeyEvent keyEvent) throws IOException {
        String pressedKey = keyEvent.getText();

        // Flip orientation of selected ship
        if (pressedKey.equalsIgnoreCase("f")) {
            if (selectedShip != -1) {
                Sound.play(Sound.Type.CLICK);
                App.game.player[Player.PLAYER].getShip()[selectedShip].flipOrientation();
                setSelectedShipAsCursor();
            }
        }

        // random ship placement
        if (pressedKey.equalsIgnoreCase("r")) {
            App.game.player[Player.PLAYER].randomShipPlacement();
            App.getScene().setCursor(Cursor.DEFAULT);
            switchToGameplay();
        }
    }

    @FXML
    private void switchToGameplay() throws IOException {
        Sound.play(Sound.Type.NOTIFICATION);
        App.setRoot("GamePlay");
    }

    @FXML
    public void quitBtnHandler() {
        Sound.play(Sound.Type.CLICK);
        System.exit(0);
    }
}