package fiek.fiekunipr.bookism;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText name, surname, password, repass, email;
    Button signIn;

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

        String nameString = name.getText().toString().trim();
        String surnameS = surname.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String reepass = repass.getText().toString().trim();
        String sEmail = email.getText().toString().trim();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!TextUtils.isEmpty(nameString) && !TextUtils.isEmpty(surnameS) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(reepass) && !TextUtils.isEmpty(sEmail)) {
//                    if (password.getText().toString().equals(repass.getText().toString())) {
                        SQLiteDatabase objDb = new DatabaseHelper(RegisterActivity.this).getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DatabaseModelHelper.UsersName, name.getText().toString());
                        contentValues.put(DatabaseModelHelper.UsersLastname, surname.getText().toString());
                        contentValues.put(DatabaseModelHelper.UsersEmail, email.getText().toString());
                        contentValues.put(DatabaseModelHelper.UsersPassword, password.getText().toString());

                        try {
                            long id = objDb.insert(DatabaseModelHelper.UsersTable, null, contentValues);
                            if (id > 0)
                                Toast.makeText(RegisterActivity.this, getString(R.string.success_message), Toast.LENGTH_LONG).show();
                            Intent feedActivity = new Intent(RegisterActivity.this, FeedActivity.class);
                            startActivity(feedActivity);

                        } catch (Exception ex) {
                            Toast.makeText(RegisterActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                        } finally {
                            objDb.close();
                        }
                    }
//                }
//            }
        });
    }
}