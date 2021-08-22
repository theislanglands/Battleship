package battleship.presentation;

import battleship.domain.BattleshipGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class BattleshipSettingsController {

    public TextField nameTextField;

    public Slider aiSlider;
    public Slider musicLevelSlider;
    public Slider soundFxSlider;
    public Slider gridSizeSlider;

    public Button startGameBtn;
    public Button quitBtn;

    public RadioButton radio4btn;
    public RadioButton radio5Btn;
    public RadioButton radio7btn;
    public RadioButton radio10btn;


    @FXML
    public void startGameBtnHandler(ActionEvent event) throws IOException {

        int fleetsize = 0;
        if (radio4btn.isSelected()) fleetsize = 4;
        if (radio5Btn.isSelected()) fleetsize = 5;
        if (radio7btn.isSelected()) fleetsize = 7;
        if (radio10btn.isSelected()) fleetsize = 10;

        System.out.println("slider " + (int)gridSizeSlider.getValue());
        System.out.println("fletsize " + fleetsize);
        App.game = new BattleshipGame(fleetsize, (int)gridSizeSlider.getValue());

        App.game.player[0].setName(nameTextField.getText());
        App.game.setAiLevel((int) aiSlider.getValue());

        App.setRoot("Primary");
    }

    @FXML
    public void quitBtnHandler(ActionEvent event) {
        System.exit(0);
    }

}
