package com.github.jovenpableo.friendtag.entity;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String uid;
    private String displayName;

    // NOTE: Lon and Lat are stored as doubles.
    private Location location;
    private Bitmap picture;

    public User() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        uid = firebaseUser.getUid();
        displayName = firebaseUser.getDisplayName();
    }

    public String getUid() { return this.uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getDisplayName() { return this.displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public Location getLocation() { return this.location; }
    public void setLocation(Location location) { this.location = location; }
    public Bitmap getPicture() { return this.picture; }
    public void setPicture(Bitmap picture) { this.picture = picture;}

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("uuid", uid);
        map.put("displayName", displayName);

        return map;
    }
}
