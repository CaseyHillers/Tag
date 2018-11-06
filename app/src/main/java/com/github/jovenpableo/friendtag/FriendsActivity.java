package com.github.jovenpableo.friendtag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jovenpableo.friendtag.R;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        LinearLayout listView = (LinearLayout) findViewById(R.id.list_view);

        LayoutInflater inflater = getLayoutInflater();
        View friendView = inflater.inflate(R.layout.friends_view, listView, false);

        listView.addView(friendView);

        View friendView2 = inflater.inflate(R.layout.friends_view, listView, false);
        TextView textView = (TextView) friendView2.findViewById(R.id.name);
        textView.setText("Joe");
        textView = friendView2.findViewById(R.id.tag);
        textView.setText("9000");
        textView = friendView2.findViewById(R.id.tag_back);
        textView.setText("0");
        textView = friendView2.findViewById(R.id.nearby);
        textView.setText("");

        listView.addView(friendView2);
    }
}
