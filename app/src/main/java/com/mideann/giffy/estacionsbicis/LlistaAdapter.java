package com.mideann.giffy.estacionsbicis;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

// Add    "implementation 'com.android.support:recyclerview-v7:28.0.0'"    to build.gradle app
public class LlistaAdapter extends RecyclerView.Adapter<LlistaAdapter.ViewHolder> {


    private ArrayList<Estacio> dades;

    public LlistaAdapter(ArrayList<Estacio> p) {
        this.dades = p;
    }

    public void addLlibre(Estacio e) {
        this.dades.add(e);
        this.notifyDataSetChanged();

    }

    // onCreate crea la vista on s'enmagetzaran les dades
    @NonNull
    @Override
    public LlistaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.llista_layout, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    // onBind carrega les dades a la vista
    @Override
    public void onBindViewHolder(@NonNull LlistaAdapter.ViewHolder viewHolder, int i) {
        Estacio estacio = this.dades.get(i);
        viewHolder.txtTitol.setText(estacio.getType());
        viewHolder.txtAddress.setText(estacio.getAddress());
        viewHolder.txtAvailability.setText("Bicis disponibles: " + String.valueOf(estacio.getBikes()));
        viewHolder.txtSlots.setText("Llocs lliures: " + String.valueOf(estacio.getSlots()));
    }

    @Override
    public int getItemCount() {
        return this.dades.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitol;
        private TextView txtAddress;
        private TextView txtAvailability;
        private TextView txtSlots;


        public ViewHolder(View itemView) {
            super(itemView);
            this.txtTitol = itemView.findViewById(R.id.id_titleText);
            this.txtAddress = itemView.findViewById(R.id.id_address);
            this.txtAvailability = itemView.findViewById(R.id.id_availability);
            this.txtSlots = itemView.findViewById(R.id.id_slots);
        }
    }
}
