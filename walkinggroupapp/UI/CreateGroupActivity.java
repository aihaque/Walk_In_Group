package com.example.homepc.walkinggroupapp.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.Group;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import java.util.List;

import retrofit2.Call;

/**
 * Creates a group with a description field only (will be upgraded in the future)
 */
public class CreateGroupActivity extends AppCompatActivity {
    private static final String TAG = "CreateGroupActivity";
    private WGServerProxy proxy;
    private App instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        setupAddButton();
        setupBackButton();
    }

    private void setupAddButton() {
        Button btn = (Button) findViewById(R.id.btnAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = readDescriptionField();
                Group group = new Group();
                group.setId(-1L);
                group.setGroupDescription(description);
                group.setLeader(instance.getCurrentUser());
                createGroup(group);
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

    private void createGroup(Group group) {
        Call<Group> caller = proxy.createNewGroup(group);
        ProxyBuilder.callProxy(CreateGroupActivity.this, caller, returnedGroup -> response(returnedGroup));
    }

    private void response(Group returnedGroup) {
        Log.w(TAG, "Server replied with group: " + returnedGroup.toString());
        finish();
    }

    private String readDescriptionField() {
        EditText e = (EditText) findViewById(R.id.editTextDescription);
        return e.getText().toString();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, CreateGroupActivity.class);
    }
}
