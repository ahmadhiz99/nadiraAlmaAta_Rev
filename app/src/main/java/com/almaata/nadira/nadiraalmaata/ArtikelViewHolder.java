package com.almaata.nadira.nadiraalmaata;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ArtikelViewHolder extends RecyclerView.ViewHolder {

    public TextView tvJudul;
    public TextView tvSumber;
    public TextView tvIsi;
    public Button btnOpen;

    public ArtikelViewHolder(View itemView) {
        super(itemView);
        tvJudul = itemView.findViewById(R.id.tv_judul);
        tvSumber = itemView.findViewById(R.id.tv_sumber);
        tvIsi = itemView.findViewById(R.id.tv_isi);
        btnOpen = itemView.findViewById(R.id.btn_open);
    }

    public void bindToArtikel(getsetArtikel getsetartikel, View.OnClickListener onClickListener){
        tvJudul.setText(getsetartikel.getJudul());
        tvSumber.setText(getsetartikel.getSumber());
        tvIsi.setText(getsetartikel.getIsi());
        btnOpen.setOnClickListener(onClickListener);

    }
}