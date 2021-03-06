package com.unipr.bookblog.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.unipr.bookblog.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText userMail, userPassword;
    private Button btnLogin;
    private ProgressBar loginProgress;
    private FirebaseAuth mAuth;
    private Intent homeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userMail = findViewById(R.id.login_mail);
        userPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.loginBtn);
        loginProgress = findViewById(R.id.login_progress);
        mAuth = FirebaseAuth.getInstance();
        homeActivity = new Intent(this, com.unipr.bookblog.Activities.HomeActivity.class);

        TextView textView = findViewById(R.id.create_one);
        textView.setOnClickListener(view -> {
            Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(registerActivity);
            finish();
        });

        btnLogin.setOnClickListener(view -> {
            btnLogin.setVisibility(View.GONE);
            loginProgress.setVisibility(View.VISIBLE);

            final String mail = userMail.getText().toString();
            final String password = userPassword.getText().toString();
            if (mail.isEmpty() || password.isEmpty()) {
                showMessage("Please Verify All Field");
                btnLogin.setVisibility(View.VISIBLE);
                loginProgress.setVisibility(View.INVISIBLE);
            } else {
                signIn(mail, password);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            //user is already connected  so we need to redirect him to home page
            updateUI();
        }
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void signIn(String mail, String password) {
        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                loginProgress.setVisibility(View.INVISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
                updateUI();
            } else {
                showMessage(Objects.requireNonNull(task.getException()).getMessage());
                btnLogin.setVisibility(View.VISIBLE);
                loginProgress.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void updateUI() {
        startActivity(homeActivity);
        finish();
    }
}
