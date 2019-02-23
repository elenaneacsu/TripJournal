package com.elenaneacsu.tripjournal.trips.activities;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        initView();
    }

    private void initView() {
        mEditTextName = findViewById(R.id.edittext_name);
        mEditTextEmail = findViewById(R.id.edittext_email);
        mEditTextPassword = findViewById(R.id.edittext_password);
    }

    public void signup(View view) {
        String email = mEditTextEmail.getText().toString();
        final String password = mEditTextPassword.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.add_email, Toast.LENGTH_LONG).show();
        }

        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.add_password, Toast.LENGTH_LONG).show();
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                Toast.makeText(getApplicationContext(), R.string.password_too_short, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.signup_error, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            initialiseUser();
                            Toast.makeText(SignUpActivity.this, R.string.signed_up_successfully, Toast.LENGTH_LONG).show();

                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            if (currentUser != null) {
                                currentUser.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), R.string.verify_email, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                            startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                            finish();
                        }
                    }
                });
    }

    private void initialiseUser() {
        String name = mEditTextName.getText().toString();
        databaseReference.child("USERS").child(firebaseAuth.getUid()).child("name").setValue(name);
        databaseReference.child("USERS").child(firebaseAuth.getUid()).child("noTrips").setValue(0);
    }

    public void switchToLogin(View view) {
        startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
    }
}
