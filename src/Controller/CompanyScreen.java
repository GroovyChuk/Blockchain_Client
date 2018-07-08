package Controller;

import HTTPRest.RestActions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import main.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by alasdair on 09.01.18.
 */
public class CompanyScreen {
    @FXML
    private Button upload;
    @FXML
    private Label textP;
    @FXML
    private Label textC;
    @FXML
    private ImageView sync;
    @FXML
    private TextField address;
    @FXML
    private MenuItem register_node;
    @FXML
    private MenuItem mode;
    @FXML
    private CheckBox personal;
    @FXML
    private CheckBox credit;
    private ArrayList<RequestedData> requests = new ArrayList<>();
    @FXML
    private ListView<String> request_list;
    ArrayList<PersonalData> personalDataList = new ArrayList<>();
    ArrayList<String> receivedWalletData = new ArrayList<>();

    @FXML
    private void initialize() throws IOException {
        textP.setText("Persönliche Daten \n nicht freigegeben");
        textC.setText("Kreditdaten \n nicht freigegeben");
        // Fetch PeronalData
        JSONArray chain = new Utility().fileToJSON("/home/alasdair/IdeaProjects/CreditScore_Chain/src/save/syncedCompany.txt");
        JSONArray keys = new Utility().fileToJSON("/home/alasdair/IdeaProjects/CreditScore_Chain/src/save/companyKeys.txt");
        requests = collectRequests(chain);


        for (int i = 0; i < keys.size(); i++) {
            JSONObject key = (JSONObject) keys.get(i);
            Long blockIndex = (Long) key.get("blockIndex");
            Long transactionIndex = (Long) key.get("transactionIndex");
            Long encryptedPersonal = new Long(-1);
            Long encryptedCredit = new Long(-1);


            // get encrpyted messages from next blocks
            for (int j = blockIndex.intValue()+1; j < chain.size(); j++) {
                JSONObject block = (JSONObject) chain.get(j);
                JSONArray transactions = (JSONArray) block.get("transactions");
                for (int k = 0; k < transactions.size(); k++) {
                    JSONObject transaction = (JSONObject) transactions.get(k);
                    JSONObject data = (JSONObject) transaction.get("data");
                    JSONObject referencedTransaction = (JSONObject) data.get("referencedTransaction");
                    Long rBlock = (Long) referencedTransaction.get("block");
                    Long rTransaction = (Long) referencedTransaction.get("transaction");
                    if (data.get("type").equals("S") && rBlock == blockIndex && rTransaction == transactionIndex){
                        encryptedPersonal = (Long) data.get("secretPersonal");
                        encryptedCredit = (Long) data.get("secretCredit");
                    }
                }
            }

            // look for correct request object
            for (RequestedData request : requests) {
                int block = (int) request.getReqestedInTransaction().get("block");
                int transaction = (int) request.getReqestedInTransaction().get("transaction");
                if (block == blockIndex.intValue() && transaction == transactionIndex.intValue()){
                    receivedWalletData.add(request.getRequestedWallet());
                    JSONObject privateKey = new JSONObject();
                    JSONArray keyArray = (JSONArray) key.get("privateKey");
                    privateKey.put("n",keyArray.get(0));
                    privateKey.put("e",keyArray.get(1));
                    request.setPrivateKey(privateKey);
                    request.setEncryptedPersonal(encryptedPersonal.intValue());
                    request.setEncryptedCredit(encryptedCredit.intValue());
                }
            }

        }

        personalDataList = getPersonalData(chain);
        ObservableList<String> list = FXCollections.observableArrayList(requests.toString());
        request_list.setItems(list);

        request_list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (request_list.getSelectionModel().getSelectedItem() != null){
                    textP.setVisible(true);
                    textP.setText(personalDataList.get(0).toString());
                }
            }
        });

        mode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                App.goToMain();
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
                    System.out.println(new RestActions().registerNode(entered,App.CompanyIP));
                else
                    System.out.println("keine URL");
            }
        });

        upload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JSONObject data = new JSONObject();
                data.put("type","R");
                data.put("key","");
                if(personal.isSelected())
                    data.put("personalInformation",1);
                else
                    data.put("personalInformation",0);
                if(credit.isSelected())
                    data.put("creditInformation",1);
                else
                    data.put("creditInformation",0);
                String response = new RestActions().requestData(App.companyAdress,address.getText(), data);
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = null;
                try {
                    jsonObject = (JSONObject) parser.parse(response);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                JSONObject newRequest = new JSONObject();
                newRequest.put("blockIndex",jsonObject.get("block_index"));
                newRequest.put("transactionIndex",jsonObject.get("transaction_index"));
                newRequest.put("privateKey",jsonObject.get("private_key"));

                JSONArray keys = new JSONArray();
                try {
                    keys = new Utility().fileToJSON("/home/alasdair/IdeaProjects/CreditScore_Chain/src/save/companyKeys.txt");
                    if (keys == null)
                        keys = new JSONArray();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                keys.add(newRequest);

                FileWriter fileWriter = null;

                try {
                    fileWriter = new FileWriter("/home/alasdair/IdeaProjects/CreditScore_Chain/src/save/companyKeys.txt");
                    fileWriter.write(keys.toJSONString());
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

        sync.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                JSONObject syncedChain = new JSONObject();
                try {
                    syncedChain = new RestActions().getURL(new URL(App.CompanyIP + "/sync-chain"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter("/home/alasdair/IdeaProjects/CreditScore_Chain/src/save/syncedCompany.txt");
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
    }

    public void updateUI() {
    }

    public ArrayList<RequestedData> collectRequests(JSONArray chain){
        ArrayList<RequestedData> temp = new ArrayList<>();
        if (chain != null)  {
            for (int block = 0; block < chain.size(); block++) {
                JSONObject object = (JSONObject) chain.get(block);
                JSONArray transactions = (JSONArray) object.get("transactions");
                for (int t = 0; t < transactions.size(); t++) {
                    JSONObject transaction = (JSONObject) transactions.get(t);
                    if (transaction.get("sender").equals(App.companyAdress)) {
                        JSONObject data = (JSONObject) transaction.get("data");
                        if (data.get("type").equals("R")){
                            String requestWallet = transaction.get("recipient").toString();
                            boolean personalInfo = new Utility().parseBoolean((Long) data.get("personalInformation"));
                            boolean creditInfo = new Utility().parseBoolean((Long) data.get("creditInformation"));
                            JSONObject referencedTransaction = new JSONObject();
                            referencedTransaction.put("block",block);
                            referencedTransaction.put("transaction",t);
                            temp.add(new RequestedData(requestWallet,personalInfo,creditInfo,referencedTransaction));
                        }
                    }

                }
            }
        }
        return temp;
    }

    public ArrayList<PersonalData> getPersonalData(JSONArray chain){
        ArrayList<PersonalData> temp = new ArrayList<>();
        for (String adress : receivedWalletData) {
            PersonalData p = new PersonalData();
            p.setDataFromJSON(chain, adress);
            temp.add(p);
        }
        return temp;
    }
}
