package com.example.isen.noroughapk.Weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Thaddée on 16/12/2016.
 */

public class WeatherHelper {
    static String stream = null;

    public WeatherHelper(){

    }

    public String getHTTPData(String urlString){
        try {
            URL url =new URL(urlString);
            HttpURLConnection httpsURLConnection =(HttpURLConnection)url.openConnection();
            if(httpsURLConnection.getResponseCode() == 200) // OK=200;
            {
                BufferedReader r =new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = r.readLine())!=null)
                    sb.append(line);
                stream = sb.toString();
                httpsURLConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }
}
