package com.example.homepc.walkinggroupapp.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
 * Shows two lists: The groups that the user leads, and the groups that the user is a member of.
 */
public class MyGroupsActivity extends AppCompatActivity {
    private static final String TAG = "MyGroupsActivity";
    private WGServerProxy proxy;
    private App instance;

    //ArrayList of strings
    ArrayList<String> stringLeaderList = new ArrayList<>();
    ArrayList<String> stringMemberList = new ArrayList<>();
    List<Group> leaderGroups;
    List<Group> memberGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        setupLeaderGroups();
        setupMemberGroups();
        setupAllGroupsButton();
        setupBackButton();
    }
    private void setupLeaderGroups() {
        stringLeaderList.clear();
        leaderGroups = instance.getCurrentUser().getLeadsGroups();

        for(int i = 0; i < leaderGroups.size(); i++) {
            Call<Group> caller = proxy.getGroupById(leaderGroups.get(i).getId());
            ProxyBuilder.callProxy(MyGroupsActivity.this, caller, returnedLeaderGroup -> responseLeaderGroups(returnedLeaderGroup));
        }
    }

    private void setupMemberGroups() {
        stringMemberList.clear();
        memberGroups = instance.getCurrentUser().getMemberOfGroups();

        for(int i=0; i<memberGroups.size(); i++) {
            Call<Group> caller = proxy.getGroupById(memberGroups.get(i).getId());
            ProxyBuilder.callProxy(MyGroupsActivity.this, caller, returnedMemberGroup -> responseMemberGroups(returnedMemberGroup));
        }
    }

    private void updateUI() {
        ArrayAdapter<String> adapterLeader = new ArrayAdapter<String>(this, R.layout.item, stringLeaderList);
        ListView listViewLeader = findViewById(R.id.listLeaderGroups);
        listViewLeader.setAdapter(adapterLeader);

        listViewLeader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;

                Call<Group> caller = proxy.getGroupById(leaderGroups.get(position).getId());
                ProxyBuilder.callProxy(MyGroupsActivity.this, caller, leaderGroup -> responseClickedGroup(leaderGroup));
            }
        });

        ArrayAdapter<String> adapterMember = new ArrayAdapter<String>(this, R.layout.item, stringMemberList);
        ListView listViewMember = findViewById(R.id.listMemberGroups);
        listViewMember.setAdapter(adapterMember);

        listViewMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;

                Call<Group> caller = proxy.getGroupById(memberGroups.get(position).getId());
                ProxyBuilder.callProxy(MyGroupsActivity.this, caller, membergroup -> responseClickedGroup(membergroup));
            }
        });
    }


    private void responseLeaderGroups(Group returnedLeaderGroup) {
        stringLeaderList.add(returnedLeaderGroup.getGroupDescription().toString());
        updateUI();
    }

    private void responseMemberGroups(Group returnedMemberGroup) {
        stringMemberList.add(returnedMemberGroup.getGroupDescription().toString());
        updateUI();
    }

    private void responseClickedGroup(Group clickedGroup) {
        Intent intent = GroupMembersActivity.makeIntent(MyGroupsActivity.this, clickedGroup);
        startActivity(intent);
    }

    private void setupAllGroupsButton() {
        Button btn = findViewById(R.id.btnAllGroups);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = GroupListActivity.makeIntent(MyGroupsActivity.this);
                startActivity(intent);
            }
        });
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

    private void getUser() {
        Call<User> caller = proxy.getUserById(instance.getCurrentUserId());
        ProxyBuilder.callProxy(MyGroupsActivity.this, caller, returnedUser -> responseClickedGroup(returnedUser));
    }

    private void responseClickedGroup(User returnedUser) {
        instance.setCurrentUser(returnedUser);
        setupLeaderGroups();
        setupMemberGroups();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, MyGroupsActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Thread.sleep(300);
            stringLeaderList.clear();
            stringMemberList.clear();
            getUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}