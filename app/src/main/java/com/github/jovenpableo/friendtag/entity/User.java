package com.github.jovenpableo.friendtag.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String uid;
    private String displayName;

    // NOTE: Lon and Lat are stored as doubles.
    private Location location;
    private String pictureUrl;
    private Bitmap picture;

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
