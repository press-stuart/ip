package bobby;

import java.io.IOException;

import bobby.ui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Bobby using FXML.
 */
public class Main extends Application {
    private MainWindow controller;
    private Bobby bobby = new Bobby();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            controller = fxmlLoader.getController();
            controller.setBobby(bobby); // inject the Bobby instance
            stage.show();

            String introMessage = bobby.getIntroMessage();
            controller.displayMessageFromBobby(introMessage);

            String loadErrorMessage = bobby.loadTasks();
            if (loadErrorMessage != null) {
                controller.displayMessageFromBobby(loadErrorMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        String cleanUpErrorMessage = bobby.cleanUpAfterExit();
        if (cleanUpErrorMessage != null) {
            controller.displayMessageFromBobby(cleanUpErrorMessage);
        }
    }
}
