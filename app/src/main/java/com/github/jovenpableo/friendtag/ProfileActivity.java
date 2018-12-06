package com.github.jovenpableo.friendtag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jovenpableo.friendtag.R;
import com.github.jovenpableo.friendtag.entity.User;
import com.github.jovenpableo.friendtag.firebase.UserManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "ucsc-tag";

    enum State {
        EDITABLE,
        EDITING,
        MESSAGE,
        BEFRIEND
    }
    State state;

    private FirebaseFirestore db;

    ImageView avatarView;
    TextView nameView;
    TextView bioView;
    TextView tagsView;
    FloatingActionButton faButton;
    EditText bioEdit;

    UserManager userManager;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        avatarView = findViewById(R.id.imageAvatar);
        nameView = findViewById(R.id.textName1);
        bioView = findViewById(R.id.textBio);
        tagsView = findViewById(R.id.textTagPoints);
        faButton = findViewById(R.id.floatingActionButton);
        bioEdit = findViewById(R.id.editBio);

        userManager = UserManager.getInstance();
        state = State.BEFRIEND;

        Intent intent = getIntent();
        if (intent.hasExtra("uid") && !intent.getStringExtra("uid").equals("")) {
            Log.i(TAG, "Setting profile to " + intent.getStringExtra("uid"));
            user = userManager.getUser(intent.getStringExtra("uid"));
        } else {
            user = userManager.getCurrentUser();
            state = State.EDITABLE;
        }

        // NOTE: This is just here to update the location of a user
        Location location = userManager.getLocation(this);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setProfile(user);
    }

    private void updateFab() {
        FloatingActionButton floatingActionButton = findViewById((R.id.floatingActionButton));
        switch (state) {
            case EDITABLE:
                floatingActionButton.setImageResource(R.drawable.ic_edit_white_24dp);
                break;
            case EDITING:
                floatingActionButton.setImageResource(R.drawable.ic_check_white_24dp);
                break;
            case BEFRIEND:
                floatingActionButton.setImageResource(R.drawable.ic_person_add_black_24dp);
                break;
            case MESSAGE:
                floatingActionButton.setImageResource(R.drawable.ic_message_white_24dp);
                break;
            default:
                Log.e(TAG, "Couldn't update FAB");
                break;
        }
    }

    private void setProfile(User user) {
        User currentUser = userManager.getCurrentUser();
        if (currentUser.equals(user)) {
            state = State.EDITABLE;

            // NOTE: Hide the tag button since we're on our own page
            Button actionButton = findViewById(R.id.btnAction);
            actionButton.setVisibility(View.GONE);
        } else if (currentUser.hasFriend(user)){
            // NOTE: Not current user check if friend
            state = State.MESSAGE;
        }

        updateFab();
        setTagButton(user);

        String name = user.getDisplayName();
        Bitmap avatar = user.getPicture();
        String tags = "" + user.getTagPoints();
        String bio = user.getBio();
        if (bio == null || bio.equals("")) {
            bio = "Hello! My name is " + name + " and I am ready to play some tag!";
            user.setBio(bio);
        }

        Log.i(TAG, "Name: " + nameView.getText());

        nameView.setText(name);
        bioView.setText(bio);
        bioEdit.setText(bio);
        tagsView.setText(tags);

        Log.i(TAG, "New name: " + nameView.getText());
    }

    public void setTagButton(User user) {
        double distance = userManager.getDistance(user);
        Button actionButton = findViewById(R.id.btnAction);

        if (distance > 1.0) {
            actionButton.setText("Too far away");
            actionButton.setBackgroundColor(Color.parseColor("#CCCCCC"));
            actionButton.setOnClickListener(null);
        }

        User current = userManager.getCurrentUser();
        // NOTE: Check if tag time is valid
        Date lastTagTime = current.getTagTime(user);
        if (lastTagTime != null) {
            Date currentTime = Calendar.getInstance().getTime();

            long difference = currentTime.getTime() - lastTagTime.getTime();
            long diffMinutes = difference / (60 * 1000);

            if (diffMinutes < 15) {
//                actionButton.setText("Tag is on cool down");
//                actionButton.setBackgroundColor(Color.parseColor("#CCCCCC"));
//                actionButton.setOnClickListener(null);
            }
        }
    }

    public void onTagClick(View v) {
        Log.i(TAG, "Tag button clicked");
        userManager.tag(user);
        setTagButton(user);
    }

    public void faButtonClick(View view) {
        switch (state) {
            case EDITABLE:
                state = State.EDITING;
                //switch to EditView with content of bioView
                bioView.setVisibility(View.GONE);
                bioEdit.setVisibility(View.VISIBLE);
                break;
            case EDITING:
                state = State.EDITABLE;
                user.setBio(bioEdit.getText().toString());
                user.write(db);
                bioView.setText(bioEdit.getText());
                bioEdit.setVisibility(View.GONE);
                bioView.setVisibility(View.VISIBLE);
                break;
            case BEFRIEND:
                // TODO: Write code and algorithms
                userManager.addFriend(user);
                Toast.makeText(this, "Added " + user.getDisplayName() + " to your friends list", Toast.LENGTH_LONG).show();
                state = State.MESSAGE;
                break;
            case MESSAGE:
                Toast.makeText(this, "Opening messages", Toast.LENGTH_SHORT).show();
                // TODO: Make intent that will go to the message activity
                break;
            default:
                Log.e(TAG, "Failed to find current profile state");
                break;
        }

        updateFab();
    }

    @Override
    public void onClick(View v) {

    }
}
