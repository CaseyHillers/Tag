package com.github.jovenpableo.friendtag.entity;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String uid;
    private String displayName;
    private Location location;
    private Bitmap picture;

    public User() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        uid = firebaseUser.getUid();
        displayName = firebaseUser.getDisplayName();
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("uuid", uid);
        map.put("displayName", displayName);

        return map;
    }
}
