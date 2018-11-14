package com.github.jovenpableo.friendtag;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.jovenpableo.friendtag.R;
import com.github.jovenpableo.friendtag.entity.User;
import com.github.jovenpableo.friendtag.firebase.Users;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    Context ctx;
    private GoogleMap mMap;

    private ArrayList<User> users;
    private Users userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ctx = getApplicationContext();
        this.userHelper = new Users();
        ctx = getApplicationContext();
        try {
            Thread.sleep(1000);
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
                                              startActivity(intent);
                                              return true;
                                          }
        });

        users = userHelper.getAll(new Callable<Void>() {
            public Void call() {
                update();
                renderUsers();
                return null;
            }
        });
    }

    public void update() {
        User user = userHelper.getUser();

        Location location = user.getLocation();
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16.0F));

    }

    public void renderUsers() {
        users = userHelper.users;
        Log.i("ucsc-tag", "Adding users to the map (size: " + users.size() + ")");
        for (User user : users) {
            Location location = user.getLocation();
            Log.i("ucsc-tag", "Putting " + user.getDisplayName() + " at " + location.getLatitude() + ", " + location.getLongitude());
            LatLng loc = new LatLng(user.getLocation().getLatitude(), user.getLocation().getLongitude());
            mMap.addMarker(new MarkerOptions().position(loc).title(user.getDisplayName()));
        }
    }

}
