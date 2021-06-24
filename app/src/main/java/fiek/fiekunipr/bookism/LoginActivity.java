package fiek.fiekunipr.bookism;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText etemail, etpassword;
    Button loginButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etemail = (EditText) findViewById(R.id.etEmail);
        etpassword = (EditText) findViewById(R.id.etPassword);
        loginButton = (Button) findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();

        String email = etemail.toString().trim();
        String pass = etpassword.toString().trim();

        loginButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if(email.isEmpty()) {
                    etemail.setError("Name is required");
                    etemail.requestFocus();
                    return;
                }
                if(pass.isEmpty()) {
                    etpassword.setError("Password is required");
                    etpassword.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etemail.setError("Enter right email");
                    etemail.requestFocus();
                    return;
                }
                if(pass.length()<6) {
//                    etpassword.setError("Password must be bigger than 6 characters");
//                    etpassword.requestFocus();
//                    return;
                    Snackbar snackbar = Snackbar.make(v, "Password must be bigger than 6 characters", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                }

                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                        } else {
                            Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }


    protected void onResume() {
        super.onResume();

        // Fetching the stored data
        // from the SharedPreference
        SharedPreferences sh = getSharedPreferences("LoginPreferences", MODE_PRIVATE);

        String s1 = sh.getString("email", "");
        String s2 = sh.getString("password", "");

        // Setting the fetched data
        // in the EditTexts
        etemail.setText(s1);
        etpassword.setText(s2);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Creating a shared pref object
        // with a file name "MySharedPref"
        // in private mode
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPreferences", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("email", etemail.getText().toString());
        myEdit.putString("password", etpassword.getText().toString());
        myEdit.apply();
    }

    // Store the data in the SharedPreference
    // in the onPause() method
    // When the user closes the application
    // onPause() will be called
    // and data will be stored


    public void openActivity2() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}