package battleship.presentation;

import battleship.domain.BattleshipGame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static BattleshipGame game;
    private static Scene scene;
    private static int gameSpeed = 3;

    @Override
    public void start(Stage stage) throws IOException {
        Sound.initializeSounds();
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

// TODO: set minimum height and width of stage according to grids!