package battleship.presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class EndGamePopUpController {

    public final Stage endDialogStage;
    Scene popUpScene = null;
    public Button yesBtn;
    public Button noBtn;

    public EndGamePopUpController() {
        endDialogStage = new Stage();
        endDialogStage.setTitle("GAME OVER");



    }

    private void playAgainPopUp() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("endGamePopUp.fxml"));

        try {
            popUpScene = new Scene(fxmlLoader.load(), 400, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }


        endDialogStage.setScene(popUpScene);
        endDialogStage.show();


    }

    public void endDialogHandler(ActionEvent actionEvent) {

        Sounds.play(Sounds.CLICK);

        if (actionEvent.getSource().equals(noBtn)) {
            System.out.println("no btn");
            // Platform.exit();
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
