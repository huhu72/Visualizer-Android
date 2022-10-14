package com.example.visualizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public void openDVPage(View view){
        Intent intent = new Intent(this, Dijkstras_Visualizer_Activity.class);
        startActivity(intent);
    }

}