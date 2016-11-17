package com.example.isen.noroughapk.BDD.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isen.noroughapk.R;

import java.util.ArrayList;


public class CarteAdapter extends RecyclerView.Adapter<CarteAdapter.ViewHolder> {
    private ArrayList<String> trou;
    private ArrayList<String> handicap;
    private ArrayList<String> score;

    public CarteAdapter(ArrayList<String> trou,ArrayList<String> handicap,ArrayList<String>score) {
        this.trou = trou;
        this.score = score;
        this.handicap=handicap;
    }

    @Override
    public CarteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cartes, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarteAdapter.ViewHolder viewHolder, int i) {

        viewHolder.numeroTrou.setText(trou.get(i));
        viewHolder.scoreTrou.setText(score.get(i));
        viewHolder.handicapTrou.setText(handicap.get(i));
    }

    @Override
    public int getItemCount() {
        return trou.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numeroTrou;
        private TextView scoreTrou;
        private TextView handicapTrou;

        public ViewHolder(View view) {
            super(view);

            numeroTrou = (TextView) view.findViewById(R.id.numeroTrou);
            handicapTrou = (TextView) view.findViewById(R.id.handicapTrou);
            scoreTrou = (TextView) view.findViewById(R.id.scoreTrou);
        }
    }

}
