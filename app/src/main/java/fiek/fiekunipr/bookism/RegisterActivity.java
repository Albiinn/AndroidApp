package fiek.fiekunipr.bookism;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText name, surname, password, repass, email;
    Button signIn;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.nameEt);
        password = findViewById(R.id.password);
        repass = findViewById(R.id.repassword);
        surname = findViewById(R.id.surname);
        signIn = findViewById(R.id.btnsignup);
        email = findViewById(R.id.email);

        mAuth = FirebaseAuth.getInstance();


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    registerUser();
                    }
//                }
//            }
        });
    }
    private void registerUser() {
        String nameString = name.getText().toString().trim();
        String surnameS = surname.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String reepass = repass.getText().toString().trim();
        String sEmail = email.getText().toString().trim();

        if(nameString.isEmpty()) {
            name.setError("Name is required");
            name.requestFocus();
            return;
        }
        if(surnameS.isEmpty()) {
            surname.setError("Surname is required");
            surname.requestFocus();
            return;
        }
        if(pass.isEmpty()) {
            password.setError("Name is required");
            password.requestFocus();
            return;
        }
        if(reepass.isEmpty()) {
            repass.setError("Name is required");
            repass.requestFocus();
            return;
        }
        if(sEmail.isEmpty()) {
            email.setError("Name is required");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
            email.setError("Enter right email");
            email.requestFocus();
            return;
        }
        if(reepass.length()<6) {
            password.setError("Password must be bigger than 6 characters");
            password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(sEmail, pass)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserHelper user = new UserHelper(nameString, surnameS, sEmail);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(RegisterActivity.this, "Failed to Register", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Failed Everything", Toast.LENGTH_LONG).show();
                        }
                        }
//
                });

    }
}

