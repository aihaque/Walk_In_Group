package com.example.homepc.walkinggroupapp.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import java.util.List;

import retrofit2.Call;

/**
 * Adds a child/parent in monitor lists.
 */
public class AddActivity extends AppCompatActivity {
    private static final String TAG = "AddActivity";
    private WGServerProxy proxy;
    private App instance;

    private static int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        setupTitle();
        setupAddButton();
        setupBackButton();
    }

    private void setupTitle() {
        TextView title = (TextView) findViewById(R.id.txtViewAdd);
        if(mode == 1) {
            title.setText("Add Children");
        } else if(mode == 2) {
            title.setText("Add Parent");
        }
    }

    private void setupAddButton() {
        Button btn = (Button) findViewById(R.id.btnAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = readEmailField();
                getUserByEmail(email);
            }
        });
    }

    private void setupBackButton() {
        Button btn = (Button) findViewById(R.id.btnBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addToMonitor(User user) {
        Call<List<User>> caller = proxy.addMonitorUser(instance.getCurrentUserId(), user);
        ProxyBuilder.callProxy(AddActivity.this, caller, returnedUserList -> response(returnedUserList));
    }

    private void addToMonitoredBy(User user) {
        Call<List<User>> caller = proxy.addMonitoredByUser(instance.getCurrentUserId(), user);
        ProxyBuilder.callProxy(AddActivity.this, caller, returnedUserList -> response(returnedUserList));
    }

    private void response(List<User> userList) {
        Log.w(TAG, "Server replied with user list: " + userList.toString());
        finish();
    }

    private String readEmailField() {
        EditText e = (EditText) findViewById(R.id.editTextDescription);
        return e.getText().toString();
    }

    private void getUserByEmail(String email) {
        Call<User> caller = proxy.getUserByEmail(email);
        ProxyBuilder.callProxy(AddActivity.this, caller, returnedUser -> response(returnedUser));
    }

    private void response(User user) {
        Log.w(TAG, "Server replied with user list: " + user.toString());

        if(mode == 1) {
            addToMonitor(user);
        } else if(mode == 2) {
            addToMonitoredBy(user);
        }
    }

    public static Intent makeIntent(Context context, int addMode) {
        mode = addMode;
        return new Intent(context, AddActivity.class);
    }
}

