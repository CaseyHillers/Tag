package com.github.jovenpableo.friendtag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jovenpableo.friendtag.R;
import com.github.jovenpableo.friendtag.entity.User;
import com.github.jovenpableo.friendtag.firebase.UserManager;

public class FriendsActivity extends AppCompatActivity {

    UserManager userManager;

    private LinearLayout listView;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        listView = findViewById(R.id.list_view);
        inflater = getLayoutInflater();

        userManager = UserManager.getInstance();

//        TODO: Make this use the callback functional way
//        ArrayList<User> friends = userManager.getAll();
//
//        for (User friend : friends) {
//            addMessageBox(friend);
//        }
    }

    private void addMessageBox(User user) {
            View friendView = inflater.inflate(R.layout.friends_view, listView, false);
            TextView textName = friendView.findViewById(R.id.name);
            TextView textTag = friendView.findViewById(R.id.tag);
            TextView textTagBack = friendView.findViewById(R.id.tag_back);

            textName.setText(user.getDisplayName());

            listView.addView(friendView);
    }
}
