package com.github.jovenpableo.friendtag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jovenpableo.friendtag.R;
import com.github.jovenpableo.friendtag.entity.User;
import com.github.jovenpableo.friendtag.firebase.Users;

import java.util.ArrayList;
import java.util.Map;

public class FriendsActivity extends AppCompatActivity {

    Users db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        LinearLayout listView = (LinearLayout) findViewById(R.id.list_view);

        LayoutInflater inflater = getLayoutInflater();

        db = new Users();

        ArrayList<User> resp = db.getFriends();

        for(int i = 0; i < resp.size(); i++){
            View friendView = inflater.inflate(R.layout.friends_view, listView, false);
            TextView textName = friendView.findViewById(R.id.name);
            TextView textTag = friendView.findViewById(R.id.tag);
            TextView textTagBack = friendView.findViewById(R.id.tag_back);

            Map<String, Object> userData = resp.get(i).toMap();

            Object test = userData.get("displayName");
            textName.setText(test.toString());

            listView.addView(friendView);
        }




//        listView.addView(friendView);
//
//        View friendView2 = inflater.inflate(R.layout.friends_view, listView, false);
//        TextView textView = (TextView) friendView2.findViewById(R.id.name);
//        textView.setText("Joe");
//        textView = friendView2.findViewById(R.id.tag);
//        textView.setText("9000");
//        textView = friendView2.findViewById(R.id.tag_back);
//        textView.setText("0");
//        textView = friendView2.findViewById(R.id.nearby);
//        textView.setText("");
//
//        listView.addView(friendView2);
    }
}
