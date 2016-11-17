package com.example.isen.noroughapk.BDD.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.isen.noroughapk.BDD.model.Partie;
import com.example.isen.noroughapk.BDD.realm.RealmController;
import com.example.isen.noroughapk.Interfaces.ClickListenerFragment;
import com.example.isen.noroughapk.NavigationDrawer;
import com.example.isen.noroughapk.R;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Thaddée on 09/11/2016.
 */

public class PartiesAdapter extends RealmRecyclerViewAdapter<Partie> {
    final Context context;
    private Realm realm;
    private LayoutInflater inflater;
    private ClickListenerFragment listenerFragment;


    public PartiesAdapter(Context context) {

        this.context = context;
    }


    // create new views (invoked by the layout manager)
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        listenerFragment = (NavigationDrawer) this.context;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parties, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        realm = RealmController.getInstance().getRealm();
        // get the article

        final Partie partie = getItem(position);
        // cast the generic view holder to our specific one
        final CardViewHolder holder = (CardViewHolder) viewHolder;

        // set the title and the snippet
        holder.textDatePartie.setText(partie.getDatePartie());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = partie.getId();
                listenerFragment.ClickListener("goToHistoryScoreFragment",id);
            }

        });

        //remove single match from realm
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View content = inflater.inflate(R.layout.item_parties, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(content)
                        .setTitle("Supprimer?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RealmResults<Partie> results = realm.where(Partie.class).findAll();

                                // Get the book title to show it in toast message
                                Partie b = results.get(position);

                                // All changes to data must happen in a transaction
                                realm.beginTransaction();

                                // remove single match
                                results.remove(position);
                                realm.commitTransaction();

                                notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });


    }

    // return the size of your data set (invoked by the layout manager)
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }


    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public CardView card;
        public TextView textDatePartie;

        public CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.card_parties);
            textDatePartie = (TextView) itemView.findViewById(R.id.date_partie);


        }
    }


}
