package com.example.homepc.walkinggroupapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.CustomJson;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import retrofit2.Call;

/**
 * A new user uses this activity to register. Automatically logs in to the MainActivity
 * on successful registration.
 */
public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private WGServerProxy proxy;
    private App instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey());

        setupSignUpButton();
    }

    private void setupSignUpButton() {
        Button btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText n = findViewById(R.id.plaintxtUsername);
                EditText e = findViewById(R.id.plaintxtEmail);
                EditText p = findViewById(R.id.plaintxtPassword);

                String name = n.getText().toString();
                String email = e.getText().toString();
                String password = p.getText().toString();

                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
                user.setCurrentPoints(100);
                user.setTotalPointsEarned(100);

                CustomJson gameObjects = new CustomJson();

                gameObjects.setBackground("Yellow");
                gameObjects.setSticker("schoolbus");
                gameObjects.setQuests(new String[]{});

                try {
                    // Convert custom object to a JSON string:
                    String customAsJson = new ObjectMapper().writeValueAsString(gameObjects);
                    // Store JSON string into the user object, which will be sent to server.
                    user.setCustomJson(customAsJson);
                } catch (JsonProcessingException error) {
                    error.printStackTrace();
                }

                Call<User> caller = proxy.createNewUser(user);
                ProxyBuilder.callProxy(SignupActivity.this, caller, returnedUser -> response(returnedUser));
            }
        });
    }

    private void response(User user) {
        Log.w(TAG, "Server replied with user: " + user.toString());

        instance.setCurrentUser(user);

        // Deserialize the custom object from the user:
        try {
            CustomJson customObjectFromServer =
                    new ObjectMapper().readValue(
                            user.getCustomJson(),
                            CustomJson.class);
            Log.w(TAG, "De-serialized embedded rewards object: " + customObjectFromServer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Registration Successful.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
