package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {

    public static Stage window;
    public static Scene companyScene;
    public static Scene mainScene;
    public static String clientIP = "http://localhost:5001";
    public static String CompanyIP = "http://localhost:5000";
    public static String adress = "myWallet";
    public static String companyAdress = "companyWallet";
    public static int privatePersonal = 123123;
    public static int privateCredit = 321321;
    public static String initVector = "RandomInitVector";
    private Stage stage;



    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        stage = primaryStage;
        companyScene = new Scene(FXMLLoader.load(getClass().getResource("/layouts/company_screen.fxml")), 700, 400);
        mainScene = new Scene(FXMLLoader.load(getClass().getResource("/layouts/main_screen.fxml")), 700, 400);

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

    public static void goToCompany(){
        window.setScene(companyScene);
    }

    public static void goToMain(){
        window.setScene(mainScene);
    }

    private Parent replaceSceneContent(String fxml) throws Exception {
        Parent page = (Parent) FXMLLoader.load(App.class.getResource(fxml), null, new JavaFXBuilderFactory());
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page, 700, 450);
            scene.getStylesheets().add(App.class.getResource("demo.css").toExternalForm());
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(page);
        }
        stage.sizeToScene();
        return page;
    }
}
