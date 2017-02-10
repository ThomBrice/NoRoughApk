package com.example.isen.noroughapk.BDD.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.isen.noroughapk.Interfaces.ClickListenerFragment;
import com.example.isen.noroughapk.NavigationDrawer;
import com.example.isen.noroughapk.R;

import java.util.ArrayList;




public class CarteAdapter extends RecyclerView.Adapter<CarteAdapter.ViewHolder>  {
    private final NavigationDrawer activity;
    private ArrayList<String> trou;
    private ArrayList<String> handicap;
    private ArrayList<String> score;
 
    private ClickListenerFragment listenerFragment;


    public CarteAdapter(ArrayList<String> trou,ArrayList<String> handicap,ArrayList<String>score, NavigationDrawer activity) {
        this.trou = trou;
        this.score = score;
        this.handicap=handicap;
        this.activity=activity;

    }

    @Override
    public CarteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        listenerFragment = this.activity  ;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cartes, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CarteAdapter.ViewHolder viewHolder, int i) {


        viewHolder.numeroTrou.setText(trou.get(i));
        viewHolder.scoreTrou.setText(score.get(i));
        viewHolder.handicapTrou.setText(handicap.get(i));



        viewHolder.shareScoreTrou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=  viewHolder.getAdapterPosition()+1;
                int scoreTotal=0;
                String data = score.get(position).toString();
                if(data!=null && !data.equals("X")){
                    scoreTotal= Integer.parseInt(data);
                }

                listenerFragment.ClickListener("sharePartie",2,scoreTotal,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trou.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numeroTrou;
        private TextView scoreTrou;
        private TextView handicapTrou;
        private Button  shareScoreTrou;

        public ViewHolder(View view) {
            super(view);

            numeroTrou = (TextView) view.findViewById(R.id.numeroTrou);
            handicapTrou = (TextView) view.findViewById(R.id.handicapTrou);
            scoreTrou = (TextView) view.findViewById(R.id.scoreTrou);
            shareScoreTrou =(Button) view.findViewById(R.id.shareAHole);

        }
    }



}
