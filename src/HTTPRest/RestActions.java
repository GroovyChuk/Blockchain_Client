package HTTPRest;

import main.*;
import okhttp3.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * Created by alasdair on 22.01.18.
 */
public class RestActions {

    public interface DBRequestListener {
        public void onDBRequestFinished(String response);
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    public String postContactInfo(String sender, JSONObject data){
        String reply = "";
        JSONObject object = new JSONObject();
        try {
            object.put("sender",sender);
            object.put("recipient",0);
            object.put("data",data);
            System.out.println(object.toString());
            reply = new RestActions().post(App.clientIP + "/create-transaction", object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reply;
    }

    public String requestData(String sender, String recipient, JSONObject data){
        String reply = "";
        JSONObject object = new JSONObject();
        try {
            object.put("sender",sender);
            object.put("recipient",recipient);
            object.put("data",data);
            System.out.println(object.toString());
            reply = new RestActions().post(App.CompanyIP + "/create-transaction", object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reply;
    }

    public String shareInfo(String sender, String recipient, JSONObject data){
        String reply = "";
        JSONObject object = new JSONObject();
        try {
            object.put("sender",sender);
            object.put("recipient",recipient);
            object.put("data",data);
            System.out.println(object.toString());
            reply = new RestActions().post(App.clientIP + "/create-transaction", object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reply;
    }


    public String registerNode(String address, String url){
        String reply = "";
        JSONObject object = new JSONObject();
        try {
            object.put("address",address);
            reply = new RestActions().post(url + "/register-node", object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reply;
    }

    public String encrypt(RequestData request, int privatePersonal, int privateCredit){
        String reply = "";

        JSONObject object = new JSONObject();
        try {
            object.put("key",request.getPublicKey());
            object.put("privatePersonal",privatePersonal);
            object.put("privateCredit",privateCredit);
            reply = new RestActions().post(App.clientIP + "/encrypt", object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reply;
    }

    public JSONObject getURL(URL url) {

        JSONObject jsonObject = new JSONObject();
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            InputStream inputStream = conn.getInputStream();
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(
                    new InputStreamReader(inputStream, "UTF-8"));


            conn.disconnect();
        } catch (Exception e) {

        }

        return jsonObject;
    }

    public String post(String url, JSONObject json) throws IOException {
        RequestBody body = RequestBody.create(JSON,json.toString());
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
