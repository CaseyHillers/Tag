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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Users {

    private final String TAG = "ucsc-tag";
    private final String TABLE_NAME = "users";


    private FirebaseFirestore db;
    private FirebaseUser currentFirebaseUser;
    private User user;

    public ArrayList<User> users;

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

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location loc) {
                        Log.i(TAG, "Location retrieved");
                        if (loc != null) {
                            user.setLocation(loc);
                            user.write(db);
                            Log.i(TAG, "Location was not nulL! :)");
                        } else {
                            Log.e(TAG, "Could not retrieve location");
                        }
                    }
                });

        Log.i(TAG, "Got location i think");

        this.write(user);
        return user.getLocation();

    }

    public User getUser() {
        return this.user;
    }

    public ArrayList<User> getAll(final Callable<Void> methodParam) {
        users = new ArrayList<>();

        db.collection(TABLE_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = new User(document.getData());
                        users.add(user);
                    }

                    try {
                        methodParam.call();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        return users;
    }

    public void write(User user) {
        db.collection(TABLE_NAME)
                .document(user.getUid())
                .update(user.toMap())
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
