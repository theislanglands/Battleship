package battleship.presentation;

import battleship.domain.BattleshipGame;
import battleship.domain.Board;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class App extends Application {

    private static Scene scene;
    public static BattleshipGame game;

    public static Image[] cellImage = new Image[10];
    public static Image cursor;

    private static int gameSpeed = 3;

    // TODO set pic size according to gridSize



    @Override
    public void start(Stage stage) throws IOException {

        initializeCellImages();
        Sounds.initializeSounds();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Battleship_settings.fxml"));
        scene = new Scene(fxmlLoader.load(), 1024, 768);
        stage.setTitle("Battleship");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        App.scene = scene;
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
            cursor = new Image(getClass().getResource("cursor_cell.png").toURI().toString());
            // , leftPicSize, leftPicSize * 0.7, false, false

        } catch (URISyntaxException e) {
            System.out.println("image files not found");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public static int getGameSpeed() {
        return gameSpeed;
    }

    public static void setGameSpeed(int gameSpeed) {
        App.gameSpeed = gameSpeed;
    }
}