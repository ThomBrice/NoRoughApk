package com.example.isen.noroughapk.activité_principale;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isen.noroughapk.BDD.model.Partie;
import com.example.isen.noroughapk.Interfaces.ClickListenerFragment;
import com.example.isen.noroughapk.NavigationDrawer;
import com.example.isen.noroughapk.R;
import com.example.isen.noroughapk.BDD.adapters.PartiesAdapter;
import com.example.isen.noroughapk.BDD.realm.RealmController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {
    private Realm realm;
    private PartiesAdapter adapter;
    String[] ParGolfSart = new String[]{"4", "3", "5", "4", "4", "4", "3", "4", "5", "4", "4", "3", "4", "4", "3", "4", "4", "5"};
    private ClickListenerFragment listenerFragment;
    Integer[] Score = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public ScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_score, container, false);

        listenerFragment = (NavigationDrawer) this.getActivity();

        FloatingActionButton nextHole = (FloatingActionButton) view.findViewById(R.id.nextHole);
        nextHole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNextHole(view);
            }
        });

        FloatingActionButton scorePlus = (FloatingActionButton) view.findViewById(R.id.scorePlus);
        scorePlus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeScorePlus(view);
            }
        });

        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        return view;
    }


    public void changeNextHole(View view) {
        TextView trouTextView = (TextView) view.findViewById(R.id.numeroTrou);
        TextView scoreTextView = (TextView) view.findViewById(R.id.score);
        TextView parTextView = (TextView) view.findViewById(R.id.Par);

        int trouNumber = (Integer.parseInt(trouTextView.getText().toString()));
        int parNumber = (Integer.parseInt(ParGolfSart[trouNumber - 1]));
        if (trouNumber < 18) {
            int scoreNumber = (Integer.parseInt(scoreTextView.getText().toString()));
            Score[trouNumber - 1] = scoreNumber;
            trouTextView.setText("" + (trouNumber + 1));
            scoreTextView.setText("0");
            parTextView.setText("" + parNumber);


        } else {
            if (trouNumber == 18) {
                setRealmData(Score);
                listenerFragment.ClickListener("goToHistory");
            }

        }
    }

    public void changeScorePlus(View view) {

        TextView scoreTextView = (TextView) view.findViewById(R.id.score);

        int scoreNumber = (Integer.parseInt(scoreTextView.getText().toString()));
        scoreTextView.setText("" + (scoreNumber + 1));

    }

    private void setRealmData(Integer Carte[]) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        long id = 1 + System.currentTimeMillis();
        ArrayList<Partie> parties = new ArrayList<>();
        Partie partie = new Partie();

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

        partie.setHandicap("10");
        partie.setTrou("10");
        parties.add(partie);


        for (Partie b : parties) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();


        }

    }

}
