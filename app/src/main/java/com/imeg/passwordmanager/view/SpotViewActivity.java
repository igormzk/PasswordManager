package com.imeg.passwordmanager.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.imeg.passwordmanager.R;
import com.imeg.passwordmanager.db.SQLiteManager;
import com.imeg.passwordmanager.model.Spot;

import java.util.List;

public class SpotViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_view);
        ListView listView = findViewById(R.id.listView);
        SQLiteManager sql = new SQLiteManager(this);
        sql.open();
        final List<Spot> listSpot = sql.loadSpot();
        sql.close();

    }
}