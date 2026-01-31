package howly;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * App runs Main.java to start up GUI
 */
public class Main extends Application {
    private Howly howly = new Howly();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMinHeight(600.0);
            stage.setMinWidth(400.0);
            fxmlLoader.<MainWindow>getController().setHowly(howly); // Inject the Howly instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
