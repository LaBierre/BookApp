package com.example.standard.bookapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submitClick (View view){

        editText = (EditText) findViewById(R.id.topic_edit_text);

        String url = editText.getText().toString();
        String query = url.replace(" ", "+");

         //Check if the device has internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean connectivity = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (connectivity){
            Intent i = new Intent(this, BookActivity.class);
            i.putExtra(getString(R.string.intent_key), query);
            startActivity(i);
        }else {
            Toast.makeText(MainActivity.this, R.string.internet_connection, Toast.LENGTH_LONG).show();
        }
    }
}
