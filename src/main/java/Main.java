import com.opencsv.CSVReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("window.fxml")));
        stage.setScene(new Scene(pane));
        stage.setTitle("HospitalFX");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}