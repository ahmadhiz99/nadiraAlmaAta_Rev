package com.almaata.nadira.nadiraalmaata;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private FirebaseDatabase firebase;
    private DatabaseReference databaseReference;
//    private SplashScreen splashScreen;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION=101;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION=102;
    private boolean permissionIsGranted=false;
    private DatabaseReference mDb;
    private double iMiles;
    private Context mContext=MapsActivity2.this;
    private Circle mCircle;
    private Marker mMarker;
    private static final int REQUEST = 112;
    private LocationManager locationManager;
    private Double curlng, curlat;
    private Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        firebase = FirebaseDatabase.getInstance();
        databaseReference = firebase.getReference("marker");
        mapFragment.getMapAsync(this);
        checkLocationPermission();
        //dref = FirebaseDatabase.getInstance().getReference().child("Users").child("MessProviders").child("ESX23mmeeNh5n4sZe0YXQlZOrTE2").child("ProfileInformation");
        //mClass = new MessProfileEditor();
        ChildEventListener mChildEventListener;
        mycurrentlocation();


    }

    private void drawMarkerWithCircle(LatLng position) {
        double radiusInMeters = 200.0;  // increase decrease this distancce as per your requirements
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions()
                .center(position)
                .radius(radiusInMeters)
                .fillColor(shadeColor)
                .strokeColor(strokeColor)
                .strokeWidth(8);
        mCircle = mMap.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(position);
        mMarker = mMap.addMarker(markerOptions);
    }


    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        LatLng coordinate = new LatLng(lat, lng);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinate, 10);

        mMap.animateCamera(cameraUpdate);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates((android.location.LocationListener) this);

            return;
        }

    }

    public void mycurrentlocation(){
        //permission
        checkLocationPermission();

        // GET CURRENT LOCATION
        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                //koordinat current loc
                if (location != null){
                    // Do it all with location
                    Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                    // Display in Toast
                    Toast.makeText(MapsActivity2.this,
                            "Lat : " + location.getLatitude() + " Long : " + location.getLongitude(),
                            Toast.LENGTH_LONG).show();
                    double curlat = location.getLatitude();
                    double curlng = location.getLongitude();

//                                 MarkerOptions markerOptions = new MarkerOptions();
//                    LatLng position = new LatLng(curlat, curlng);
//                    markerOptions.position(position);
//                    Marker marker = mMap.addMarker(markerOptions);
                    Circle circle;
                    double iMeter = iMiles * 1609.34;


                    onLocationChanged(location);
                }
            }
        });
    }

    //restart activity
    @Override
    public void onResume() {
        super.onResume();

        if(mMap != null){
            mMap.clear();mycurrentlocation();
            Intent refresh = new Intent(this, MapsActivity2.class);
            startActivity(refresh);//Start the same Activity
            finish();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //current loc
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

//        LatLng sydney = new LatLng(curlat,curlng);
//        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//
//        // Enable / Disable my location button
//        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        // Enable / Disable Compass icon
//        googleMap.getUiSettings().setCompassEnabled(true);
//
//        // Enable / Disable Rotate gesture
//        googleMap.getUiSettings().setRotateGesturesEnabled(true);
//
//        // Enable / Disable zooming functionality
//        googleMap.getUiSettings().setZoomGesturesEnabled(true);
// Zoom in, animating the camera.


//        float currentZoomLevel = getZoomLevel(circle);
//        float animateZomm = currentZoomLevel + 5;

//        Log.e("Zoom Level:", currentZoomLevel + "");
//        Log.e("Zoom Level Animate:", animateZomm + "");
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(curlat, curlng), animateZomm));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoomLevel), 2000, null);
//        Log.e("Circle Lat Long:", curlat + ", " + curlng);

//        );

//        LatLng aaaa = new LatLng(curlat, curlng);
//        mMap.addCircle(new CircleOptions()
//                .center(aaaa)
//                .radius(10000)
//                .strokeWidth(0f)
//                .fillColor(0x550000FF));



//        buildGoogleApiClient();
//        mMap.moveCamera(CameraUpdateFactory.newLatLng());

    }

    public float getZoomLevel(Circle circle) {
        float zoomLevel=0;
        if (circle != null){
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel =(int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel +.5f;
    }
    //builder curren loc




    //marker
    @Override
    protected void onStart() {
        super.onStart();

//        DatabaseReference currentDBcordinates = FirebaseDatabase.getInstance().getReference().child("banksampah");
//        DatabaseReference currentDBcordinates = FirebaseDatabase.getInstance().getReference().child("locarions");
        DatabaseReference currentDBcordinates = FirebaseDatabase.getInstance().getReference().child("umkmdiy");
        currentDBcordinates.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
                Marker[] allMarkers = new Marker[size];
                mMap.clear();

                for(DataSnapshot s: dataSnapshot.getChildren())
                {
                    CordinatesModel cordinatesModel = new CordinatesModel();

                    for(int i=0;i<=size;i++) {
                        try {

                            cordinatesModel.setmProviderLatitude(s.getValue(CordinatesModel.class).getmProviderLatitude());
                            cordinatesModel.setmProviderLongitude(s.getValue(CordinatesModel.class).getmProviderLongitude());
                            cordinatesModel.setmProviderBrandName(s.getValue(CordinatesModel.class).getmProviderBrandName());

                            //convert string latitude to double
                            Double latitude1 = cordinatesModel.getmProviderLatitude();
                            Double longitude1 = cordinatesModel.getmProviderLongitude();
                            String brandName=cordinatesModel.getmProviderBrandName();

                            LatLng latLng = new LatLng(latitude1, longitude1);
                            //   mMap.clear();
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            mMap.setTrafficEnabled(true);
                            mMap.setBuildingsEnabled(true);
                            mMap.getUiSettings().setZoomControlsEnabled(true);

                            //lets add updated marker
                            allMarkers[i] = mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(latLng).title(brandName));

                        }catch (Exception ex){
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


//        // Add a marker in Bantul and move the camera
//        LatLng bsGemahRipah = new LatLng(-7.8946253, 110.3294609);
//        mMap.addMarker(new MarkerOptions().position(bsGemahRipah).title("Bank Sampah Gemah Ripah"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(bsGemahRipah));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bsGemahRipah, zoomLevel));
//
//        LatLng bstuhuTentrem = new LatLng(-7.8206074,110.3787696 );
//        mMap.addMarker(new MarkerOptions().position(bstuhuTentrem).title("Bank Sampah Tuhu Tentrem"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(bstuhuTentrem));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bstuhuTentrem, zoomLevel));
//        firebase location
//    }



    //Locaion permission
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity2.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSION_REQUEST_FINE_LOCATION);
                            }
                        })
                        .create()
                        .show();

                finish();
                startActivity(getIntent());
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    }

                } else {Toast.makeText(mContext, "The app was not allowed to access your location", Toast.LENGTH_LONG).show();


                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        //mLocationRequest.setInterval(1000);
        //mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //sorting radius
//    public class SortPlaces()  {
//        LatLng currentLoc;
//
//        public SortPlaces(LatLng current){
//            currentLoc = current;
//        }

    public int compare() {
//            places.add("Place 1", LatLng());
        double lat1 = -7.7442257;
        double lat2 = -7.7859925;
        double lon1 = 110.4029802;
        double lon2 = 110.3565502;

//            double lat2 = place2.latlng.latitude;
//            double lon2 = place2.latlng.longitude;

        double distanceToPlace1 = distance(curlat, curlng, lat1, lon1);
        double distanceToPlace2 = distance(curlat, curlng, lat2, lon2);
        return (int) (distanceToPlace1 - distanceToPlace2);
    }

    public double distance(double fromLat, double fromLon, double toLat, double toLon) {
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLon - fromLon;
        double angle = 2 * Math.asin( Math.sqrt(
                Math.pow(Math.sin(deltaLat/2), 2) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon/2), 2) ) );
        return radius * angle;
    }
}
//}




//package com.nadira.localhost.nadhira;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentActivity;
//import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdate;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.Circle;
//import com.google.android.gms.maps.model.CircleOptions;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//
//    private GoogleMap mMap;
//    private FirebaseDatabase firebase;
//    private DatabaseReference databaseReference;
//private SplashScreen splashScreen;
//    GoogleApiClient mGoogleApiClient;
//    Location mLastLocation;
//    LocationRequest mLocationRequest;
//    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION=101;
//    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION=102;
//    private boolean permissionIsGranted=false;
//    private DatabaseReference mDb;
//private double iMiles;
//    private Context mContext=MapsActivity.this;
//    private Circle mCircle;
//    private Marker mMarker;
//    private static final int REQUEST = 112;
//    private LocationManager locationManager;
//    private Double curlng, curlat;
//    private Location location;
//    private Circle circle;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//         firebase = FirebaseDatabase.getInstance();
//        databaseReference = firebase.getReference("marker");
//        mapFragment.getMapAsync(this);
//        checkLocationPermission();
//        ChildEventListener mChildEventListener;
////mycurrentlocation();
//    }
//
//    public void mycurrentlocation(){
//        //permission
//        checkLocationPermission();
//
//        // GET CURRENT LOCATION
//        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
//        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                double lat = location.getLatitude();
//                double lng = location.getLongitude();
//                LatLng coordinate = new LatLng(lat, lng);
//
//
//                //Circle radius
//                // Instantiating CircleOptions to draw a circle around the marker
//                CircleOptions circleOptions = new CircleOptions();
//                // Specifying the center of the circle
//                circleOptions.center(coordinate);
//                // Radius of the circle
//                circleOptions.radius(7000);
//                // Border color of the circle
//                circleOptions.strokeColor(Color.BLACK);
//                // Fill color of the circle
//                circleOptions.fillColor(0x30ff0000);
//                // Border width of the circle
//                circleOptions.strokeWidth(2);
//                mMap.addCircle(circleOptions);
//
//                if(location != null){
//                    CameraUpdate center = CameraUpdateFactory.newLatLngZoom(coordinate, 11);
//                    mMap.moveCamera(center);
//                }
//
//            }
//        });
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        //current loc
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
//
//    }
//
////marker
//    @Override
//    protected void onStart() {
//        super.onStart();
//        DatabaseReference currentDBcordinates = FirebaseDatabase.getInstance().getReference().child("locarions");
//        currentDBcordinates.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                int size = (int) dataSnapshot.getChildrenCount();
//                Marker[] allMarkers = new Marker[size];
//                mMap.clear();
//
////                Marker[] sortMarkers = new Marker[size];
//
//                for(DataSnapshot s: dataSnapshot.getChildren())
//
//                {
//                    CordinatesModel cordinatesModel = new CordinatesModel();
//
//                    cordinatesModel.setmProviderLatitude(s.getValue(CordinatesModel.class).getmProviderLatitude());
//                    cordinatesModel.setmProviderLongitude(s.getValue(CordinatesModel.class).getmProviderLongitude());
//                    cordinatesModel.setmProviderBrandName(s.getValue(CordinatesModel.class).getmProviderBrandName());
//
//                    for(int i=0;i<=size;i++) {
//                        try {
//
//                            //convert string latitude to double
//                            Double latitude1 = cordinatesModel.getmProviderLatitude();
//                            Double longitude1 = cordinatesModel.getmProviderLongitude();
//                            String brandName = cordinatesModel.getmProviderBrandName();
//
//                            LatLng latLng = new LatLng(latitude1, longitude1);
//                   mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                            mMap.setTrafficEnabled(true);
//                            mMap.setBuildingsEnabled(true);
//                            mMap.getUiSettings().setZoomControlsEnabled(true);
//
//                            //lets add updated marker
//                                allMarkers[i] = mMap.addMarker(new MarkerOptions()
//                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(latLng).title(brandName));
//
//                        }catch (Exception ex){
//                        }
//                    }
//
//                }
//            }
////
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//
////Locaion permission
//    public boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//            new AlertDialog.Builder(this)
//                        .setTitle(R.string.title_location_permission)
//                        .setMessage(R.string.text_location_permission)
//                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //Prompt the user once explanation has been shown
//                                ActivityCompat.requestPermissions(MapsActivity.this,
//                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                        MY_PERMISSION_REQUEST_FINE_LOCATION);
//                            }
//                        })
//                        .create()
//                        .show();
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSION_REQUEST_FINE_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSION_REQUEST_FINE_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // location-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//                    }
//
//                } else {Toast.makeText(mContext, "The app was not allowed to access your location", Toast.LENGTH_LONG).show();
//
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//
//                }
//                return;
//            }
//
//        }
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        mLocationRequest = new LocationRequest();
//        //mLocationRequest.setInterval(1000);
//        //mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
//            }
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    public boolean onMarkerClick(Marker marker) {
//        return false;
//    }
//
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    private float distBetween(LatLng pos1, LatLng pos2) {
//        return distBetween(pos1.latitude, pos1.longitude, pos2.latitude,
//                pos2.longitude);
//    }
//
//    /** distance in meters **/
//    private float distBetween(double lat1, double lng1, double lat2, double lng2) {
//        double earthRadius = 3958.75;
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLng = Math.toRadians(lng2 - lng1);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
//                + Math.cos(Math.toRadians(lat1))
//                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
//                * Math.sin(dLng / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double dist = earthRadius * c;
//
//        int meterConversion = 1609;
//
//        return (float) (dist * meterConversion);
//    }
//
//    private Marker getNearestMarker(List<Marker> markers,
//                                    LatLng origin) {
//
//        Marker nearestMarker = null;
//        double lowestDistance = Double.MAX_VALUE;
//
//        if (markers != null) {
//
//            for (Marker marker : markers) {
//
//                double dist = distBetween(origin, marker.getPosition());
//
//                if (dist < lowestDistance) {
//                    nearestMarker = marker;
//                    lowestDistance = dist;
//                }
//            }
//        }
//
//        return nearestMarker;
//    }
//
//    private List<Marker> getSurroundingMarkers(List<Marker> markers,
//                                               LatLng origin, int maxDistanceMeters) {
//        List<Marker> surroundingMarkers = null;
//
//        if (markers != null) {
//            surroundingMarkers = new ArrayList<Marker>();
//            for (Marker marker : markers) {
//
//                double dist = distBetween(origin, marker.getPosition());
//
//                if (dist < maxDistanceMeters) {
//                    surroundingMarkers.add(marker);
//                }
//            }
//        }
//
//        return surroundingMarkers;
//    }
//
//
//
//    }
////}


