package com.example.isen.noroughapk;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.example.isen.noroughapk.Interfaces.ClickListenerFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

public class StartFragment extends Fragment {
    TextView surname;
    private BluetoothAdapter bluetoothAdapter;
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 1;

    private ClickListenerFragment listenerFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        listenerFragment=(NavigationDrawer) this.getActivity();

        surname = (TextView) rootView.findViewById(R.id.surname);

        CardView playButton = (CardView) rootView.findViewById(R.id.start_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerFragment.ClickListener("chooseGolf");
            }
        });
        CardView historiqueButton = (CardView) rootView.findViewById(R.id.historique_button);
        historiqueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerFragment.ClickListener("historique");
            }
        });
        CardView statistiquesButton = (CardView) rootView.findViewById(R.id.statistiques_button);
        statistiquesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerFragment.ClickListener("statistiques");
            }
        });
        CardView analyseSwingButton = (CardView) rootView.findViewById(R.id.analyse_button);
        analyseSwingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerFragment.ClickListener("analyseSwing");
            }
        });
        ImageView accountPicture = (ImageView) rootView.findViewById(R.id.account_picture);
        accountPicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listenerFragment.ClickListener("goToAccount");
            }
        });

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        Switch bluetoothSwitch = (Switch) rootView.findViewById(R.id.bluetootSwitch);
        if (bluetoothAdapter.isEnabled())
            bluetoothSwitch.setChecked(true);
        else
            bluetoothSwitch.setChecked(false);

        bluetoothSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BLUETOOTH);
                }else{
                    bluetoothAdapter.disable();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String TextSurname = sharedPref.getString("Surname", "");
        if (!TextSurname.equals(""))
            surname.setText(TextSurname);
        else
        surname.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
