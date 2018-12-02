package com.github.jovenpableo.friendtag;

import android.graphics.Bitmap;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jovenpableo.friendtag.R;
import com.github.jovenpableo.friendtag.entity.User;
import com.github.jovenpableo.friendtag.firebase.UserManager;


public class ProfileActivity extends AppCompatActivity {
    private static String TAG = "ucsc-tag";

    ImageView avatarView;
    TextView nameView;
    TextView bioView;

    UserManager userManager;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        avatarView = findViewById(R.id.imageAvatar);
        nameView = findViewById(R.id.textName1);
        bioView = findViewById(R.id.textBio);

        userManager = new UserManager();
        user = userManager.getCurrentUser();

        // NOTE: This is just here to update the location of a user
        Location location = userManager.getLocation(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        setProfile(user);
    }

    private void setProfile(User user) {
        User currentUser = userManager.getCurrentUser();
        if (currentUser.equals(user)) {
            Button actionButton = findViewById(R.id.btnAction);
            FloatingActionButton floatingActionButton = findViewById((R.id.floatingActionButton));
            actionButton.setVisibility(View.GONE);
            floatingActionButton.setImageResource(R.drawable.ic_edit_white_24dp);
        }

        String name = user.getDisplayName();
        Bitmap avatar = user.getPicture();
        String bio = user.getBio();
        if (bio == null || bio.equals("")) {
            bio = "Hello! My name is " + name + " and I am ready to play some tag!";
            user.setBio(bio);
        }

        Log.i(TAG, "Name: " + nameView.getText());

        nameView.setText(name);
        bioView.setText(bio);

        Log.i(TAG, "New name: " + nameView.getText());
    }
}
