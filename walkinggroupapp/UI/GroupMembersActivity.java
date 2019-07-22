package com.example.homepc.walkinggroupapp.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;

/**
 * Lists group members in a group. Has the ability to Join the group
 * as the 'current' user, add children to the group, and delete self or children.
 */
public class GroupMembersActivity extends AppCompatActivity {
    private static final String TAG = "GroupMembersActivity";
    private WGServerProxy proxy;
    private App instance;

    private static Group group;
    private static User groupLeader;
    private static List<User> currUserList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        setupTitle();
        setupLeader();
        setupAddChildButton();
        setupJoinButton();
        getItems();
        registerClickCallback();
        setupBackButton();
    }

    private void setupTitle() {
        TextView name = findViewById(R.id.txtViewGroupName);
        name.setText(group.getGroupDescription());
    }

    private void setupLeaderName() {
        TextView leaderName = findViewById(R.id.txtViewLeaderName);
        leaderName.setText(groupLeader.getName());
    }

    private void setupLeader() {
        User leader = group.getLeader();

        if(leader != null) {
            Call<User> caller = proxy.getUserById(leader.getId());
            ProxyBuilder.callProxy(GroupMembersActivity.this, caller, returnedUser -> response(returnedUser));
        }

    }

   private void setupAddChildButton() {
        Button btn = findViewById(R.id.btnAddChild);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.setCurrGroup(group);
                Intent intent = UserListActivity.makeIntent(GroupMembersActivity.this, 3);
                startActivity(intent);
            }
        });
    }

    private void setupJoinButton() {
        Button btn = findViewById(R.id.btnJoinGroup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinGroup();
            }
        });
    }

    private void registerClickCallback() { //add child to a group
        ListView listView = findViewById(R.id.list);
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("View User Info");
        menu.add("Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;

        long groupId = group.getId();
        Long userId = currUserList.get(position).getId();
        User user = new User();
        user.setId(userId);
        if (item.getTitle().equals("View User Info")) {
            Call<User> caller = proxy.getUserById(userId);
            ProxyBuilder.callProxy(GroupMembersActivity.this, caller, returnedUser -> responseUserInfo(returnedUser));
        }
        else if(item.getTitle().equals("Delete")) {
            if(instance.getCurrentUser().searchId(userId) || instance.getCurrentUserId() == userId) {
                deleteUser(groupId, userId);
            } else {
                Toast.makeText(this, "ERROR: Unable to delete a non-child user.", Toast.LENGTH_LONG).show();
            }
        }

        return true;
    }

    private void joinGroup() {
        Call<List<User>> caller = proxy.addMemberToGroup(group.getId(), instance.getCurrentUser());
        ProxyBuilder.callProxy(GroupMembersActivity.this, caller, returnedUserList -> response(returnedUserList));
    }

    private void getItems() {
        Call<List<User>> caller = proxy.getGroupMembers(group.getId());
        ProxyBuilder.callProxy(GroupMembersActivity.this, caller, returnedUserList -> response(returnedUserList));
    }


    private void deleteUser(long groupId, Long userId) {
        Call<Void> caller = proxy.removeFromGroup(groupId, userId);
        ProxyBuilder.callProxy(GroupMembersActivity.this, caller, returnedNothing -> response(returnedNothing));
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

    private void response(List<User> userList) {
        Log.w(TAG, "Server replied with user list: " + userList.toString());
        currUserList = userList;
        displayItems(userList);
    }

    private void response(Void returnedNothing) {
        Log.w(TAG, "Server replied to login request (no content was expected).");
        getItems();
    }

    private void response(User returnedUser) {
        Log.w(TAG, "Server replied with leader details.");
        groupLeader = returnedUser;
        setupLeaderName();
    }

    private void response(Group returnedGroup) {
        Log.w(TAG, "Set group from server.");
        group = returnedGroup;
    }

    private void responseUserInfo(User returnedUser) {
        Log.w(TAG, "Set user from server for viewing info.");
        Intent intent = UserInfoActivity.makeIntent(GroupMembersActivity.this, returnedUser);
        startActivity(intent);
    }

    private void displayItems(List<User> userList) {
        ArrayList<String> stringList = new ArrayList<>();

        for(User user : userList) {
            stringList.add(user.getName() + "\n" + user.getEmail());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, stringList);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    public static Intent makeIntent(Context context, Group newGroup){
        group = newGroup;
        return new Intent(context, GroupMembersActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Thread.sleep(300); //TODO: make this ovservable next iteration. Temporary fix for now.
            getItems();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}