package com.example.isen.noroughapk.fragment_partie_lancée;


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
    private ClickListenerFragment listenerFragment;
    Integer[] Score = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    Integer[] Handicap = new Integer[]{7, 3, 15, 11, 1, 9, 17, 13, 5, 8, 4, 16, 14, 2, 18, 10, 6, 12};
    Integer[] ParGolfSart = new Integer[]{4, 3, 5, 4, 4, 4, 3, 4, 5, 4, 4, 3, 4, 4, 3, 4, 4, 5};
    int handicap = 18;
    Integer[] newScore = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int lecteurHandicap = 1;
    int scoreTotal = 0;

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
        if (trouNumber < 19) {
            int scoreNumber = (Integer.parseInt(scoreTextView.getText().toString()));
            Score[trouNumber - 1] = scoreNumber;
            if (trouNumber == 18) {
                setRealmData(Score);
                listenerFragment.ClickListener("goToHistory");



            }else {
                int parNumber = (ParGolfSart[trouNumber]);

                trouTextView.setText("" + (trouNumber + 1));
                scoreTextView.setText("0");
                parTextView.setText("" + parNumber);
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
        setNewScore();


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

    public void setNewScore() {
        while (handicap != 0) {

            if (handicap <= 18) {
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
    }
}


