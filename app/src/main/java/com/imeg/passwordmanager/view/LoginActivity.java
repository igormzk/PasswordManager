package com.imeg.passwordmanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.imeg.passwordmanager.MainActivity;
import com.imeg.passwordmanager.R;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Erro na autenticação: " + errString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
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

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);

        });
    }
}