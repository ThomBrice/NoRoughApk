package com.example.isen.noroughapk;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.isen.noroughapk.Interfaces.ClickListenerFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {
    private ClickListenerFragment listenerFragment;

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        listenerFragment=(NavigationDrawer) this.getActivity();

        FloatingActionButton playButton = (FloatingActionButton) view.findViewById(R.id.start_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerFragment.ClickListener("play");
            }
        });

        return view;
    }

}
