package com.github.jovenpableo.friendtag.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.json.JSONArray;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String uid;
    private String displayName;

    // NOTE: Lon and Lat are stored as doubles.
    private Location location;
    private String pictureUrl;
    private String tags;
    private String tagged;

    private Bitmap picture;
    private ArrayList<String> friends;

    private final String TAG = "ucsc-tag";
    private final String TABLE_NAME = "users";

    public User() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        uid = firebaseUser.getUid();
        displayName = firebaseUser.getDisplayName();
        pictureUrl = firebaseUser.getPhotoUrl().toString();
    }

    public User(Map<String,Object> data) {
        uid = getString(data, "uuid");
        displayName = getString(data, "displayName");
        pictureUrl = getString(data, "pictureUrl");
        friends = getList(data, "friends");
        tags = getString(data, "tagPoints");
        tagged = getString(data, "taggedPoints");
        String lat = getString(data, "lat");
        String lon = getString(data, "lon");
        location = new Location("tag-firestore");
        if (lat != null && lon != null) {
            location.setLatitude(Double.parseDouble(lat));
            location.setLongitude(Double.parseDouble(lon));
        }
    }

    private String getString(Map<String, Object> data, String key) {
        if (data.containsKey(key)) {
            try {
                return data.get(key).toString();
            } catch (Exception e) {}
        }

        return null;
    }

    private ArrayList<String> getList(Map<String, Object> data, String key) {
        if (data.containsKey(key)) {
            try {
                ArrayList<String> list = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray(data.get(key).toString());


                for(int i = 0; i < jsonArray.length(); i++){
                    list.add(jsonArray.getString(i));
                }

                return list;
            } catch (Exception e) {}
        }

        return null;
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
    public ArrayList<String> getFriends() { return this.friends; }
    public String getTags() { return this.tags; }
    public String getTagged() { return this.tagged; }





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
