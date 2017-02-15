package com.example.isen.noroughapk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.isen.noroughapk.Interfaces.ClickListenerFragment;
import com.example.isen.noroughapk.json_helper.JsonReader;


public class GetGolf extends Fragment {
    public View view;
    JsonReader jsonReader;
    Bundle bundle;

    private ClickListenerFragment listenerFragment;

    ListView listGolf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_get_golf, container, false);
        listenerFragment = (NavigationDrawer) this.getActivity();
        onResume();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        listGolf = (ListView) view.findViewById(R.id.list_golf);


        bundle = this.getArguments();
        if (bundle != null) {
            jsonReader = bundle.getParcelable("nomsGolf");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext()
                    , android.R.layout.simple_expandable_list_item_1
                    , jsonReader.getGolfNames());
            listGolf.setAdapter(adapter);
        }


        listGolf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listGolf.getSelectedItemPosition();
                String golfName = listGolf.getItemAtPosition(position).toString();
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("NomGolf", golfName);
                editor.commit();
                listenerFragment.ClickListener("goToAccount");
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }





}
