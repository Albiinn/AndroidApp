package fiek.fiekunipr.bookism;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        email.findViewById(R.id.etEmail);
        password.findViewById(R.id.etPassword);
        loginButton.findViewById(R.id.btnLogin);

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