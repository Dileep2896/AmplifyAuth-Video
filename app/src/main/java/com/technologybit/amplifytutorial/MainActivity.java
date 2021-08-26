package com.technologybit.amplifytutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.auth.result.AuthSignUpResult;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText etEmail, etUsername, etPassword;
    TextView tvError;
    private String errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);

        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Tutorial", "Initialized Amplify");
        } catch (AmplifyException failure) {
            Log.e("Tutorial", "Could not initialize Amplify", failure);
        }

    }

    public void btnSignUp(View view) {
        if (etEmail.getText().toString().isEmpty() || etUsername.getText().toString().isEmpty() ||
                etPassword.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Please Enter All The Fields",
                    Toast.LENGTH_SHORT).show();
            Log.i("Buttons", "Please Enter All The Fields");
        } else {
            Log.i("Buttons", "Sign Up Clicked");
            Log.i("Buttons", etEmail.getText().toString().toLowerCase(Locale.ROOT));
            Log.i("Buttons", etUsername.getText().toString());
            Log.i("Buttons", etPassword.getText().toString());
            AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), etEmail.getText().toString())
                .build();
            Amplify.Auth.signUp(etUsername.getText().toString(), etPassword.getText().toString(), options,
                    this::signUpUser,
                    this::signUpError
            );

        }
    }

    public void signUpUser(AuthSignUpResult result) {
        Log.i("AuthQuickStart", "Result: " + result.toString());
        Intent i = new Intent(getApplicationContext(),CodeVerificationActivity.class);

        //Create the bundle
        Bundle bundle = new Bundle();
        //Add your data to bundle
        bundle.putString("username", etUsername.getText().toString());
        //Add the bundle to the intent
        i.putExtras(bundle);

        startActivity(i);
    }

    public void signUpError(AuthException error) {
        Log.e("AuthQuickStart", "Sign up failed", error);
        Log.i("AuthQuickStart", "======================================================");
        errorText = Objects.requireNonNull(Objects.requireNonNull(
                error.getCause()).getLocalizedMessage()).substring(0, 30);
        Log.i("AuthQuickStart", "Sign up failed " + errorText);

        runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(getBaseContext(), errorText, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    public void btnSignIn(View view) {
        Log.i("Buttons", "Sign In Clicked");
        Intent i = new Intent(getApplicationContext(),SignInActivity.class);
        startActivity(i);
    }

    public void btnVideo(View view) {
        Log.i("Buttons", "Video Clicked");
        Intent i = new Intent(getApplicationContext(),VideoActivity.class);
        startActivity(i);
    }

}