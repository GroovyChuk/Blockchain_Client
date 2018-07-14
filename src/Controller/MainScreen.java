package Controller;

import HTTPRest.RestActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import main.App;
import main.PersonalData;
import main.RequestData;
import main.Utility;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by alasdair on 09.01.18.
 */
public class MainScreen {

    @FXML
    private Button upload;
    @FXML
    private Button share;
    @FXML
    private ImageView sync;
    @FXML
    private MenuItem register_node;
    @FXML
    private MenuItem mode;
    @FXML
    private Label sync_text;
    @FXML
    private ProgressIndicator sync_bar;
    @FXML
    private TextField firstName;
    @FXML
    private TextField surname;
    @FXML
    private TextField dateOfBirth;
    @FXML
    private TextField city;
    @FXML
    private TextField zip;
    @FXML
    private TextField street;
    @FXML
    private ListView<String> request_list;
    private PersonalData personalData = new PersonalData();
    private ArrayList<RequestData> requests = new ArrayList<>();

    @FXML
    private void initialize() throws IOException {
        share.setDisable(true);
        request_list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (request_list.getSelectionModel().getSelectedItem() != null){
                    share.setDisable(false);
                }
            }
        });

        // Fetch PeronalData
        JSONArray chain = new Utility().fileToJSON("/home/alasdair/IdeaProjects/CreditScore_Chain/src/save/synced.txt");
        personalData.setDataFromJSON(chain,App.adress);
        firstName.setText(personalData.getFirstName());
        surname.setText(personalData.getSurname());
        dateOfBirth.setText(personalData.getDateOfBirth());
        city.setText(personalData.getCity());
        zip.setText(personalData.getZip());
        street.setText(personalData.getStreet());

        requests = collectRequests(chain);
        ObservableList<String> list = FXCollections.observableArrayList(requests.toString());
        request_list.setItems(list);
        System.out.println("");

        upload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                personalData = new PersonalData(surname.getText(), firstName.getText(), dateOfBirth.getText(), city.getText(), zip.getText(), street.getText());
                JSONObject data = new JSONObject();
                personalData.encryptData(App.privatePersonal);
                data.put("personalInformation", personalData.toJSON());
                data.put("creditInformation", new JSONObject());
                data.put("type", "U");
                System.out.println(new RestActions().postContactInfo(App.adress, data));
            }
        });

        share.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int pP = 0, pC = 0;
                System.out.println(request_list.getSelectionModel().getSelectedIndex());
                RequestData requestData = requests.get(request_list.getSelectionModel().getSelectedIndex());
                if (requestData.isPersonalInformation())
                    pP = App.privatePersonal;
                if (requestData.isCreditInformation())
                    pC = App.privateCredit;
                String response = new RestActions().encrypt(requestData,pP,pC);
                System.out.println(response);
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = null;
                try {
                    jsonObject = (JSONObject) parser.parse(response);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long secretPersonal = (Long) jsonObject.get("secretPersonal");
                Long secretCredit = (Long) jsonObject.get("secretCredit");
                JSONObject data = new JSONObject();
                data.put("type","S");
                data.put("secretPersonal",secretPersonal);
                data.put("secretCredit",secretCredit);
                data.put("referencedTransaction",requestData.getReferencedTransaction());
                System.out.println(new RestActions().shareInfo(App.adress,requestData.getRequestWallet(),data));
                System.out.println();
            }
        });

        register_node.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Register Node");
                dialog.setGraphic(null);
                dialog.setHeaderText("Please enter the IP of the node you would like to register.");

                // This allows you to get the response back from the user
                Optional<String> result = dialog.showAndWait();
                String entered = "";
                if (result.isPresent()) {
                    entered = result.get();
                }

                if (new Utility().isValidURL(entered))
                    System.out.println(new RestActions().registerNode(entered,App.clientIP));
                else
                    System.out.println("keine URL");
            }
        });

        sync.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                JSONObject syncedChain = new JSONObject();
                try {
                    syncedChain = new RestActions().getURL(new URL(App.clientIP + "/sync-chain"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter("/home/alasdair/IdeaProjects/CreditScore_Chain/src/save/synced.txt");
                    fileWriter.write(syncedChain.toJSONString());

                    System.out.println("Successfully Copied JSON Object to File...");
                    System.out.println("\nJSON Object: " + syncedChain);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        mode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                App.goToCompany();
            }
        });
    }

    public void updateUI() {
    }

    public ArrayList<RequestData> collectRequests(JSONArray chain){
        ArrayList<RequestData> temp = new ArrayList<>();
        if (chain != null)  {
            for (int block = 0; block < chain.size(); block++) {
                JSONObject object = (JSONObject) chain.get(block);
                JSONArray transactions = (JSONArray) object.get("transactions");
                for (int t = 0; t < transactions.size(); t++) {
                    JSONObject transaction = (JSONObject) transactions.get(t);
                    if (transaction.get("recipient").equals(App.adress)) {
                        JSONObject data = (JSONObject) transaction.get("data");
                        if (data.get("type").equals("R")){
                            String requestWallet = transaction.get("sender").toString();
                            boolean personalInfo = new Utility().parseBoolean((Long) data.get("personalInformation"));
                            boolean creditInfo = new Utility().parseBoolean((Long) data.get("creditInformation"));
                            JSONArray keyArray = (JSONArray) data.get("key");
                            JSONObject key = new JSONObject();
                            key.put("n",keyArray.get(0));
                            key.put("e",keyArray.get(1));
                            JSONObject referencedTransaction = new JSONObject();
                            referencedTransaction.put("block",block);
                            referencedTransaction.put("transaction",t);
                            temp.add(new RequestData(requestWallet,personalInfo,creditInfo,key,referencedTransaction));
                        }
                    }

                }
            }
        }
        return temp;
    }

}
