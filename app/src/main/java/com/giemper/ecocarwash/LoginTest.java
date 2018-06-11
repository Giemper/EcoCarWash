package com.giemper.ecocarwash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginTest extends AppCompatActivity
{
    private FirebaseAuth ecoAuth;
    private AutoCompleteTextView emailView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ecoAuth = FirebaseAuth.getInstance();
        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);

        Button signInButton = findViewById(R.id.email_sign_in_button);
        signInButton.setOnClickListener((View view) ->
        {
            startLogin();
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseAuth.AuthStateListener ecoAuthListener = ((@NonNull FirebaseAuth firebaseAuth) ->
        {
            if(firebaseAuth.getCurrentUser() != null)
                startActivity(new Intent(this, MainActivity.class));
        });

        ecoAuth.addAuthStateListener(ecoAuthListener);
    }

    private void startLogin()
    {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        emailView.setError(null);
        passwordView.setError(null);

        if(TextUtils.isEmpty(email))
        {
            emailView.setError("Usuario no puede estar vacio.");
            emailView.requestFocus();
        }

        else if(TextUtils.isEmpty(password))
        {
            passwordView.setError("Contraseña no puede estar vacia.");
            passwordView.requestFocus();
        }

        else
        {
            ecoAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((@NonNull Task<AuthResult> task) ->
            {
                if(task.isSuccessful())
                {
                    startActivity(new Intent(this, MainActivity.class));
                }
                else
                {

                }
            });
        }
    }
}
