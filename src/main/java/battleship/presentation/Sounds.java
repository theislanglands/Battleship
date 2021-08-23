package battleship.presentation;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sounds {

    private static MediaPlayer sound;

    public static void playBang(){
        sound = new MediaPlayer(new Media(Sounds.class.getResource("bang.mp3").toExternalForm()));
        sound.play();
    }
}
