package com.example.isen.noroughapk;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.isen.noroughapk.BDD.adapters.PartiesAdapter;
import com.example.isen.noroughapk.BDD.adapters.RealmPartieAdapter;
import com.example.isen.noroughapk.BDD.model.Partie;
import com.example.isen.noroughapk.BDD.realm.RealmController;

import io.realm.Realm;
import io.realm.RealmResults;

public class HistoryFragment extends Fragment {



    private PartiesAdapter adapter;
    private Realm realm;
    private RecyclerView recycler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentHistoryView = inflater.inflate(R.layout.fragment_history, container, false);

        recycler = (RecyclerView) fragmentHistoryView.findViewById(R.id.recyclerHistory);
        this.realm = RealmController.with(this).getRealm();

        setupRecycler();
        RealmController.with(this).refresh();
       setRealmAdapter(RealmController.with(this).getBooks());


        return fragmentHistoryView;
    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        adapter = new PartiesAdapter(getContext());
        recycler.setAdapter(adapter);
    }



    public void setRealmAdapter(RealmResults<Partie> books) {

        RealmPartieAdapter realmAdapter = new RealmPartieAdapter(getContext(), books, true);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }


}
