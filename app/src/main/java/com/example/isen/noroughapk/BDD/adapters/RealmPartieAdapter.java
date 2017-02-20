package com.example.isen.noroughapk.BDD.adapters;

import android.content.Context;
import io.realm.RealmResults;

import com.example.isen.noroughapk.BDD.model.Partie;

/**
 * Created by Thaddée on 09/11/2016.
 */

public class RealmPartieAdapter extends RealmModelAdapter<Partie>  {
    public RealmPartieAdapter(Context context, RealmResults<Partie> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}
