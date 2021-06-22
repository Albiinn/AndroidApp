package fiek.fiekunipr.bookism;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class RegisterActivity extends AppCompatActivity {
    EditText name, surname, password, repass, email;
    Button signIn;

    GoogleSignInClient mGoogleSignInClient;

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



        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("190310750508-76gjuhu536dil9pikr5bgag6e75se6kg.apps.googleusercontent.com")
                .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(RegisterActivity.this, googleSignInOptions);





        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = mGoogleSignInClient.getSignInIntent();
//
//                startActivityForResult(intent, 100);
                    registerUser();
                    }
//
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(signInAccountTask.isSuccessful()) {
                //String
                
                String s = "Google sign in successful";
                Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();

                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    if(googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        mAuth.signInWithCredential(authCredential).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //when task is successful
                                    Intent intent = new Intent(RegisterActivity.this, FeedActivity.class);
                                    Toast.makeText(RegisterActivity.this, "Boni", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, task.getException().getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
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
                                    Log.d("MUTI", task.getException().getStackTrace().toString());
                                    Toast.makeText(RegisterActivity.this, task.getException().getStackTrace().toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
    }
}

