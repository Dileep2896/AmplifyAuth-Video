package com.technologybit.amplifytutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.core.Amplify;

public class CodeVerificationActivity extends AppCompatActivity {

    EditText etCode;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);

        etCode = findViewById(R.id.etCode);

        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        username = bundle.getString("username");

    }

    public void confirmCode(View view){
        if (etCode.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter The Code",
                    Toast.LENGTH_SHORT).show();
        } else {
            Log.i("Code", "Code Confirm Button");
            Amplify.Auth.confirmSignUp(
                    username,
                    etCode.getText().toString().trim(),
                    result -> Log.i("AuthQuickstart", result.isSignUpComplete() ?
                            goToLoginPage() : getToastError("Confirm sign up not complete")
                            ),
                    error -> Log.e("AuthQuickstart", getToastError(error.getMessage()))
            );
        }
    }

    public String goToLoginPage() {
        Intent i = new Intent(getApplicationContext(),SignInActivity.class);
        startActivity(i);
        return null;
    }

    public String getToastError(String error) {
        Toast.makeText(getApplicationContext(),
                error,
                Toast.LENGTH_SHORT).show();
        return null;
    }

}