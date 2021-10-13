package fiek.fiekunipr.bookism;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThreadActivity extends AppCompatActivity {

    Button bt1;
    TextView tf1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        bt1 = findViewById(R.id.btnAsync);
        tf1 = findViewById(R.id.tf1);



        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Async async = new Async();
                async.execute();
            }
        });
    }

    private class Async extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute(){
            sleepsecond();
            tf1.setText("Welcome");
        }

        @Override
        protected String doInBackground(Void... voids) {
            String[] welcome = {"to", "Bookism", "App", "!"};
            for(String item : welcome) {
                sleepsecond();
                publishProgress(item);
            }
            return "Welcome to Bookism App";
        }

        @Override
        protected void onProgressUpdate(String... values){
            tf1.setText(values[0]);
        }

        @Override
        protected void onPostExecute(String result){
            tf1.setText(result);
        }
    }

    private void sleepsecond(){
        try{
            Thread.sleep(700);
        }catch (InterruptedException e){
            Log.e("Thread1", e.getLocalizedMessage());
        }
    }
}