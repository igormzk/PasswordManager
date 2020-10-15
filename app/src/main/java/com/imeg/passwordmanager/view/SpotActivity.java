package com.imeg.passwordmanager.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imeg.passwordmanager.R;
import com.imeg.passwordmanager.db.SQLiteManager;
import com.imeg.passwordmanager.model.Spot;

public class SpotActivity extends AppCompatActivity {

    private EditText txtDescription;
    private EditText txtLogin;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);

        txtDescription = findViewById(R.id.txtDescription);
        txtLogin = findViewById(R.id.txtLogin);
        txtPassword = findViewById(R.id.txtPassword);

        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void save() {
        Spot s = new Spot();
        s.setDescription(txtDescription.getText().toString());
        s.setLogin(txtLogin.getText().toString());
        s.setPassword(txtPassword.getText().toString());
        SQLiteManager sql = new SQLiteManager(this);
        sql.open();
        String result = sql.saveSpot(s);
        sql.close();
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
}