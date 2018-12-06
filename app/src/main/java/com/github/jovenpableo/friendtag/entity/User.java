package com.github.jovenpableo.friendtag.entity;

import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
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
    private ArrayList<String> friends;

    private Map<String, Date> tags;
    private int tagPoints;

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

        tagPoints = getInt(data, "tagPoints");
        Map<String, Object> dataTags = (Map<String, Object>) data.get("tags");
        tags = getTags(dataTags);

        friends = getList(data, "friends");
    }

    private String getString(Map<String, Object> data, String key) {
        Log.v(TAG, "Getting string for " + key);
        if (data.containsKey(key)) {
            try {
                return data.get(key).toString();
            } catch (Exception e) {
                Log.w(TAG, "Failed to get key: " + key);
            }
        } else {
            Log.w(TAG, "Failed to find key:" + key);
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

    private int getInt(Map<String, Object> data, String key) {
        Log.v(TAG, "Getting integer for user");
        String integer = getString(data, key);
        Log.v(TAG, "Retrieved string: " + integer);
        if (integer.equals(null)) {
            Log.w(TAG, "Retrieved integer is null");
            return 0;
        }

        Log.v(TAG, "Parsing the integer");
        return Integer.parseInt(integer);
    }

    private Map<String, Date> getTags(Map<String, Object> data) {
        Map<String, Date> tags = new HashMap<String, Date>();

        if (data == null) {
            return tags;
        }

        Log.i(TAG, "Tag Data passed in " + data);

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String dateString = "";
            if (entry.getValue() != null) {
                dateString = entry.getValue().toString();
            }

            if (dateString.equals("")) {
                dateString = "2-Dec-2018 04:21:09";
            }

            Log.i(TAG, entry.getKey() + ":" + entry.getValue());
            Date date;
            try {
                date = new SimpleDateFormat("MMM dd yyyy HH:mm:ss").parse(dateString);
            } catch (Exception e) {
                date = new Date();
            }
            String uid = entry.getKey();
            tags.put(uid, date);
        }

        return tags;
    }

    public void tag(User user) {
        Log.i(TAG, "Tagging " + user.getUid());
        tags.put(user.uid, new Date());
        tagPoints++;
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
        if (tagPoints > 0) {
            return tagPoints;
        }

        return 0;
    }

    public ArrayList<String> getFriends() { return this.friends; }

    public void addFriend(User user) {
        if (friends == null) {
            friends = new ArrayList<>();
        }

        friends.add(user.getUid());
    }

    public boolean hasFriend(User friend) {
        if (friends == null) {
            return false;
        }

        return friends.contains(friend.getUid());
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
        map.put("tags", tags);
        map.put("bio", bio);

        if (location != null) {
            map.put("lon", location.getLongitude());
            map.put("lat", location.getLatitude());
        }

        if (friends != null) {
            map.put("friends", friends);
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
