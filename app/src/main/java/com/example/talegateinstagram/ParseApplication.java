package com.example.talegateinstagram;

import android.app.Application;

import com.example.talegateinstagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this).
                applicationId("talegateInstagram").
                clientKey("itIsBlackAndWhite").
                server("http://talegate-instagram.herokuapp.com/parse").
                build());
    }
}
