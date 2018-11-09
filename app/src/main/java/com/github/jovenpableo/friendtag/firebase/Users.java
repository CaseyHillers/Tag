package com.github.jovenpableo.friendtag.firebase;

import android.Manifest;
import android.annotation.SuppressLint;
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

    private final String TAG = "ucsc-tag";
    private final String TABLE_NAME = "users";


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

    public void addFriend(String email) {
        // TODO: Postpone for sake of getting working demo
    }

    @SuppressLint("MissingPermission")
    public Location getLocation(Activity context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        Log.i(TAG, "getLocation called");

        Log.e(TAG, "TODO!!! haha suckers");
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location loc) {
                        Log.i(TAG, "Location retrieved");
                        if (loc != null) {
                            user.setLocation(loc);
                            user.write(db);
                            Log.i(TAG, "Location was not nulL! :)");
                        }
                    }
                });

        Log.i(TAG, "Got location i think");

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
