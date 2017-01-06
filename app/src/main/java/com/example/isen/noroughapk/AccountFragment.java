package com.example.isen.noroughapk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.isen.noroughapk.Class.Player;
import com.example.isen.noroughapk.json_helper.JsonReader;

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


        listGolf =  (ListView) view.findViewById(R.id.list_golf);
        firstName = (EditText) view.findViewById(R.id.firstname);
        surName = (EditText) view.findViewById(R.id.surname);
        handicap = (EditText) view.findViewById(R.id.handicap);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String TextfirstName= sharedPref.getString("FirstName","");
        String TextSurname = sharedPref.getString("Surname","");
        Float TextHandicap = sharedPref.getFloat("Handicap", 54.0f);



        bundle = this.getArguments();
        if(bundle !=null){
            jsonReader = bundle.getParcelable("nomsGolf");

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext()
                ,android.R.layout.simple_expandable_list_item_1
                ,jsonReader.getGolfNames());

        firstName.setText(TextfirstName);
        surName.setText(TextSurname);
        handicap.setText(TextHandicap.toString());

        listGolf.setAdapter(adapter);


    }

    @Override
    public void onPause() {
        super.onPause();


       String FirstName = firstName.getText().toString();
        String Surname = surName.getText().toString();
        Float  Handicap = Float.parseFloat(handicap.getText().toString());



        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("FirstName", FirstName);
        editor.putString("Surname", Surname);
        editor.putFloat("Handicap",Handicap);
        editor.commit();


    }
}
