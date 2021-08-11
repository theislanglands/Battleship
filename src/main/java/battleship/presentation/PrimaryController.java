package battleship.presentation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("Secondary");
    }

}