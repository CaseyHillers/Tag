package com.github.jovenpableo.friendtag.firebase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.jovenpableo.friendtag.LocationService;
import com.github.jovenpableo.friendtag.entity.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class UserManager {
    private static final UserManager ourInstance = new UserManager();

    private final String TAG = "ucsc-tag";
    private final String TABLE_NAME = "users";

    private FirebaseFirestore db;
    private FirebaseUser currentFirebaseUser;
    private User user;

    public Map<String, User> users;

    private FusedLocationProviderClient mFusedLocationClient;

    private UserManager() {
        db = FirebaseFirestore.getInstance();

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = new User();

        this.loadUsers();
    }

    public static UserManager getInstance() {
        return ourInstance;
    }


    @SuppressLint("MissingPermission")
    public Location getLocation(Activity context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        Log.i(TAG, "getLocation called");

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location loc) {
                        Log.i(TAG, "Location retrieved");
                        if (loc != null) {
                            user.setLocation(loc);
                            user.write(db);
                            Log.i(TAG, "Writing location (" + loc.getLatitude() + ", " + loc.getLongitude() + ")");
                        } else {
                            Log.e(TAG, "Could not retrieve location");
                        }
                    }
                });

        Log.i(TAG, "Got location i think");

        return user.getLocation();

    }

    public User getCurrentUser() {
        return this.user;
    }

    public User getUser(String uid) {
        return users.get(uid);
    }

    private void loadUsers() {
        users = new HashMap<String, User>();

        db.collection(TABLE_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User u = new User(document.getData());
                        if (u.getUid().equals(user.getUid())) {
                            Log.i(TAG, "Updating current user from the getAll() call");
                            user = u;
                        }

                        users.put(u.getUid(), u);
                    }
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void getAll(final Callable<Void> methodParam) {
        users = new HashMap<String, User>();

        db.collection(TABLE_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User u = new User(document.getData());
                        if (u.getUid().equals(user.getUid())) {
                            Log.i(TAG, "Updating current user from the getAll() call");
                            user = u;
                        }

                        users.put(u.getUid(), u);
                    }

                    try {
                        methodParam.call();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public boolean tag(User user) {
        if (this.user.equals(user)) {
            Log.i(TAG, "Cannot tag ourselves");
            return false;
        }

        // NOTE: Check if tag time is valid
        Date lastTagTime = user.getTagTime(user);
        Date currentTime = Calendar.getInstance().getTime();

        long difference = currentTime.getTime() - lastTagTime.getTime();
        long diffMinutes = difference / (60 * 1000);

        if (diffMinutes < 15) {
            Log.i(TAG, "Tag is on cooldown for this user");
            return false;
        }

        // TODO: Check if tag distance is valid

        // TODO: Tag the user


        return true;
    }

    public void write(User user) {
        db.collection(TABLE_NAME)
                .document(user.getUid())
                .update(user.toMap())
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

    public void getLocation(LocationService locationService) {
    }
}
