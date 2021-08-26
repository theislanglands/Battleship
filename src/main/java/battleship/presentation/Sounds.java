package battleship.presentation;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sounds {
    private static int soundFxLevel = 50; // 0-100
    private static final Media[] soundMedia = new Media[10];

    public final static int SPLASH = 0; // splash
    public final static int BANG = 1; // bang
    public final static int ERROR = 2;
    public final static int NOTIFICATION = 3;
    public final static int CLICK = 4;
    public final static int SUNK = 5;
    // use a map?

    public static void initializeSounds() {
        //  initialize media soundFx
        soundMedia[SPLASH] = new Media(Sounds.class.getResource("medium_splash.mp3").toExternalForm());
        soundMedia[BANG] = new Media(Sounds.class.getResource("bang_short.mp3").toExternalForm());
        soundMedia[ERROR] = new Media(Sounds.class.getResource("error.mp3").toExternalForm());
        soundMedia[NOTIFICATION] = new Media(Sounds.class.getResource("notification.mp3").toExternalForm());
        soundMedia[CLICK] = new Media(Sounds.class.getResource("click.mp3").toExternalForm());
        soundMedia[SUNK] = new Media(Sounds.class.getResource("bang_splash.mp3").toExternalForm());
    }

    public static void play(int boardCode){
        MediaPlayer playSound = new MediaPlayer(soundMedia[boardCode]);
        playSound.setVolume((double) soundFxLevel / 100);
        // 0.0 is inaudible and 1.0 is full volume, which is the default -hence the /100
        playSound.play();
    }

    public static void setSoundFxLevel(int soundFxLevel) {
        if (soundFxLevel < 0 || soundFxLevel > 100) {
            System.out.println("sound must be between 0 and 100");
        } else {
            Sounds.soundFxLevel = soundFxLevel;
        }
    }
}
