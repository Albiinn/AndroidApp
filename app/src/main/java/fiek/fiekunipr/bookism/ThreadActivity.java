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

    private class Async extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute(){
            tf1.setText("0");
        }

        @Override
        protected String doInBackground(Void... voids) {
            for(int i=1; i<=5; i++){
                sleepsecond();
                publishProgress(i);
            }
            return "Final";
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            tf1.setText(values[0].toString());
        }

        @Override
        protected void onPostExecute(String result){
            tf1.setText(result);
        }
    }

    private void sleepsecond(){
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            Log.e("Thread1", e.getLocalizedMessage());
        }

    }
}