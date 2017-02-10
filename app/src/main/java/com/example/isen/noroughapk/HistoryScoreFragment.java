package com.example.isen.noroughapk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.isen.noroughapk.BDD.adapters.CarteAdapter;
import com.example.isen.noroughapk.BDD.model.Partie;
import com.example.isen.noroughapk.BDD.realm.RealmController;

import java.util.ArrayList;

import io.realm.Realm;


public class HistoryScoreFragment extends Fragment {

    private ArrayList trous;
    private ArrayList handicap;
    private ArrayList score;
    private RecyclerView recycler;
    private Realm realm;


    Integer[] Score = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    Integer[] Handicap = new Integer[]{7, 3, 15, 11, 1, 9, 17, 13, 5, 8, 4, 16, 14, 2, 18, 10, 6, 12};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentHistoryScoreView = inflater.inflate(R.layout.fragment_history_score, container, false);


        int id = getArguments().getInt("ID", 0);
        this.realm = RealmController.with(this).getRealm();

        Partie myPartie =realm.where(Partie.class).equalTo("id",id).findFirst();
        Score[0]=myPartie.getScore1();
        Score[1]=myPartie.getScore2();
        Score[2]=myPartie.getScore3();
        Score[3]=myPartie.getScore4();
        Score[4]=myPartie.getScore5();
        Score[5]=myPartie.getScore6();
        Score[6]=myPartie.getScore7();
        Score[7]=myPartie.getScore8();
        Score[8]=myPartie.getScore9();
        Score[9]=myPartie.getScore10();
        Score[10]=myPartie.getScore11();
        Score[11]=myPartie.getScore12();
        Score[12]=myPartie.getScore13();
        Score[13]=myPartie.getScore14();
        Score[14]=myPartie.getScore15();
        Score[15]=myPartie.getScore16();
        Score[16]=myPartie.getScore17();
        Score[17]=myPartie.getScore18();


        recycler = (RecyclerView) fragmentHistoryScoreView.findViewById(R.id.recyclerHistoryScore);
        initViews();

        return fragmentHistoryScoreView;
    }

    private void initViews() {


        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        trous = new ArrayList<>();
        handicap = new ArrayList<>();
        score = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            int trou = i + 1;
            trous.add("" + trou);
            handicap.add("" + Handicap[i]);
            if(Score[i]!=10) {
                score.add("" + Score[i]);
            }
            else{score.add("X");}
        }
        RecyclerView.Adapter CarteAdapter = new CarteAdapter(trous, handicap,score, (NavigationDrawer) this.getActivity());
        recycler.setAdapter(CarteAdapter);


    }
}