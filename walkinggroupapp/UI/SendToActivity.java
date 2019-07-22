package com.example.homepc.walkinggroupapp.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.Group;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Lists groups that user is a member of to select which group to send
 * a message to.
 */
public class SendToActivity extends AppCompatActivity {
    private static final String TAG = "SendToActivity";
    private WGServerProxy proxy;
    private App instance;

    private static int mode;
    private List<Group> groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to);
        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        getGroups();
        registerClickCallback();
    }

    private void getGroups() {
        Call<List<Group>> caller = proxy.getGroups();
        ProxyBuilder.callProxy(SendToActivity.this, caller, returnedList -> response(returnedList));
    }

    private void registerClickCallback() {
        ListView listView = (ListView) findViewById(R.id.listViewGroups);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                Group group = groupList.get(position);

                Intent intent = NewMessageActivity.makeIntent(SendToActivity.this, 0, group);
                startActivity(intent);
            }
        });
    }

    private void response(List<Group> groupList) {
        Log.w(TAG, "Server replied with group list: " + groupList.toString());
        this.groupList = groupList;
        displayItems(groupList);
    }

    private void displayItems(List<Group> groupList) {
        ArrayList<String> stringList = new ArrayList<>();

        for(Group group : groupList) {
            stringList.add(group.getGroupDescription());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, stringList);
        ListView listView = (ListView) findViewById(R.id.listViewGroups);
        listView.setAdapter(adapter);
    }
    public static Intent makeIntent(Context context, int listMode){
        mode = listMode;
        return new Intent(context, SendToActivity.class);
    }
}
