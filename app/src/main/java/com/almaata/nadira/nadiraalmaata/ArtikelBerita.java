package com.almaata.nadira.nadiraalmaata;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class ArtikelBerita extends Fragment {

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<getsetArtikel, ArtikelViewHolder> mAdapter;
    private ArrayList<getsetArtikel> listArtikel;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_artikel_berita,
                container, false);

        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        // To dismiss the dialog
        progress.dismiss();




        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = rootView.findViewById(R.id.list_artikel);
        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query query = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<getsetArtikel>()
                .setQuery(query, getsetArtikel.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<getsetArtikel, ArtikelViewHolder>(options) {
            @Override
            public ArtikelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                //presitance

                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new ArtikelViewHolder(inflater.inflate(R.layout.item_artikel, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull final ArtikelViewHolder holder, int position, @NonNull final getsetArtikel model) {
                holder.bindToArtikel(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(), detailBerita.class);
                        intent.putExtra(detailBerita.EXTRA_JUDUL, model.getJudul());
                        intent.putExtra(detailBerita.EXTRA_ISI, model.getIsi());
                        intent.putExtra(detailBerita.EXTRA_IMAGE, model.getSumber());

                        startActivity(intent);

                    }
                });
            }
        };

        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    private Query getQuery(DatabaseReference mDatabase){
        Query query = mDatabase.child("artikel");
        return query;
    }


}
