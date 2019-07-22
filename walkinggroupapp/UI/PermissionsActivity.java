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
import com.example.homepc.walkinggroupapp.Model.Permission;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Displays the external resources that are used for the project.
 */
public class PermissionsActivity extends AppCompatActivity {
    private static final String TAG = "PermissionsActivity";

    private static WGServerProxy proxy;
    private static App instance;

    List<Permission> permissionList;
    List<String> stringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        setupPendingButton();
        setupApprovedButton();
        setupDeniedButton();
        getPendingPermissions();
        registerClickCallback();
        Toast.makeText(this, "Long press on a permission to Approve/Deny", Toast.LENGTH_LONG).show();
    }

    private void setupPendingButton() {
        Button btn = (Button) findViewById(R.id.btnPending);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPendingPermissions();
            }
        });
    }

    private void setupApprovedButton() {
        Button btn = (Button) findViewById(R.id.btnApproved);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApprovedPermissions();
            }
        });
    }

    private void setupDeniedButton() {
        Button btn = (Button) findViewById(R.id.btnDenied);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeniedPermissions();
            }
        });
    }

    private void getPendingPermissions() {
        TextView name = findViewById(R.id.txtViewPermissionTitle);
        name.setText("Pending:");
        String id = instance.getCurrentUserId().toString();
        Call<List<Permission>> caller = proxy.getUserPermissions(id, "PENDING");
        ProxyBuilder.callProxy(PermissionsActivity.this, caller, returnedList -> response(returnedList));
    }

    private void getApprovedPermissions() {
        TextView name = findViewById(R.id.txtViewPermissionTitle);
        name.setText("Approved:");
        String id = instance.getCurrentUserId().toString();
        Call<List<Permission>> caller = proxy.getUserPermissions(id, "APPROVED");
        ProxyBuilder.callProxy(PermissionsActivity.this, caller, returnedList -> response(returnedList));
    }

    private void getDeniedPermissions() {
        TextView name = findViewById(R.id.txtViewPermissionTitle);
        name.setText("Denied:");
        String id = instance.getCurrentUserId().toString();
        Call<List<Permission>> caller = proxy.getUserPermissions(id, "DENIED");
        ProxyBuilder.callProxy(PermissionsActivity.this, caller, returnedList -> response(returnedList));
    }

    private void response(List<Permission> returnedList) {
        Log.w(TAG, "Server replied with permission list: " + returnedList.toString());
        displayItems(returnedList);
    }

    private void registerClickCallback() {
        ListView listView = (ListView) findViewById(R.id.listViewPermission);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Approve");
        menu.add("Deny");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        Long permissionId = permissionList.get(position).getId();

        if (item.getTitle().equals("Approve")) {
            approvePermission(permissionId);
        } else if (item.getTitle().equals("Deny")) {
            denyPermission(permissionId);
        }
        return true;
    }

    private void approvePermission(Long permissionId) {
        Call<Permission> caller = proxy.approveDenyPermission(permissionId, "APPROVED");
        ProxyBuilder.callProxy(PermissionsActivity.this, caller, returnedPermission -> response(returnedPermission));
    }

    private void denyPermission(Long permissionId) {
        Call<Permission> caller = proxy.approveDenyPermission(permissionId, "DENIED");
        ProxyBuilder.callProxy(PermissionsActivity.this, caller, returnedPermission -> response(returnedPermission));
    }

    private void response(Permission returnedPermission) {
        Log.w(TAG, "Server replied with permission: " + returnedPermission);
        getPendingPermissions();
    }

    private void displayItems(List<Permission> permissionList) {
        this.permissionList = permissionList;
        stringList = new ArrayList<>();

        for(Permission permission : permissionList) {
            stringList.add(permission.getMessage());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, stringList);
        ListView listView = (ListView) findViewById(R.id.listViewPermission);
        listView.setAdapter(adapter);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, PermissionsActivity.class);
    }
}
