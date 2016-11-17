package com.example.isen.noroughapk.BDD.adapters;

import android.content.Context;

import com.example.isen.noroughapk.BDD.model.Partie;

import io.realm.RealmResults;

/**
 * Created by Thaddée on 09/11/2016.
 */

public class RealmPartieAdapter extends RealmModelAdapter<Partie>  {
    public RealmPartieAdapter(Context context, RealmResults<Partie> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}
