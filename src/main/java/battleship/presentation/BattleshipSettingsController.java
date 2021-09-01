package battleship.presentation;

import battleship.domain.BattleshipGame;
import battleship.domain.Player;
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
    public Slider gamespeedSlider;


    @FXML
    public void startGameBtnHandler() throws IOException {
        Sound.play(Sound.Type.CLICK);

        // setting game paremeters
        int fleetsize = 0;
        if (radio4btn.isSelected()) fleetsize = 4;
        if (radio5Btn.isSelected()) fleetsize = 5;
        if (radio7btn.isSelected()) fleetsize = 7;
        if (radio10btn.isSelected()) fleetsize = 10;

        App.game = new BattleshipGame(fleetsize, (int)gridSizeSlider.getValue());

        App.game.player[Player.PLAYER].setName(nameTextField.getText());
        App.game.setAiLevel((int) aiSlider.getValue());
        Sound.setSoundFxLevel((int) soundFxSlider.getValue()*10 );
        App.setGameSpeed(5 - (int) gamespeedSlider.getValue());

        App.setRoot("PlaceShips");
    }

    @FXML
    public void quitBtnHandler() {
        Sound.play(Sound.Type.CLICK);
        System.exit(0);
    }

    public void changeSoundFxVolumeHandler() {
        Sound.setSoundFxLevel((int) soundFxSlider.getValue()*10 );
        Sound.play(Sound.Type.SPLASH);
    }
}
