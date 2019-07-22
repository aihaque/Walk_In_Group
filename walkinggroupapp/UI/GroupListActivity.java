package com.example.homepc.walkinggroupapp.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.Group;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Lists ALL the groups that are in the server.
 */
public class GroupListActivity extends AppCompatActivity {
    private static final String TAG = "GroupListActivity";
    private WGServerProxy proxy;
    private App instance;

    private List<Group> groupList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        getItems();
        setupAddGroupButton();
        registerClickCallback();
        setupBackButton();
    }

    private void setupAddGroupButton() {
        Button btn = findViewById(R.id.btnAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CreateGroupActivity.makeIntent(GroupListActivity.this);
                startActivity(intent);
            }
        });
    }

    private void registerClickCallback() {
        ListView listView = findViewById(R.id.list);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                Group group = groupList.get(position);

                Intent intent = GroupMembersActivity.makeIntent(GroupListActivity.this, group);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Start walking with this group");
        menu.add("Edit Group");
        menu.add("Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        Group group = groupList.get(position);
        Long groupId=group.getId();
        if (item.getTitle().equals("Delete")) {
            Toast.makeText(this, "Delete not yet implemented.", Toast.LENGTH_LONG).show();
            //deleteGroup(groupId);
        } else if (item.getTitle().equals("Edit Group")) {
            Intent intent=EditGroupActivity.makeintent(getApplicationContext(),group);
            startActivity(intent);
        }
        else if (item.getTitle().equals("Start walking with this group")) {
            instance.setStartWalkingWithTheGroup(group);
         }
        return true;
    }

    private void setupBackButton() {
        Button btn = findViewById(R.id.btnBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getItems() {
        Call<List<Group>> caller = proxy.getGroups();
        ProxyBuilder.callProxy(GroupListActivity.this, caller, returnedUser -> response(returnedUser));
    }

    private void deleteGroup(long groupId) {
        Call<Void> caller = proxy.deleteGroup(groupId);
        ProxyBuilder.callProxy(GroupListActivity.this, caller, returnedUser -> response(returnedUser));
    }

    private void response(List<Group> groupList) {
        Log.w(TAG, "Server replied with group list: " + groupList.toString());
        this.groupList = groupList;
        displayItems(groupList);
    }

    private void response(Void returnedNothing) {
        Log.w(TAG, "Server replied to login request (no content was expected).");
        getItems();
    }

    private void displayItems(List<Group> groupList) {
        ArrayList<String> stringList = new ArrayList<>();

        for(Group group : groupList) {
            stringList.add(group.getGroupDescription());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, stringList);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getItems();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, GroupListActivity.class);
    }
}
