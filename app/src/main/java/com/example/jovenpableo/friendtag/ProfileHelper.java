package com.example.jovenpableo.friendtag;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

public class ProfileHelper implements View.OnClickListener {

    private Activity activity;

    private ProfileHelper(Activity activity){
        this.activity = activity;
    }

    public static ProfileHelper with(Activity activity){
        return new ProfileHelper(activity);
    }

    public void loadProfile(){
        final FrameLayout holder = activity.findViewById(R.id.about);

        AboutBuilder builder = AboutBuilder.with(activity)
                .setPhoto(R.mipmap.profile_picture)
                .setCover(R.mipmap.profile_cover)
                .setName("Your Full Name")
                .setSubTitle("Mobile Developer")
                .setBrief("I'm warmed of mobile technologies. Ideas maker, curious and nature lover.")
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .setWrapScrollView(true)
                .setLinksAnimated(true)
                .setShowAsCard(false);

        AboutView view = builder.build();
        holder.addView(view);

    }

    @Override
    public void onClick(View v) {

    }
}
