
package com.example.isen.noroughapk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.isen.noroughapk.Interfaces.ClickListenerFragment;


public class AmisFragment extends Fragment {
    private ClickListenerFragment listenerFragment;
    View view;
    String textToShare="";

    boolean messageShare = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_amis, container, false);
        listenerFragment = (NavigationDrawer) this.getActivity();

        int score= getArguments().getInt("score", 0);
        int from= getArguments().getInt("from",0);

        if(from==1) {
            textToShare = "Bonjour je viens de faire " + score + " en Stableford essayes de faire mieux! #NoRough";
        }
        else{
            int position = getArguments().getInt("position",0);
            textToShare = "Bonjour je viens de faire " + score + " sur le trou " + position + " essayes de faire mieux! #NoRough";
        }
        Button buttonShare = (Button) view.findViewById(R.id.buttonShare);
        Button buttonSkip = (Button) view.findViewById(R.id.buttonSkip);

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageShare = true;
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
                startActivity(Intent.createChooser(sharingIntent, "Share text using"));


            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerFragment.ClickListener("goToHistory");
            }
        });
        return (view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(messageShare){
             listenerFragment.ClickListener("goToHistory");
        }
    }
}
