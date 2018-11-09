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
    private User user;

    public Users() {
        db = FirebaseFirestore.getInstance();

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = new User();
    }

    public ArrayList<User> getFriends() {
        // TODO: Postpone for sake of getting working demo
        return null;
    }

    public Location getLocation() {
        user.getLocation();
    }

    public void addFriend(String email) {
        // TODO: Postpone for sake of getting working demo
    }

    public void setLocation(Location location) {
        User user = new User();
        user.setLocation(location);
        this.write(user);
    }

    public User getUser() {
        return this.user;
    }

    public void write(User user) {
        db.collection(TABLE_NAME)
                .document(user.getUid())
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
