package com.example.isen.noroughapk.BDD.realm;

import android.app.Activity;
import android.app.Application;

import com.example.isen.noroughapk.HistoryFragment;
import com.example.isen.noroughapk.HistoryScoreFragment;
import com.example.isen.noroughapk.fragment_partie_lancée.ScoreFragment;
import com.example.isen.noroughapk.BDD.model.Partie;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(HistoryFragment historyFragment){
      if(instance ==null){
          instance = new RealmController((historyFragment.getActivity().getApplication()));
      }
        return instance;
    }

    public static RealmController with(HistoryScoreFragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(ScoreFragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(Partie.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<Partie> getBooks() {

        return realm.where(Partie.class).findAll();
    }

    //query a single item with the given id
    public Partie getBook(String id) {

        return realm.where(Partie.class).equalTo("id", id).findFirst();
    }

    //check if Book.class is empty
    public boolean hasBooks() {

        return !realm.allObjects(Partie.class).isEmpty();
    }

    //query example
    public RealmResults<Partie> queryedBooks() {

        return realm.where(Partie.class)
                .contains("author", "Author 0")
                .or()
                .contains("datePartie", "Realm")
                .findAll();

    }
}