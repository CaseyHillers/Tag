package com.github.jovenpableo.friendtag.firebase;

import android.location.Location;

import com.github.jovenpableo.friendtag.entity.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Users {

    private final String TABLE_NAME = "users";

    public Users() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(TABLE_NAME);
    }

    public ArrayList<User> getFriends() {
        return null;
    }

    public Location getLocation() {
        return null;
    }

    public void addFriend(String email) {

    }

    public void setLocation(Location location) {

    }


}
