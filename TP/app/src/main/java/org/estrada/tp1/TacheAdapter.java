package org.estrada.tp1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TacheAdapter extends RecyclerView.Adapter<TacheAdapter.MyViewHolder> {
    public List<Tache> list;
    private Context mContext;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvNom;
        public TextView tvPourcentage;
        public TextView tvTempsEcouler;
        public TextView tvDateLimite;
        public LinearLayout parent_layout;
        public MyViewHolder(LinearLayout v) {
            super(v);
            parent_layout =v.findViewById(R.id.parent_layout);
            tvNom = v.findViewById(R.id.tvNom);
            tvPourcentage = v.findViewById(R.id.tvPourcentage);
            tvTempsEcouler = v.findViewById(R.id.tvTempsEcouler);
            tvDateLimite = v.findViewById(R.id.tvDateLimite);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TacheAdapter(Context context) {
        list = new ArrayList<>();
        mContext = context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public TacheAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tache_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Tache tacheCourante = list.get(position);

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Consultation.class);
                i.putExtra("Nom tache", tacheCourante.Nom);
                i.putExtra("Pourcentage", tacheCourante.Pourcentage);
                i.putExtra("Temps ecouler", tacheCourante.Temps);
                i.putExtra("Date limite", tacheCourante.DateLimite.toString());
                mContext.startActivity(i);
            }
        });

        holder.tvNom.setText(tacheCourante.Nom);
        holder.tvPourcentage.setText(""+tacheCourante.Pourcentage+"%"); // TODO setText sur un integer crash
        holder.tvTempsEcouler.setText(""+tacheCourante.Temps+" / 7");
        holder.tvDateLimite.setText(""+tacheCourante.DateLimite);
    }

    // renvoie la taille de la liste
    @Override
    public int getItemCount() {
        return list.size();
    }
}