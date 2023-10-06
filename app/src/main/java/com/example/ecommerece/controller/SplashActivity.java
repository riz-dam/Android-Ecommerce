package com.example.ecommerece.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ecommerece.R;
import com.example.ecommerece.utility.Session;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    private ConnectionTestTask connectionTestTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequirment();
    }

    private void setRequirment(){
        //setup url
        Session.url ="https://dummyjson.com/";

        //ngetest
        connectionTestTask = new ConnectionTestTask();
        connectionTestTask.execute();
    }

    private void  goExit(){
        finish();
    }

    private void goMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    //cek koneksi
    private class ConnectionTestTask extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                URL url = new URL(Session.url + "http/200");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null)
                {
                    sb.append(line + "\n");
                }

                br.close();
                return sb.toString();
            } catch (Exception e) {
                return null;
            }
        }


        @Override
        protected void onPostExecute(String string) {
            connectionTestTask = null;

            if (string != null)
            {
                goMainActivity();
            }
            else {
                goExit();
                Toast.makeText(getApplicationContext(), "No Internet Connection/ Server Issue!", Toast.LENGTH_LONG).show();
            }
        }
    }
}