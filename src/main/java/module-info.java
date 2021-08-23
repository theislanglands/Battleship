module battleship.battleship2 {
    requires javafx.controls;
    requires javafx.fxml;


    requires org.controlsfx.controls;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;

    opens battleship.presentation to javafx.fxml, javafx.media;
    exports battleship.presentation;
}