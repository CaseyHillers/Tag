package com.github.jovenpableo.friendtag.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String uid;
    private String displayName;

    // NOTE: Lon and Lat are stored as doubles.
    private Location location;
    private String pictureUrl;
    private Bitmap picture;

    private final String TAG = "ucsc-tag";
    private final String TABLE_NAME = "users";


    public User() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        uid = firebaseUser.getUid();
        displayName = firebaseUser.getDisplayName();
        pictureUrl = firebaseUser.getPhotoUrl().toString();


    }

    public String getUid() { return this.uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getDisplayName() { return this.displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public Location getLocation() { return this.location; }
    public void setLocation(Location location) { this.location = location; }
    public Bitmap getPicture() { return this.picture; }
    public void setPicture(Bitmap picture) { this.picture = picture;}
    public String getPictureUrl() { return this.pictureUrl; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }

    public void write(FirebaseFirestore db) {
        db.collection(TABLE_NAME)
                .document(getUid())
                .set(toMap())
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

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("uuid", uid);
        map.put("displayName", displayName);
        map.put("pictureUrl", pictureUrl);

        if(location != null){
            map.put("lon", location.getLongitude());
            map.put("lat", location.getLatitude());
        }

        return map;
    }
}
