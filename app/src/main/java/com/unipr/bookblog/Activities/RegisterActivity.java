package com.unipr.bookblog.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.unipr.bookblog.Helpers.ImageHelper;
import com.unipr.bookblog.R;

import java.io.IOException;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private EditText userEmail;
    private EditText userPassword;
    private EditText userPassword2;
    private EditText userName;
    private ProgressBar loadingProgress;
    private Button regBtn;
    private FirebaseAuth mAuth;
    static int PRegCode = 1;
    static int REQUEST_CODE = 1;
    ImageView imgUserPhoto;
    Uri pickedImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            checkAndRegisterForPermission();
        }

        mAuth = FirebaseAuth.getInstance();

        //ini views
        userEmail = findViewById(R.id.regMail);
        userPassword = findViewById(R.id.regPassword);
        userPassword2 = findViewById(R.id.regPassword2);
        userName = findViewById(R.id.regName);
        loadingProgress = findViewById(R.id.regProgressBar);
        regBtn = findViewById(R.id.regBtn);
        loadingProgress.setVisibility(View.INVISIBLE);

        regBtn.setOnClickListener(view -> {
            regBtn.setVisibility(View.INVISIBLE);
            loadingProgress.setVisibility(View.VISIBLE);
            final String email = userEmail.getText().toString();
            final String password = userPassword.getText().toString();
            final String password2 = userPassword2.getText().toString();
            final String name = userName.getText().toString();

            if (email.isEmpty() || name.isEmpty() || password.isEmpty() || !password.equals(password2)) {
                // something goes wrong : all fields must be filled
                // we need to display an error message
                showMessage("Please Verify all fields");
                regBtn.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
            } else {
                // everything is ok and all fields are filled now we can start creating user account
                // CreateUserAccount method will try to create the user if the email is valid
                CreateUserAccount(email, name, password);
            }
        });

        imgUserPhoto = findViewById(R.id.regUserProfile);
        imgUserPhoto.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                checkAndRegisterForPermission();
            } else {
                openGallery();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            pickedImgUri = data.getData();
            try {
                Bitmap bitmap = ImageHelper.getRoundedCornerBitmap(pickedImgUri, getContentResolver());
                imgUserPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_CODE);
    }

    private void checkAndRegisterForPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(RegisterActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PRegCode);
        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.CAMERA}, PRegCode);
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void CreateUserAccount(String email, final String name, String password) {
        // this method creates user account with specific email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // user account created successfully
                        showMessage("Account created");
                        // after we created user account we need to update his profile picture and name
                        updateUserInfo(name, pickedImgUri, mAuth.getCurrentUser());
                    } else {
                        // account creation failed
                        showMessage("Account creation failed" + Objects.requireNonNull(task.getException()).getMessage());
                        regBtn.setVisibility(View.VISIBLE);
                        loadingProgress.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        // first we need to upload user photo to firebase storage and get url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());

        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
            // image uploaded successfully
            // now we can get our image url
            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                // uri contain user image url
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(uri)
                        .build();

                currentUser.updateProfile(profileUpdate).addOnCompleteListener(task -> updateUI());
            });
        });
    }

    public void updateUI() {
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }
}