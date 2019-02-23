package com.elenaneacsu.tripjournal.trips.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elenaneacsu.tripjournal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;

    private FirebaseAuth firebaseAuth;

    public SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        firebaseAuth = FirebaseAuth.getInstance();

        initView();

        mSharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        if(mSharedPreferences.getBoolean("logged", false)) {
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
        }
    }

    private void initView() {
        mEditTextEmail = findViewById(R.id.edittext_email);
        mEditTextPassword = findViewById(R.id.edittext_password);
    }


    public void login(View view) {
        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.add_email), Toast.LENGTH_LONG).show();
        }

        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.add_password, Toast.LENGTH_LONG).show();
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), getString(R.string.wrong_credentials), Toast.LENGTH_LONG).show();
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.wrong_credentials), Toast.LENGTH_LONG).show();
                        } else {
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            if (currentUser.isEmailVerified()) {
                                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                                mSharedPreferences.edit().putBoolean("logged", true).apply();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.verify_email), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }


    public void switchToSignUp(View view) {
        startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
    }
}

