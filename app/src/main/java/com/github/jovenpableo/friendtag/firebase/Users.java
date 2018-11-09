package com.github.jovenpableo.friendtag.firebase;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.github.jovenpableo.friendtag.entity.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Users {

    private final String TABLE_NAME = "users";
    private final String TAG = "ucsc-tag";


    private FirebaseFirestore db;
    private FirebaseUser currentFirebaseUser;
    private User user;

    private FusedLocationProviderClient mFusedLocationClient;

    public Users() {
        db = FirebaseFirestore.getInstance();

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = new User();
    }

    public ArrayList<User> getFriends() {
        // TODO: Postpone for sake of getting working demo
        return null;
    }

    public Location getLocation() {
        return user.getLocation();
    }

    public void addFriend(String email) {
        // TODO: Postpone for sake of getting working demo
    }

    public Location getLocation(Activity context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            return TODO;
            Log.e(TAG, "TODO!!! haha suckers");
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location loc) {
                            user.setLocation(loc);
                        }
                    });

            this.write(user);
            return user.getLocation();
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location loc) {
                        user.setLocation(loc);
                    }
                });

        this.write(user);
        return user.getLocation();

    }

    public void setLocation(Location location) {
        User user = new User();
        user.setLocation(location);
        this.write(user);
    }

    public User getUser() {
        return this.user;
    }

    public ArrayList<User> getAll() {
        // TODO: Write firestore code that gets everyone, then converts them to user entity
        return null;
    }

    public void write(User user) {
        db.collection(TABLE_NAME)
                .document(user.getUid())
                .set(user.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

}
