package com.example.isen.noroughapk.json_helper;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.isen.noroughapk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static java.security.AccessController.getContext;

/**
 * Created by Thomas B on 29/11/2016.
 */

public class JsonReader extends AsyncTask<Void, Integer, Void> {

    Context context;

    public JsonReader (Context context){
       this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context, "Début du traitement Asynchrone", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        JSONObject jsonObject;
        jsonObject = parseJSONData(context);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Toast.makeText(context, "Le traitement asynchrone est terminé", Toast.LENGTH_LONG).show();
    }

    //Method that will parse the JSON file and will return a JSONObject
    public JSONObject parseJSONData(Context context) {
        String JSonString = null;
        JSONObject JSonObject = null;
        try {

            //open the inputStream to the file
            InputStream inputStream = context.getAssets().open("coordonates.json");

            int sizeOfJSONFile = inputStream.available();

            //array that will store all the data
            byte[] bytes = new byte[sizeOfJSONFile];

            //reading data into the array from the file
            inputStream.read(bytes);

            //close the input stream
            inputStream.close();

            JSonString = new String(bytes, "UTF-8");
            JSonObject = new JSONObject(JSonString);

            //JSonObject.getJSONObject();

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException x) {
            x.printStackTrace();
            return null;
        }
        return JSonObject;
    }

}
