package com.example.talegateinstagram.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.talegateinstagram.R;
import com.example.talegateinstagram.models.Post;
import com.example.talegateinstagram.utils.BitmapScaler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int CAPTURE_IMAGE_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    private File photoFile;

    private EditText etDescription;
    private Button btnTakePicture;
    private ImageView ivPicture;
    private Button btnPost;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDescription = findViewById(R.id.etDescription);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        ivPicture = findViewById(R.id.ivPicture);
        btnPost = findViewById(R.id.btnPost);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLaunchCamera(view);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = etDescription.getText().toString();
                ParseUser user = ParseUser.getCurrentUser();
                savePost(description, user, photoFile);
            }
        });
    }

    private void savePost(String description, ParseUser user, File photoFile) {
        if (ivPicture.getDrawable() == null) {
            displayMessage("There is no photo!");
        } else {
            Post post = new Post();
            post.setDescription(description);
            post.setUser(user);
            post.setImage(new ParseFile(photoFile));
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        etDescription.setText("");
                        ivPicture.setImageResource(0);
                    } else {
                        Log.e(TAG, "Parse exception thrown", e);
                        displayMessage("Problem saving post");
                    }
                }
            });
        }
    }

    public void onLaunchCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(this, "com.talegate.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
        } else {
            displayMessage("You don't have an available camera app!");
        }
    }

    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.e(TAG, "Failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
                    Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                    Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 250);
                    ivPicture.setImageBitmap(resizedBitmap);
                } else {
                    displayMessage("Picture wasn't taken");
                }
                break;
            default:
                Log.d(TAG, "Some other request code was given");
        }
    }

    private void queryPosts() {
        ParseQuery<Post> postParseQuery = new ParseQuery<Post>(Post.class);
        postParseQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                for (Post post: posts) {
                    Log.d(TAG, "Post: " + post.getDescription());
                }
            }
        });
    }

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
