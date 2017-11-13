package com.utini.utini;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText emailEdit, pwdEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        mAuth = FirebaseAuth.getInstance();
        final Button signUp, logIn;

        logIn = (Button) findViewById(R.id.loginbutton);
        signUp = (Button) findViewById(R.id.signupbutton);
        emailEdit = (EditText) findViewById(R.id.EmailEdit);
        pwdEdit = (EditText) findViewById(R.id.pwdEdit);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(Login_activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(emailEdit.getText().toString(), pwdEdit.getText().toString());
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(emailEdit.getText().toString(), pwdEdit.getText().toString());
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(String email, String password) {

        Log.d("DETAILS", "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SUCCESS", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FAILED", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Login_activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                });
    }
    private boolean validateForm() {
        boolean valid = true;
        String email = emailEdit.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Required.");
            valid = false;
        } else {
            emailEdit.setError(null);
        }
        String password = pwdEdit.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pwdEdit.setError("Required.");
            valid = false;
        } else {
            pwdEdit.setError(null);
        }
        return valid;
    }

    private void signIn(String email, String password) {
        Log.d("DETAILS", "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SUCCESS", "signInWithEmail:success");
                            Intent intent = new Intent(Login_activity.this, MainActivity.class);
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FAILURE", task.getException());
                            Toast.makeText(Login_activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Toast.makeText(Login_activity.this, "FAILED", Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

}
