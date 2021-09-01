package battleship.presentation;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.HashMap;
import java.util.Map;

public class Sound {
    private static int soundFxLevel = 50; // 0-100
    private static final Map<Type, Media> sounds = new HashMap<>();

    public enum Type {
        SPLASH, BANG, ERROR, NOTIFICATION,CLICK,SUNK
    }

    public static void initializeSounds() {

        sounds.put(Type.SPLASH,new Media(Sound.class.getResource("medium_splash.mp3").toExternalForm()));
        sounds.put(Type.BANG, new Media(Sound.class.getResource("bang_short.mp3").toExternalForm()));
        sounds.put(Type.ERROR, new Media(Sound.class.getResource("error.mp3").toExternalForm()));
        sounds.put(Type.NOTIFICATION, new Media(Sound.class.getResource("click.mp3").toExternalForm()));
        sounds.put(Type.CLICK, new Media(Sound.class.getResource("click.mp3").toExternalForm()));
        sounds.put(Type.SUNK, new Media(Sound.class.getResource("bang_splash.mp3").toExternalForm()));
    }

    public static void play(Type soundType){
        MediaPlayer playSound = new MediaPlayer(sounds.get(soundType));
        playSound.setVolume((double) soundFxLevel / 100);
        playSound.play();
    }

    public static void setSoundFxLevel(int soundFxLevel) {
        if (soundFxLevel < 0 || soundFxLevel > 100) {
            System.out.println("sound must be between 0 and 100");
        } else {
            Sound.soundFxLevel = soundFxLevel;
        }
    }
}
