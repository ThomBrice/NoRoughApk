package com.example.isen.noroughapk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.isen.noroughapk.json_helper.JsonReader;
import com.example.isen.noroughapk.Class.Player;

/**
 * Created by Thomas B on 30/11/2016.
 */

public class AccountFragment extends Fragment {

    public View view;
    JsonReader jsonReader;
    Bundle bundle;
    Player player;

    ListView listGolf;
    EditText firstName;
    EditText surName;
    EditText handicap;

    public AccountFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_account,container,false);

        onResume();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        player = new Player();

        listGolf =  (ListView) view.findViewById(R.id.list_golf);
        firstName = (EditText) view.findViewById(R.id.firstname);
        surName = (EditText) view.findViewById(R.id.surname);
        handicap = (EditText) view.findViewById(R.id.handicap);

        bundle = this.getArguments();
        if(bundle !=null){
            jsonReader = bundle.getParcelable("nomsGolf");
            player = bundle.getParcelable("Player");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext()
                ,android.R.layout.simple_expandable_list_item_1
                ,jsonReader.getGolfNames());
        listGolf.setAdapter(adapter);

        firstName.setText(player.getFirstname());
        surName.setText(player.getSurname());
        if(player.getHandicap() > -1.0){
            handicap.setText(player.getHandicap().toString());}
        listGolf.setSelection(player.getPositionGolf());
    }

    @Override
    public void onPause() {
        super.onPause();
        player = new Player();

        if (listGolf.getSelectedItemPosition() > -1) {
            player.setGolfDeReference(listGolf.getSelectedItem().toString());
            player.setPositionGolf(listGolf.getSelectedItemPosition());
        }
        if (!firstName.getText().toString().equals("")) {
            player.setFirstname(firstName.getText().toString());
        }
        if (!surName.getText().toString().equals("")) {
            player.setSurname(surName.getText().toString());
        }
        if (!handicap.getText().toString().equals("")) {
            player.setHandicap(Float.parseFloat(handicap.getText().toString()));
        }

        Intent intent = new Intent(getActivity(), NavigationDrawer.class);
        intent.putExtra("PlayerFromFrag", player);
        startActivity(intent);

    }
}
