package com.imeg.passwordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.imeg.passwordmanager.view.SpotActivity;
import com.imeg.passwordmanager.view.SpotViewActivity;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private Button btnNewSpot;
    private Button btnViewSpot;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnViewSpot = findViewById(R.id.btnViewSpot);
        btnNewSpot = findViewById(R.id.btnNewSpot);
        btnNewSpot.setEnabled(false);
        btnViewSpot.setEnabled(false);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Erro na autenticação: " + errString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                btnNewSpot.setEnabled(true);
                btnViewSpot.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Autenticação correta!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Falha na autenticação.", Toast.LENGTH_LONG).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Leitura biométrica para acesso")
                .setSubtitle("Acesse usando sua digital").setNegativeButtonText("Use a senha").build();

        biometricPrompt.authenticate(promptInfo);



        context = this;
        btnNewSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newSpot();
            }
        });


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