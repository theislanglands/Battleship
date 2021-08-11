package battleship.presentation;

import javafx.fxml.FXML;
import java.io.IOException;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("Primary");
    }
}
