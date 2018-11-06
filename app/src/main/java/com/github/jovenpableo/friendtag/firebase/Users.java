package com.github.jovenpableo.friendtag.firebase;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.jovenpableo.friendtag.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Users {

    private final String TABLE_NAME = "users";
    private final String TAG = "ucsc-tag";


    private FirebaseFirestore db;
    private FirebaseUser currentFirebaseUser;

    public Users() {
        db = FirebaseFirestore.getInstance();

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

    public void addUser(User user) {
        db.collection(TABLE_NAME)
                .document(user.uid)
                .set(user.toMap())
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
