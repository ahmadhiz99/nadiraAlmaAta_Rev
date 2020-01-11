package com.almaata.nadira.nadiraalmaata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.almaata.nadira.nadiraalmaata.AccountActivity.LoginActivity;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class Home extends Fragment {

    CardView maplite;
    ViewFlipper viewFlipper;
    private CardView bankSampah;
    private TextView emailshow;
    public TextView emailget,saldo;
    public Long jmlsaldo;
    private Button btnChangePassword, btnRemoveUser,
            changePassword, remove, signOut;
    private TextView email, emailhome;
    private EditText oldEmail, password, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
//    private GoogleMap mMap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,
                container, false);

//        Account
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        email = (TextView) rootView.findViewById(R.id.useremail);
        emailhome =(TextView) rootView.findViewById(R.id.tv_emailShow);
        saldo =(TextView) rootView.findViewById(R.id.tv_saldoshow);

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);

        //jika user tak ada
//        authListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user == null) {
//                    // user auth state is changed - user is null
//                    // launch login activity
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                    getActivity().finish();
//                }
//            }
//        };

//

//saldogit init
        FirebaseDatabase database = FirebaseDatabase.getInstance();
       final DatabaseReference reference = database.getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser currentuser = auth.getCurrentUser();
                String uid = currentuser.getUid();

                Long output = dataSnapshot.child("User").child(uid).child("saldo").getValue(Long.class);
                String out = String.valueOf(output);
                saldo.setText("Saldo: "+out);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//        litemaps
      GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
      maplite = (CardView) rootView.findViewById(R.id.maplite);
        maplite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                maplite();
            }
        });
//emailget =rootView.findViewById(R.id.tv_emailShow);
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
                startActivity(new Intent(getActivity() , MapsActivity.class));
            }
        });



        Button button_umkm = (Button) rootView.findViewById(R.id.btn_umkm);
        button_umkm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                umkm();
            }
        });


        Button buttonsedekah = (Button) rootView.findViewById(R.id.btn_sedekahSampah);
        buttonsedekah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sedekah();
            }
        });
        return rootView;
    }

    public void updateDetail() {
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        startActivity(intent);
    }

    public void umkm() {
        Intent intent = new Intent(getActivity(), MapsActivity2.class);
        startActivity(intent);
    }

    public void sedekah() {
        Intent intent = new Intent(getActivity(), MapsActivity3.class);
        startActivity(intent);
    }

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


    @SuppressLint("SetTextI18n")
    public void setDataToView(FirebaseUser user) {

        emailhome.setText("User Email: " + user.getEmail());

        Toast.makeText(this.getContext(),"Selamat datang"+user.getEmail(),Toast.LENGTH_SHORT).show();

    }
    // this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            } else {
                setDataToView(user);

            }
        }


    };


}

