package battleship.presentation;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class EndGamePopUpController {

    public Button yesBtn;
    public Button noBtn;

    public void endDialogHandler(ActionEvent actionEvent) {

        Sound.play(Sound.Type.CLICK);

        if (actionEvent.getSource().equals(noBtn)) {
            System.exit(0);
        }

        if (actionEvent.getSource().equals(yesBtn)) {

            // closing popup - stage!
            Stage stage = (Stage) yesBtn.getScene().getWindow();
            stage.close();

            // starting new game!
            try {
                App.setRoot("Battleship_settings");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
