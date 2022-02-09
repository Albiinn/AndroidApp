package com.unipr.bookblog.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.unipr.bookblog.Models.Post;
import com.unipr.bookblog.R;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int P_REQ_CODE = 2;
    private static final int REQUEST_CODE = 2;
    private Uri pickedImgUri = null;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Dialog popAddPost;
    ImageView popupUserImage;
    ImageView popupPostImage;
    ImageView popupAddBtn;
    TextView title;
    TextView popupTitle;
    TextView popupDescription;
    Toolbar toolbar;
    ProgressBar popupClickProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        toolbar = findViewById(R.id.toolbar);
        title = toolbar.findViewById(R.id.tv_toolbar_custom);
        setSupportActionBar(toolbar);
        Async async = new Async();
        async.execute();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        initPopup();
        setupPopupImageClick();

        FloatingActionButton fab = findViewById(R.id.fabHome);
        fab.setOnClickListener(view -> popAddPost.show());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        // set the home fragment as the default one
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private void initPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        popupUserImage = popAddPost.findViewById(R.id.popup_user_image);
        popupPostImage = popAddPost.findViewById(R.id.popup_img);
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);

        // load Current user profile photo
        Glide.with(HomeActivity.this).load(currentUser.getPhotoUrl()).into(popupUserImage);

        // Add post click Listener
        popupAddBtn.setOnClickListener(view -> {
            popupAddBtn.setVisibility(View.INVISIBLE);
            popupClickProgress.setVisibility(View.VISIBLE);

            // we need to test all input fields (Title and description) and post image
            if (!popupTitle.getText().toString().isEmpty() && !popupDescription.getText().toString().isEmpty() && pickedImgUri != null) {
                // first we need to upload post Image
                // access firebase storage
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageDownloadLink = uri.toString();
                    // create post Object
                    Post post = new Post(popupTitle.getText().toString(), popupDescription.getText().toString(), imageDownloadLink, currentUser.getUid(), Objects.requireNonNull(currentUser.getPhotoUrl()).toString());
                    // Add post to firebase database
                    addPost(post);
                }).addOnFailureListener(e -> {
                    // something goes wrong uploading picture
                    showMessage(e.getMessage());
                    popupClickProgress.setVisibility(View.INVISIBLE);
                    popupAddBtn.setVisibility(View.VISIBLE);
                }));
            } else {
                showMessage("Please verify all input fields and choose Post Image");
                popupAddBtn.setVisibility(View.VISIBLE);
                popupClickProgress.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        // get post unique ID and update post key
        String key = myRef.getKey();
        post.setPostKey(key);

        // add post data to firebase database
        myRef.setValue(post).addOnSuccessListener(aVoid -> {
            showMessage("Post added successfully");
            popupClickProgress.setVisibility(View.INVISIBLE);
            popupAddBtn.setVisibility(View.VISIBLE);
            popAddPost.dismiss();
        });
    }

    private void showMessage(String message) {
        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void setupPopupImageClick() {
        popupPostImage.setOnClickListener(view -> checkAndRequestForPermission());
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(HomeActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, P_REQ_CODE);
            }
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_CODE);
    }

    // when user picked an image ...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            popupPostImage.setImageURI(pickedImgUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_about_us) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (id == R.id.nav_take_photo) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Take Photo");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CameraFragment()).commit();
        } else if (id == R.id.nav_map) {
            Intent registerActivityIntent = new Intent(HomeActivity.this, CircleLayerClusteringActivity.class);
            startActivity(registerActivityIntent);
        } else if (id == R.id.nav_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
            finish();
        } else if (id == R.id.nav_about_us) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("About us");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutUsFragment()).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void updateNavHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);

        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        // now we will use Glide to load user image
        // first we need to import the library
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
    }

    private class Async extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            sleepSeconds(5000);
            title.setText("Welcome");
        }

        @Override
        protected String doInBackground(Void... voids) {
            String[] welcome = {"to", "Bookism", "App", "!"};
            for (String item : welcome) {
                sleepSeconds(1200);
                publishProgress(item);
            }
            return "Bookism";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            title.setText(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            title.setText(result);
        }
    }

    private void sleepSeconds(int sec) {
        try {
            Thread.sleep(sec);
        } catch (InterruptedException e) {
            Log.e("Thread1", e.getLocalizedMessage());
        }
    }
}