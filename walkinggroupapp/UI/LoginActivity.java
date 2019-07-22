package com.example.homepc.walkinggroupapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import retrofit2.Call;

/**
 * User login activity. User enters a valid email and password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private WGServerProxy proxy;
    private App instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey());

        setupLoginButton();
        setupSignUpButton();
    }

    private void setupLoginButton() {
        Button btnlogin = (Button) findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enableStrictMode();
                EditText e = (EditText) findViewById(R.id.plaintxtEmail);
                String email = e.getText().toString();
                EditText p = (EditText) findViewById(R.id.plaintxtPassword);
                String password = p.getText().toString();

                User user = new User();
                user.setEmail(email);
                user.setPassword(password);

                ProxyBuilder.setOnTokenReceiveCallback( token -> onReceiveToken(token));
                Call<Void> caller = proxy.login(user);
                ProxyBuilder.callProxy(LoginActivity.this, caller, returnedNothing -> response(returnedNothing));
                instance.setCurrentUser(user);

                //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //startActivity(intent);
            }
        });
    }

    private void onReceiveToken(String token) {
        // Replace the current proxy with one that uses the token!
        Log.w(TAG, "   --> NOW HAVE TOKEN: " + token);
        instance.setToken(token);
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), token);



        instance.setisUserLoggedIn(true);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class /*Change to your activity name*/);
        startActivity(intent);
    }

    private void response(Void returnedNothing) {
        Log.w(TAG, "Server replied to login request (no content was expected).");
    }

    private void setupSignUpButton() {
        Button btnsignup = (Button) findViewById(R.id.btnSignUp);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
