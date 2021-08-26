package com.technologybit.amplifytutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.Consumer;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etUsername = findViewById(R.id.etSignInUser);
        etPassword = findViewById(R.id.etSignInPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

    }

    @SuppressLint("SetTextI18n")
    public void btnSignInUser(View view) {
        if (etUsername.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter All The Fields",
                    Toast.LENGTH_SHORT).show();
        } else {
            btnSignIn.setText("Signing In....");
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            Log.i("AuthQuickstart", username);
            Log.i("AuthQuickstart", password);
            Amplify.Auth.signIn(
                    username,
                    password,
                    this::onLoginSuccess,
                    this::handleError
            );
        }
    }

    public void onLoginSuccess(AuthSignInResult result){
        finish();
        Intent i = new Intent(getApplicationContext(),ViewVideoActivity.class);
        startActivity(i);
    }

    @SuppressLint("SetTextI18n")
    public void handleError(AuthException error) {
        btnSignIn.setText("Sign");
        if (Objects.equals(error.getMessage(), "User not confirmed in the system.")) {
            confirmUser();
        }

        runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(getBaseContext(), error.getMessage(),
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    public void confirmUser() {
        Intent i = new Intent(getApplicationContext(),CodeVerificationActivity.class);

        //Create the bundle
        Bundle bundle = new Bundle();
        //Add your data to bundle
        bundle.putString("username", etUsername.getText().toString());
        //Add the bundle to the intent
        i.putExtras(bundle);

        startActivity(i);
    }

}