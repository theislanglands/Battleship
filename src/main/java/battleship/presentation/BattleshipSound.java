package battleship.presentation;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.spi.AudioFileReader;
import java.applet.AudioClip;

public class BattleshipSound {

    // get sound clips
    //
    public Clip[] soundClip = new Clip[10];

    public void initSound(){

        // Button myButton = new Button("Press me for sound!");
        //myButton.setOnAction(event -> {
        //    new AudioClip(
        //            getClass()
        //                    .getResource("/audio/buzzer.mp3")
        //                    .toExternalForm())
        //            .play();
        //});

        // soundClip[0] = new AudioInputStream();

                //Clip(this.getClass().getResource("bang.mp3"));
    }

    public void hitSound(){

    }

}
