package com.almaata.nadira.nadiraalmaata;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.w3c.dom.Text;


public class Home extends Fragment {

    CardView maplite;
    ViewFlipper viewFlipper;
    private CardView bankSampah;
    private TextView emailshow;

//    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,
                container, false);
        //litemaps
//      GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
//      maplite = (CardView) rootView.findViewById(R.id.maplite);
//        maplite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                maplite();
//            }
//        });

        //TextView Email
        //Flipper
        int images[] = {R.drawable.pengertian, R.drawable.visi, R.drawable.misi};
        viewFlipper = rootView.findViewById(R.id.tv_flipper);

        for (int image:images){
            flipperImage(image);
        }

        //Button BankSAmpah
        Button button = (Button) rootView.findViewById(R.id.ll_banksampah);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                updateDetail();
            }
        });



        Button button_umkm = (Button) rootView.findViewById(R.id.btn_umkm);
        button_umkm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                umkm();
            }
        });


        Button buttonsedekah = (Button) rootView.findViewById(R.id.btn_sedekahSampah);
        buttonsedekah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sedekah();
            }
        });
        return rootView;
    }

//    public void updateDetail() {
//        Intent intent = new Intent(getActivity(), MapsActivity.class);
//        startActivity(intent);
//    }
//
//    public void umkm() {
//        Intent intent = new Intent(getActivity(), MapsActivity2.class);
//        startActivity(intent);
//    }
//
//    public void sedekah() {
//        Intent intent = new Intent(getActivity(), MapsActivity3.class);
//        startActivity(intent);
//    }

    //Flipper image
    public void flipperImage(int image){
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);

        //animation

        viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);

    }

//    public void maplite(){
//        Intent maplite = new Intent(getActivity(), MapsActivity.class);
//        startActivity(maplite);
//    }

}

