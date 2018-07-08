package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by alasdair on 07.07.18.
 */
public class Utility {

    public String JSONFormatter(KeyValueTuple[] list){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (KeyValueTuple tuple : list) {
            sb.append("\"");
            sb.append(tuple.getKey());
            sb.append("\": \"");
            sb.append(tuple.getValue());
            sb.append("\",");
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean isValidURL(String url) {

        URL u = null;

        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }

        try {
            u.toURI();
        } catch (URISyntaxException e) {
            return false;
        }

        return true;
    }

    public JSONArray fileToJSON(String path) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String jsonString = "";
        JSONArray jsonArray = null;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            jsonString = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (!jsonString.equals("")){
            JSONParser parser = new JSONParser();
            if (jsonString.substring(0,1).equals("[")){
                try {
                    jsonArray = (JSONArray) parser.parse(jsonString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
                    jsonArray = (JSONArray) jsonObject.get("chain");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
        return jsonArray;
    }

    public boolean parseBoolean(Long l){
        if (l == 1)
            return true;
        else
            return false;
    }

}