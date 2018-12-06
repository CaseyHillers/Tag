package com.github.jovenpableo.friendtag;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.jovenpableo.friendtag.R;
import com.github.jovenpableo.friendtag.entity.User;
import com.github.jovenpableo.friendtag.firebase.UserManager;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "ucsc-tag";

    enum State {
        EDITABLE,
        EDITING,
        MESSAGE,
        BEFRIEND
    }

    State state;

    ImageView avatarView;
    TextView nameView;
    TextView bioView;
    TextView tagsView;
    FloatingActionButton faButton;
    EditText bioEdit;

    UserManager userManager;
    User user;
    boolean isCurrentUser = false;
    boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        avatarView = findViewById(R.id.imageAvatar);
        nameView = findViewById(R.id.textName1);
        bioView = findViewById(R.id.textBio);
        tagsView = findViewById(R.id.textTagPoints);
        faButton = findViewById(R.id.floatingActionButton);
        bioEdit = findViewById(R.id.editBio);

        userManager = UserManager.getInstance();

        Intent intent = getIntent();
        if (intent.hasExtra("uid") && !intent.getStringExtra("uid").equals("")) {
            Log.i(TAG, "Setting profile to " + intent.getStringExtra("uid"));
            user = userManager.getUser(intent.getStringExtra("uid"));
            state = State.BEFRIEND;
        } else {
            user = userManager.getCurrentUser();
            state = State.EDITABLE;
        }

        // NOTE: This is just here to update the location of a user
        Location location = userManager.getLocation(this);
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
            isCurrentUser = true;
            Button actionButton = findViewById(R.id.btnAction);
            FloatingActionButton floatingActionButton = findViewById((R.id.floatingActionButton));
            actionButton.setVisibility(View.GONE);
            floatingActionButton.setImageResource(R.drawable.ic_edit_white_24dp);
        }

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

    public void onTagClick(View v) {
        Log.i(TAG, "Tag button clicked");
        userManager.tag(user);
    }

    public void faButtonClick(View view) {
        if (isCurrentUser && !isEditing) {
            faButton.setImageResource(R.drawable.ic_check_white_24dp);
            isEditing = true;
            //switch to EditView with content of bioView
            bioView.setVisibility(View.GONE);
            bioEdit.setVisibility(View.VISIBLE);
        } else if (isCurrentUser) { //isEditing is true
            faButton.setImageResource(R.drawable.ic_edit_white_24dp);
            isEditing = false;
            bioView.setText(bioEdit.getText());
            bioEdit.setVisibility(View.GONE);
            bioView.setVisibility(View.VISIBLE);
            //TODO: store in database
        } else {
            //TODO: start message activity here
        }
    }

    @Override
    public void onClick(View v) {

    }
}
