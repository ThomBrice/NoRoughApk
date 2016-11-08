package com.example.isen.noroughapk.activité_principale;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isen.noroughapk.Interfaces.ClickListenerFragment;
import com.example.isen.noroughapk.NavigationDrawer;
import com.example.isen.noroughapk.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {


   String[] ParGolfSart = new String[] { "4","3", "5","4","4","4","3","4","5","4","4","3","4","4","3","4","4","5" };

    private ClickListenerFragment listenerFragment;


    public ScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_score, container, false);

        listenerFragment=(NavigationDrawer) this.getActivity();

        FloatingActionButton nextHole = (FloatingActionButton) view.findViewById(R.id.nextHole);
        nextHole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNextHole(view);
            }
        });

        FloatingActionButton scorePlus= (FloatingActionButton) view.findViewById(R.id.scorePlus);
        scorePlus.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                changeScorePlus(view);
            }
        });


        return view;
    }


public void changeNextHole(View view){
    TextView trouTextView = (TextView) view.findViewById(R.id.numeroTrou);
    TextView scoreTextView = (TextView) view.findViewById(R.id.score);
    TextView parTextView = (TextView) view.findViewById(R.id.Par);

    int trouNumber = (Integer.parseInt(trouTextView.getText().toString()));
    int parNumber =(Integer.parseInt(ParGolfSart[trouNumber-1]));
    if(trouNumber < 18){
        trouTextView.setText("" + (trouNumber + 1));
        scoreTextView.setText("0");
        parTextView.setText(""+parNumber);

    }
}

    public void changeScorePlus(View view){

        TextView scoreTextView =(TextView) view.findViewById(R.id.score);

        int scoreNumber = (Integer.parseInt(scoreTextView.getText().toString()));
        scoreTextView.setText(""+(scoreNumber + 1));

    }


}
