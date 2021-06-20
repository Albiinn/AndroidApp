package fiek.fiekunipr.bookism;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        loginButton = (Button) findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int retLogin = LoginUser(email.getText().toString(), password.getText().toString());
                if(retLogin==1)
                    Toast.makeText(LoginActivity.this, getString(R.string.user_not_found), Toast.LENGTH_LONG).show();
                else if(retLogin == 0)
                    Toast.makeText(LoginActivity.this, getString(R.string.error_valid_email_password), Toast.LENGTH_LONG).show();
                else {
                    Intent mainActivityIntent = new Intent(LoginActivity.this, FeedActivity.class);
                    mainActivityIntent.putExtra("email", email.getText().toString());
                    startActivity(mainActivityIntent);
                }
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
        email.setText(s1);
        password.setText(s2);
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
        myEdit.putString("email", email.getText().toString());
        myEdit.putString("password", password.getText().toString());
        myEdit.apply();
    }

    // Store the data in the SharedPreference
    // in the onPause() method
    // When the user closes the application
    // onPause() will be called
    // and data will be stored
    private int LoginUser(String email, String password)
    {
        SQLiteDatabase objDb = new DatabaseHelper(LoginActivity.this).getReadableDatabase();
        Cursor cursor = objDb.query(DatabaseModelHelper.UsersTable, new String[]{DatabaseModelHelper.UsersEmail, DatabaseModelHelper.UsersPassword},DatabaseModelHelper.UsersEmail="?",
                new String[]{email}, "","","");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            String dbUserMail = cursor.getString(0);
            String dbUserPassword = cursor.getString(1);

            cursor.close();
            objDb.close();
            if(password.equals(dbUserPassword))
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        return -1;
    }
    public void openActivity2() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}