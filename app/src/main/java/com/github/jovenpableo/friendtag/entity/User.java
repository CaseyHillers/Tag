package com.github.jovenpableo.friendtag.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {
    private final String TAG = "ucsc-tag";
    private final String TABLE_NAME = "users";

    private String uid;
    private String displayName;

    // NOTE: Lon and Lat are stored as doubles.
    private Location location;
    private String pictureUrl;
    private Bitmap picture;
    private String bio;

    private Map<String, Date> tags;
    private int tagPoints;
    private int taggedPoints;

    public User() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        uid = firebaseUser.getUid();
        displayName = firebaseUser.getDisplayName();
        pictureUrl = firebaseUser.getPhotoUrl().toString();
    }

    public User(Map<String, Object> data) {
        uid = getString(data, "uuid");
        displayName = getString(data, "displayName");
        pictureUrl = getString(data, "pictureUrl");
        bio = getString(data, "bio");

        String lat = getString(data, "lat");
        String lon = getString(data, "lon");
        location = new Location("tag-firestore");
        if (lat != null && lon != null) {
            location.setLatitude(Double.parseDouble(lat));
            location.setLongitude(Double.parseDouble(lon));
        }

//        tagPoints = getInt(data, "tagPoints");
//        taggedPoints = getInt(data, "taggedPoints");
    }

    private String getString(Map<String, Object> data, String key) {
        if (data.containsKey(key)) {
            try {
                return data.get(key).toString();
            } catch (Exception e) {
            }
        }

        return null;
    }

    private int getInt(Map<String, Object> data, String key) {
        String integer = getString(data, key);
        return Integer.parseInt(integer);
    }

    private Map<String, Date> getTags(Map<String, Object> data) {
        return null;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Bitmap getPicture() {
        return this.picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getPictureUrl() {
        return this.pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getTagPoints() {
        return this.tagPoints;
    }

    public int getTaggedPoints() {
        return this.taggedPoints;
    }

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
        map.put("tagPoints", tagPoints);
        map.put("taggedPoints", taggedPoints);
        map.put("tags", tags);

        if (location != null) {
            map.put("lon", location.getLongitude());
            map.put("lat", location.getLatitude());
        }

        return map;
    }

    public boolean equals(User user) {
        return this.uid == user.uid;
    }

    public Date getTagTime(User user) {
        // NOTE: Looks up user uid in the tag map
        String uid = user.getUid();
        return this.tags.get(uid);
    }
}
