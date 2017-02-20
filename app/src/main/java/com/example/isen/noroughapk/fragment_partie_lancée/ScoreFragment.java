package com.example.isen.noroughapk.fragment_partie_lancée;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isen.noroughapk.BDD.model.Partie;
import com.example.isen.noroughapk.BDD.realm.RealmController;
import com.example.isen.noroughapk.Bluetooth.BluetoothLeService;
import com.example.isen.noroughapk.Interfaces.ClickListenerFragment;
import com.example.isen.noroughapk.Interfaces.TrouChangeListener;
import com.example.isen.noroughapk.NavigationDrawer;
import com.example.isen.noroughapk.R;
import com.example.isen.noroughapk.Weather.OpenWheatherMap;
import com.example.isen.noroughapk.Weather.WeatherCommon;
import com.example.isen.noroughapk.Weather.WeatherHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;

/**
 * Created by Thomas B & Thaddée K on 18/01/2017.
 */

public class ScoreFragment extends Fragment {

    private Realm realm;
    private ClickListenerFragment listenerFragment;
    Integer[] Score = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    Integer[] Handicap = new Integer[]{7, 3, 15, 11, 1, 9, 17, 13, 5, 8, 4, 16, 14, 2, 18, 10, 6, 12};
    Integer[] ParGolfSart = new Integer[]{4, 3, 5, 4, 4, 4, 3, 4, 5, 4, 4, 3, 4, 4, 3, 4, 4, 5};
    Integer[] newScore = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    String meteoIcon;
    int lecteurHandicap = 1, scoreTotal = 0;
    TrouChangeListener TrouChangeListener;
    boolean weatherRetreive = false;
    GetWeather weatherAsync;
    TextView numeroTrou, TxtThermometerText, TXtWindText, TxtTextWindRose, TxtTextPressure, TxtTextAtmospheric, TxtVille, TxtClub,scoreText, parText;
    OpenWheatherMap openWheatherMap;
    FloatingActionButton scorePlus;
    static double lat, lng;

    public ScoreFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_score, container, false);

        listenerFragment = (NavigationDrawer) this.getActivity();
        TrouChangeListener = (NavigationDrawer) this.getActivity();

        FloatingActionButton nextHole = (FloatingActionButton) view.findViewById(R.id.nextHole);
        nextHole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNextHole(view);
            }
        });

        scorePlus = (FloatingActionButton) view.findViewById(R.id.scorePlus);
        scorePlus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeScorePlus(view);
            }
        });

        FloatingActionButton abandonTrou = (FloatingActionButton) view.findViewById(R.id.dismiss);
        abandonTrou.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeAbandonTrou(view);
            }
        });

        FloatingActionButton scoreMoins = (FloatingActionButton) view.findViewById(R.id.scoremoins);
        scoreMoins.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeScoreMoins(view);
            }
        });

        // edit the TextView and call the Location fonction to get the Location value and then get Weather data
        TxtThermometerText = (TextView) view.findViewById(R.id.ThermometerText);
        TXtWindText = (TextView) view.findViewById(R.id.WindText);
        TxtTextWindRose = (TextView) view.findViewById(R.id.TextWindRose);
        TxtTextPressure = (TextView) view.findViewById(R.id.TextPressure);
        TxtTextAtmospheric = (TextView) view.findViewById(R.id.TextAtmospheric);
        TxtVille = (TextView) view.findViewById(R.id.TextVille);
        TxtClub = (TextView) view.findViewById(R.id.club);
        scoreText = (TextView) view.findViewById(R.id.score);
        parText = (TextView) view.findViewById(R.id.Par);
        numeroTrou = (TextView) view.findViewById(R.id.numeroTrou);

        weatherAsync = new GetWeather(TxtThermometerText, TXtWindText, TxtTextWindRose, TxtTextPressure, TxtTextAtmospheric, TxtVille);

        parText.setText(ParGolfSart[0].toString());
        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mGattUpdateReceiver);
    }

    public void changeNextHole(View view) {
        int trouNumber = (Integer.parseInt(numeroTrou.getText().toString()));
        if (trouNumber < 19) {
            int scoreNumber = (Integer.parseInt(scoreText.getText().toString()));
            Score[trouNumber - 1] = scoreNumber;
            if (trouNumber == 18) {
                setRealmData(Score);
                listenerFragment.ClickListener("sharePartie",1,scoreTotal,0);
            } else {
                int parNumber = (ParGolfSart[trouNumber]);
                numeroTrou.setText("" + (trouNumber + 1));
                scoreText.setText("0");
                parText.setText("" + parNumber);
            }
        }
    }

    public void changeScorePlus(View view) {
        TextView scoreTextView = (TextView) view.findViewById(R.id.score);
        int scoreNumber = (Integer.parseInt(scoreTextView.getText().toString()));
        scoreTextView.setText("" + (scoreNumber + 1));
    }

    public void changeScoreMoins(View view) {
        TextView scoreTextView = (TextView) view.findViewById(R.id.score);
        int scoreNumber = (Integer.parseInt(scoreTextView.getText().toString()));
        if (scoreNumber != 0) {
            scoreTextView.setText("" + (scoreNumber - 1));
        }
    }

    public void changeAbandonTrou(View view) {
        TextView scoreTextView = (TextView) view.findViewById(R.id.score);
        scoreTextView.setText("" + 10);
        changeNextHole(view);
    }

    private void setRealmData(Integer Carte[]) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        long id = 1 + System.currentTimeMillis();
        ArrayList<Partie> parties = new ArrayList<>();
        Partie partie = new Partie();
        setNewScore();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String golfName = sharedPref.getString("GolfPartie", "");
        partie.setMeteoIcon(meteoIcon);
        partie.setParcour(golfName);
        partie.setId((int) (id));
        partie.setDatePartie("" + date);
        partie.setScore1(Carte[0]);
        partie.setScore2(Carte[1]);
        partie.setScore3(Carte[2]);
        partie.setScore4(Carte[3]);
        partie.setScore5(Carte[4]);
        partie.setScore6(Carte[5]);
        partie.setScore7(Carte[6]);
        partie.setScore8(Carte[7]);
        partie.setScore9(Carte[8]);
        partie.setScore10(Carte[9]);
        partie.setScore11(Carte[10]);
        partie.setScore12(Carte[11]);
        partie.setScore13(Carte[12]);
        partie.setScore14(Carte[13]);
        partie.setScore15(Carte[14]);
        partie.setScore16(Carte[15]);
        partie.setScore17(Carte[16]);
        partie.setScore18(Carte[17]);
        partie.setScore("" + scoreTotal);
        parties.add(partie);

        for (Partie b : parties) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();
        }
    }

    public void setNewScore() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Float TextHandicap = sharedPref.getFloat("Handicap", 54.0f);
        int handicap = Math.round(TextHandicap);
        while (handicap != 0) {
            if (handicap == 54) {
                for (int i = 0; i < 18; i++) {
                    newScore[i] = Score[i] - 3;
                }
                handicap = handicap - 54;
            }
            if (handicap >= 36) {
                for (int i = 0; i < 18; i++) {
                    newScore[i] = Score[i] - 2;
                }
                handicap = handicap - 36;
            }

            if (handicap >= 18) {
                for (int i = 0; i < 18; i++) {
                    newScore[i] = Score[i] - 1;
                }
                handicap = handicap - 18;
            } else {
                for (int j = 0; j < handicap; j++) {
                    for (int i = 0; i < 18; i++) {
                        if (lecteurHandicap == Handicap[i]) {
                            newScore[i] = Score[i] - 1;
                            lecteurHandicap++;
                            handicap=handicap-1;
                            if (handicap==0){
                                break;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 18; i++) {
            int diff = newScore[i] - ParGolfSart[i];
            switch (diff) {
                case 0:
                    scoreTotal += 2;
                    break;
                case 1:
                    scoreTotal += 1;
                    break;
                case 2:
                    scoreTotal +=0;
                    break;
                case 3:
                    scoreTotal +=0;
                    break;
                case -1:
                    scoreTotal += 3;
                    break;
                case -2:
                    scoreTotal += 4;
                    break;
                case -3:
                    scoreTotal += 5;
                    break;
                case -4:
                    scoreTotal += 6;
                    break;
                default:
                    break;
            }
        }
        scoreTotal +=0;
    }

    private class GetWeather extends AsyncTask<String, Void, String> {

        TextView TxtThermometerText, TXtWindText, TxtTextWindRose, TxtTextPressure, TxtTextAtmospheric, TxtVille;

        public GetWeather(TextView txtThermometerText, TextView TXtWindText, TextView txtTextWindRose, TextView txtTextPressure, TextView txtTextAtmospheric, TextView txtVille) {
            TxtThermometerText = txtThermometerText;
            this.TXtWindText = TXtWindText;
            TxtTextWindRose = txtTextWindRose;
            TxtTextPressure = txtTextPressure;
            TxtTextAtmospheric = txtTextAtmospheric;
            TxtVille = txtVille;
        }

        public void setTxtThermometerText(TextView txtThermometerText) {
            TxtThermometerText = txtThermometerText;
        }

        public void setTXtWindText(TextView TXtWindText) {
            this.TXtWindText = TXtWindText;
        }

        public void setTxtTextWindRose(TextView txtTextWindRose) {
            TxtTextWindRose = txtTextWindRose;
        }

        public void setTxtTextPressure(TextView txtTextPressure) {
            TxtTextPressure = txtTextPressure;
        }

        public void setTxtTextAtmospheric(TextView txtTextAtmospheric) {
            TxtTextAtmospheric = txtTextAtmospheric;
        }

        public void setTxtVille(TextView txtVille) {
            TxtVille = txtVille;
        }

        @Override
        protected String doInBackground(String... params) {
            String stream = null;
            String urlString = params[0]; // we get the URL
            WeatherHelper http = new WeatherHelper();
            return stream = http.getHTTPData(urlString); // URL need to get the weather data
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            Type myType = new TypeToken<OpenWheatherMap>() {
            }.getType();
            openWheatherMap = gson.fromJson(s, myType); // we get all the data from the URL on a Gson Filz

            // We set all the data we needTxtVille.setText(String.valueOf(openWheatherMap.getName()));
            TXtWindText.setText(String.valueOf(openWheatherMap.getWind().getSpeed()) + " m/s");
            TxtTextWindRose.setText(String.valueOf(openWheatherMap.getWind().getDeg()) + " °");
            TxtThermometerText.setText(String.format("%.1f °C", openWheatherMap.getMainWeather().getTemp() - 273, 15 + " °"));
            TxtTextPressure.setText(String.valueOf(openWheatherMap.getMainWeather().getPressure()) + " hPa");
            TxtTextAtmospheric.setText(String.valueOf(openWheatherMap.getMainWeather().getHumidity()) + " %");
            TxtVille.setText(String.valueOf(openWheatherMap.getName()));
            meteoIcon = String.valueOf(openWheatherMap.getWeather().get(0).getMain());
        }
    }

    public void setTextStart(Double latitude, Double longitude) {
        if (!weatherRetreive) {
            weatherRetreive = true;
            this.lat = latitude;
            this.lng = longitude;
            lat = Math.round((lat * 100));
            lat = lat / 100;
            lng = Math.round((lng * 100));
            lng = lng / 100;
            weatherAsync.execute(WeatherCommon.apiRequest(String.valueOf(lat), String.valueOf(lng)));
        }
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_DATA_VERIFIED.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_VERIFIED);
        return intentFilter;
    }

    private void displayData(String data){
        switch (data){
            case "1:0/":
                TxtClub.setText(R.string.bois_1);
                break;
            case "1:1/":
                TxtClub.setText(R.string.bois_2);
                break;
            case "1:2/":
                TxtClub.setText(R.string.bois_3);
                break;
            case "1:3/":
                TxtClub.setText(R.string.bois_4);
                break;
            case "1:4/":
                TxtClub.setText(R.string.bois_5);
                break;
            case "1:5/":
                TxtClub.setText(R.string.fer_1);
                break;
            case "1:6/":
                TxtClub.setText(R.string.fer_2);
                break;
            case "1:7/":
                TxtClub.setText(R.string.fer_3);
                break;
            case "1:8/":
                TxtClub.setText(R.string.fer_4);
                break;
            case "1:9/":
                TxtClub.setText(R.string.fer_5);
                break;
            case "1:10/":
                TxtClub.setText(R.string.fer_6);
                break;
            case "1:11/":
                TxtClub.setText(R.string.fer_7);
                break;
            case "1:12/":
                TxtClub.setText(R.string.fer_8);
                break;
            case "1:13/":
                TxtClub.setText(R.string.fer_9);
                break;
            case "1:14/":
                TxtClub.setText(R.string.chipper);
                break;
            case "1:15/":
                TxtClub.setText(R.string.w48);
                break;
            case "1:16/":
                TxtClub.setText(R.string.w52);
                break;
            case "1:17/":
                TxtClub.setText(R.string.w56);
                break;
            case "1:18/":
                TxtClub.setText(R.string.w60);
                break;
            case "1:19/":
                TxtClub.setText(R.string.w64);
                break;
            case "2:0/":
                scorePlus.performClick();
                break;
            default:
                break;
        }
    }
}


