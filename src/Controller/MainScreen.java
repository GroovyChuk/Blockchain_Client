package Controller;

import HTTPRest.Request;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by alasdair on 09.01.18.
 */
public class MainScreen {

    @FXML
    AnchorPane anchorPane;
    @FXML
    AnchorPane anchorPaneCurrentProduct;
    @FXML
    AnchorPane anchorPaneCurrentProductNutInfo;
    @FXML
    ImageView imageUser;
    @FXML
    Label labelProductSum;
    @FXML
    Pane paneEmptyShoppingCart;

    @FXML
    Label labelProductName;
    @FXML
    Label labelProductPrice;
    @FXML
    Label labelProductProducer;
    @FXML
    Label labelProductType;

    @FXML
    Label labelProductCalories;
    @FXML
    Label labelProductCarbohydrate;
    @FXML
    Label labelProductFat;

    @FXML
    ImageView imageViewProductType;

    private Thread readerThread;

    @FXML
    private void initialize() throws IOException {

    }

    public void updateUI() {

    }
}
