package com.almaata.nadira.nadiraalmaata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class detailBerita extends AppCompatActivity {
    private TextView judulDetail, isiDetail;
    private ImageView image;
    public static final String EXTRA_JUDUL = "extra_judul";
    public static final String EXTRA_ISI = "extra_isi";
    public static final String EXTRA_IMAGE = "extra_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);

        judulDetail = findViewById(R.id.tv_juduldetail);
        image = findViewById(R.id.img_detail);
        isiDetail = findViewById(R.id.tv_isidetail);

        //judul
        String name = getIntent().getStringExtra(EXTRA_JUDUL);
        // int age = getIntent().getIntExtra(EXTRA_AGE, 0);
        String textjudul = name ;
        judulDetail.setText(textjudul);

        //ISI
        String isi = getIntent().getStringExtra(EXTRA_ISI);
        String textisi = isi ;
        isiDetail.setText(textisi);

        //foto
        String foto = getIntent().getStringExtra(EXTRA_IMAGE);
        String imgfoto = foto ;

        Glide.with(this)
                .load(imgfoto)
//                .apply(new RequestOptions().override(55, 55))
                .into(image);
    }
}
