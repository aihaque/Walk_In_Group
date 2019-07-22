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

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.Group;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Displays a list of users depending on the mode of the activity. Shows a list of children
 * or parents.
 */
public class UserListActivity extends AppCompatActivity {
    private static final String TAG = "UserListActivity";
    private WGServerProxy proxy;
    private App instance;

    private static int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        setupTitle();
        setupAddButton();
        getItems();
        registerClickCallback();
        setupBackButton();
    }

    private void setupTitle() {
        TextView title = (TextView) findViewById(R.id.textViewMy);
        if(mode == 1 || mode == 3) {
            title.setText("My Children");
        } else if(mode == 2) {
            title.setText("My Parents");
        }
    }

    private void setupAddButton() {
        Button btn = (Button) findViewById(R.id.btnAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddActivity.makeIntent(UserListActivity.this, mode);
                startActivity(intent);
            }
        });
    }

    private void getItems() {
        if(mode == 1 || mode == 3) {
            getMonitorUsers();
        } else if (mode == 2) {
            getMonitoredByUsers();
        }
    }

    private void displayItems(List<User> userList) {
        ArrayList<String> stringList = new ArrayList<>();

        for(User user : userList) {
            stringList.add(user.getName() + "\n" + user.getEmail());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, stringList);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    private void registerClickCallback() { //add child to a group
        ListView listView = (ListView) findViewById(R.id.list);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                if(mode == 3) {
                    Log.w(TAG, "Server replied with user :" + instance.getCurrentUserMonitor().get(position));
                    Call<List<User>> caller = proxy.addMemberToGroup(instance.getCurrGroup().getId(),
                            instance.getCurrentUserMonitor().get(position));
                    ProxyBuilder.callProxy(UserListActivity.this, caller, returnedUserList -> response(returnedUserList));
                    finish();
                }
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Edit Children Info");
        menu.add("Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;

        User user = null;
        if(mode == 1) {
            user = instance.getCurrentUserMonitor().get(position);
        } else if(mode == 2) {
            user = instance.getCurrentUserMonitoredBy().get(position);
        }

        Long currUserId = instance.getCurrentUserId();
        Long otherUserId = user.getId();

        if(item.getTitle().equals("Delete")) {
            if(mode == 1) {
                deleteMonitor(currUserId, otherUserId);
            } else if(mode == 2) {
                deleteMonitor(otherUserId, currUserId);
            }
        }
        if(item.getTitle().equals("Edit Children Info")){
            if(mode == 1){
                //Intent intent = new Intent(EditInfoFromParents.this, EditInfoActivity.class);
                Intent intent = EditInfoFromParents.makeIntent(this, otherUserId);
                startActivity(intent);
            }
        }



        return true;
    }

    private void getMonitorUsers() {
        Call<List<User>> caller = proxy.getMonitorUser(instance.getCurrentUserId());
        ProxyBuilder.callProxy(UserListActivity.this, caller, returnedUser -> response(returnedUser));
    }

    private void getMonitoredByUsers() {
        Call<List<User>> caller = proxy.getMonitoredByUser(instance.getCurrentUserId());
        ProxyBuilder.callProxy(UserListActivity.this, caller, returnedUser -> response(returnedUser));
    }

    private void response(List<User> userList) {
        Log.w(TAG, "Server replied with user list: " + userList.toString());
        setCurrentUserList(userList);
        displayItems(userList);
    }

    private void deleteMonitor(Long idA, Long idB) {
        Call<Void> caller = proxy.deleteMonitor(idA, idB);
        ProxyBuilder.callProxy(UserListActivity.this, caller, returnedNothing -> response(returnedNothing));
    }

    private void response(Void returnedNothing) {
        Log.w(TAG, "Server replied to delete request (no content was expected).");
        onResume();
    }

    private void setCurrentUserList(List<User> userList) {
        if(mode == 1 || mode == 3) {
            instance.setCurrentUserMonitor(userList);
        } else if(mode == 2) {
            instance.setCurrentUserMonitoredBy(userList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mode == 1 || mode == 3) {
            getMonitorUsers();
        } else if(mode == 2) {
            getMonitoredByUsers();
        }
    }

    public static Intent makeIntent(Context context, int listMode){
        mode = listMode;
        return new Intent(context, UserListActivity.class);
    }
}
