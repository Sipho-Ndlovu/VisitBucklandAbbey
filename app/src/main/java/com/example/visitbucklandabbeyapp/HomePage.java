package com.example.visitbucklandabbeyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {
    private TextView btnAdmin;
    private TextView btnAttractions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        btnAdmin = (TextView) findViewById(R.id.btnAdmin);
        btnAttractions = (TextView) findViewById(R.id.btnAttractions);
        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {OpenAdmin();}
        });
        btnAttractions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {OpenAttractions();}
        });
    }

    private void OpenAttractions() {
        Intent intent = new Intent(HomePage.this, AttractionsPage.class);
        startActivity(intent);
    }

    public void OpenAdmin() {
        Intent intent = new Intent(HomePage.this, AdminPage.class);
        startActivity(intent);
    }
}
