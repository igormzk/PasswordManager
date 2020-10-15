package com.imeg.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.imeg.passwordmanager.view.SpotActivity;
import com.imeg.passwordmanager.view.SpotViewActivity;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private Button btnNewSpot;
    private Button btnViewSpot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        btnNewSpot = findViewById(R.id.btnNewSpot);

        btnNewSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newSpot();
            }
        });

        btnViewSpot = findViewById(R.id.btnViewSpot);
        btnViewSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewSpot();
            }
        });

    }

    private void newSpot() {
        Intent i = new Intent(MainActivity.this, SpotActivity.class);
        startActivity(i);
    }

    private void viewSpot() {
        Intent i = new Intent(MainActivity.this, SpotViewActivity.class);
        startActivity(i);
    }
}