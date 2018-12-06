package com.github.jovenpableo.friendtag;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jovenpableo.friendtag.R;
import com.github.jovenpableo.friendtag.entity.User;
import com.github.jovenpableo.friendtag.firebase.UserManager;
import com.github.jovenpableo.friendtag.utility.CircleTransform;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {
    private final String TAG = "ucsc-tag";

    UserManager userManager;

    LinearLayout listView;
    LayoutInflater inflater;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        listView = findViewById(R.id.list_view);
        inflater = getLayoutInflater();

        userManager = UserManager.getInstance();

        currentUser = userManager.getCurrentUser();
        ArrayList<User> friends = userManager.getFriends();
        Log.i(TAG, "Friend count: " + friends.size());

        for(User friend : friends){
            addFriendView(friend);
        }
    }

    private void addFriendView(User friend) {
        Log.i(TAG, "Adding friend view for " + friend.getDisplayName());
        View friendView = inflater.inflate(R.layout.friends_view, listView, false);
        TextView textName = friendView.findViewById(R.id.name);
        TextView textTag = friendView.findViewById(R.id.tag);
        TextView textDistance = friendView.findViewById(R.id.textDistance);
        ImageView picture = friendView.findViewById(R.id.picture);

        Log.d(TAG, "Retrieved the view elements");

        double distance = getDistance(friend);

        textName.setText(friend.getDisplayName());
        textTag.setText("" + friend.getTagPoints());
        Picasso.get().load(friend.getPictureUrl()).transform(new CircleTransform()).into(picture);
        textDistance.setText("" + distance + " miles");

        Log.d(TAG, "Values set for friend view");

        listView.addView(friendView);
    }

    private double getDistance(User user) {
        Location here = currentUser.getLocation();
        Location there = user.getLocation();

        double distanceMeters = here.distanceTo(there);
        double distanceMiles = distanceMeters / 1609.344; // NOTE: Courtesy of Google
        distanceMiles = Math.round(distanceMiles * 100) / 100;

        return distanceMiles;
    }

}
