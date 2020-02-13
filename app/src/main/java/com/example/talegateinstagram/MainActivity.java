package com.example.talegateinstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.talegateinstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private EditText etDescription;
    private Button btnTakePicture;
    private ImageView ivPicture;
    private Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDescription = findViewById(R.id.etDescription);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        ivPicture = findViewById(R.id.ivPicture);
        btnPost = findViewById(R.id.btnPost);

        queryPosts();
    }

    private void queryPosts() {
        ParseQuery<Post> postParseQuery = new ParseQuery<Post>(Post.class);
        postParseQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                for (Post post: posts) {
                    Log.d(TAG, "Post: " + post);
                }
            }
        });
    }
}
