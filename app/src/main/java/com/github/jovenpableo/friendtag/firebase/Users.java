package com.github.jovenpableo.friendtag.firebase;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.jovenpableo.friendtag.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Users {

    private final String TABLE_NAME = "users";

    private FirebaseDatabase database;
    private FirebaseUser currentFirebaseUser;
    private DatabaseReference mDatabase;

    public Users() {
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(TABLE_NAME);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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

    public void writeUser(User user) {
        Log.d("ucsc-tag","Writing user to firebase");
        mDatabase.child(user.uid).setValue(user.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("ucsc-tag", "Successfully wrote/updated user to firebase");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ucsc-tag", e.toString());
            }
        });
    }


}
