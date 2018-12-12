package com.github.jovenpableo.friendtag;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.jovenpableo.friendtag.R;
import com.github.jovenpableo.friendtag.entity.User;
import com.github.jovenpableo.friendtag.firebase.UserManager;
import com.github.jovenpableo.friendtag.utility.DownloadImage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    Context ctx;
    private static GoogleMap mMap;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    DownloadImage di;

    private ArrayList<User> users;
    private static UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ctx = getApplicationContext();
        this.userManager = UserManager.getInstance();
        ctx = getApplicationContext();
        try {
            Thread.sleep(1000);
//            Location location = userManager.getLocation(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                          @Override
                                          public boolean onMarkerClick(Marker marker) {
                                              Intent intent = new Intent(ctx, ProfileActivity.class);
                                              intent.putExtra("uid", marker.getTag().toString());
                                              startActivity(intent);
                                              return true;
                                          }
        });

        userManager.getAll(new Callable<Void>() {
            public Void call() {
                update();
                renderUsers();
                return null;
            }
        });
    }

    public void update() {
        User user = userManager.getCurrentUser();

        Location location = user.getLocation();
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16.0F));

    }

    public void renderUsers() {
        users = userManager.getAllUsers();
        Log.i("ucsc-tag", "Adding users to the map (size: " + users.size() + ")");

        for (User user : users) {
            di = new DownloadImage();
            di.uuid = user.getUid();
            di.execute(user.getPictureUrl());
        }
    }

    public static void renderAfter(Bitmap bitmap, String currUID){
        ArrayList<User> allUsers = userManager.getAllUsers();
        for (User user : allUsers) {
            if(user.getUid().equals(currUID)){
                Location location = user.getLocation();

                Log.i("ucsc-tag", "Putting " + user.getDisplayName() + " at " + location.getLatitude() + ", " + location.getLongitude());
                LatLng loc = new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude());
                mMap.addMarker(new MarkerOptions().position(loc).title(user.getDisplayName()).icon(BitmapDescriptorFactory.fromBitmap(bitmap))).setTag(user.getUid());
            }
        }

    }


    public void changeToFriends(View view) {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }
}
