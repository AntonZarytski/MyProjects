package client.start;

import client.controllers.Autorisation;
import client.controllers.WorkWindowControll;
import client.model.Connection;
import client.controllers.Registration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private final static int PORT = 8189;
    private final static String HOST = "localhost";
    public static final String MY_CLOUD_TITLE = "Мое облако";
    private static Autorisation auth;
    private static Registration reg;
    public static Connection connection;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../fxml/workWindow.fxml"));
        Parent fxmlMain = fxmlLoader.load();
        WorkWindowControll maiController = fxmlLoader.getController();
        maiController.setStage(primaryStage);
        primaryStage.setTitle(MY_CLOUD_TITLE);
        primaryStage.setScene(new Scene(fxmlMain));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
