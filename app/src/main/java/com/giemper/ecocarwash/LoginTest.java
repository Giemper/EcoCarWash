package com.giemper.ecocarwash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static com.giemper.ecocarwash.CarMethods.checkFirebase;

public class LoginTest extends AppCompatActivity
{
    private FirebaseAuth ecoAuth;
    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkFirebase();

        ecoAuth = FirebaseAuth.getInstance();
        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        passwordView.setOnEditorActionListener((TextView textView, int id, KeyEvent keyEvent) ->
        {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL)
            {
                startLogin();
                return true;
            }
            return false;
        });

//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);

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
//                    showProgress(true);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                else
                {
                    Snackbar.make(findViewById(R.id.big_layout), "Login failed.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show)
//    {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
//        {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow((findViewById(R.id.big_layout)).getWindowToken(), 0);
//
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter()
//            {
//                @Override
//                public void onAnimationEnd(Animator animation)
//                {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter()
//            {
//                @Override
//                public void onAnimationEnd(Animator animation)
//                {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else
//        {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }
}
