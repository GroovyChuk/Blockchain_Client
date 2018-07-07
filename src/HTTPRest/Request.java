package HTTPRest;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
/**
 * Created by alasdair on 22.01.18.
 */
public class Request {

    public JSONObject getURL(URL url){
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
            jsonObject = (JSONObject)jsonParser.parse(
                    new InputStreamReader(inputStream, "UTF-8"));


            conn.disconnect();
        } catch (Exception e){

        }

        return jsonObject;
    }
}
