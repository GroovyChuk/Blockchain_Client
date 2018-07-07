package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {

    public static Stage window;
    public static Scene loginScene;
    public static Scene mainScene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;

        loginScene = new Scene(FXMLLoader.load(getClass().getResource("/layouts/login_screen.fxml")), 600, 400);
        mainScene = new Scene(FXMLLoader.load(getClass().getResource("/layouts/main_screen.fxml")), 600, 400);

        window.setTitle("Client v0.1");
        window.setScene(mainScene);
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
                public void handle(WindowEvent event) {

            }
        });
        window.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
