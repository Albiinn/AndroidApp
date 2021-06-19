package fiek.fiekunipr.bookism;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void openActivity2() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}