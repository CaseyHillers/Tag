package com.github.jovenpableo.friendtag;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        LinearLayout listView = (LinearLayout) findViewById(R.id.list_view);

        LayoutInflater inflater = getLayoutInflater();

        userManager = UserManager.getInstance();

        ArrayList<User> friends = userManager.getFriends();

        for(User friend : friends){
            View friendView = inflater.inflate(R.layout.friends_view, listView, false);
            TextView textName = friendView.findViewById(R.id.name);
            TextView textTag = friendView.findViewById(R.id.tag);
            TextView textTagBack = friendView.findViewById(R.id.tag_back);
            ImageView picture = friendView.findViewById(R.id.picture);


            // TODO CALCULATE NEARBY BOOLEAN BY DISTANCE OF X PRECISION

            textName.setText(resp.get(i).getDisplayName());
            textTag.setText(resp.get(i).getTags());
            textTagBack.setText(resp.get(i).getTagged());
            Picasso.get().load(resp.get(i).getPictureUrl()).transform(new CircleTransform()).into(picture);

            listView.addView(friendView);
        }
    }
}
