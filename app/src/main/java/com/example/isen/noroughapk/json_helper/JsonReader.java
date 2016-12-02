package com.example.isen.noroughapk.json_helper;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.example.isen.noroughapk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by Thomas B on 29/11/2016.
 */

public class JsonReader extends AsyncTask<Void, Integer, Void> implements Parcelable {

    Context context;
    List<String> golfNames = new ArrayList<>();
    ArrayList<HashMap<String, String>> coordonneesList;

    public ArrayList<HashMap<String, String>> getCoordonneesList() {
        return coordonneesList;
    }

    public List<String> getGolfNames() {
        return golfNames;
    }

    public JsonReader (Context context){
       this.context = context;
    }

    protected JsonReader(Parcel in) {
        golfNames = in.createStringArrayList();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        JSONObject jsonObject;
        jsonObject = parseJSONData(context);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
    }

    //Method that will parse the JSON file and will return a JSONObject
    public JSONObject parseJSONData(Context context) {
        coordonneesList = new ArrayList<>();
        String jsonString = null;
        JSONObject jsonObject = null;
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

            jsonString = new String(bytes, "UTF-8");
            jsonObject = new JSONObject(jsonString);

            Iterator<String> iter = jsonObject.keys(); // récupérer chaque nom d'objet principal
            while (iter.hasNext()) {
                String key = iter.next();
                golfNames.add(key);
            }

            // getting Json Array Node
            JSONArray golfSart = jsonObject.getJSONArray("Golf du Sart");
            for(int i=0; i< golfSart.length();i++){
                JSONObject trous = golfSart.getJSONObject(i);
                for (int u=0; u<trous.length();u++) {
                    JSONObject trou = trous.getJSONObject(String.valueOf(u+1));

                    String trouNum = String.valueOf(i);
                    String latS = trou.getString("LatS");
                    String lonS = trou.getString("LonS");
                    String latM = trou.getString("LatM");
                    String lonM = trou.getString("LonM");
                    String latE = trou.getString("LatE");
                    String lonE = trou.getString("LonE");

                    // tmp hash map for single contact
                    HashMap<String, String> coordonnees = new HashMap<>();

                    // adding each child node to HashMap key => value
                    coordonnees.put("trouNum", trouNum);
                    coordonnees.put("LatS", latS);
                    coordonnees.put("LonS", lonS);
                    coordonnees.put("LatM", latM);
                    coordonnees.put("LonM", lonM);
                    coordonnees.put("LatE", latE);
                    coordonnees.put("LonE", lonE);

                    // adding contact to contact list
                    coordonneesList.add(coordonnees);
                }
                }




        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException x) {
            x.printStackTrace();
            return null;
        }
        return jsonObject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(golfNames);
    }

    public static final Creator<JsonReader> CREATOR = new Creator<JsonReader>() {
        @Override
        public JsonReader createFromParcel(Parcel in) {
            return new JsonReader(in);
        }

        @Override
        public JsonReader[] newArray(int size) {
            return new JsonReader[size];
        }
    };
}
