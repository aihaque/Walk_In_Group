package com.example.homepc.walkinggroupapp.UI;

import android.app.FragmentManager;
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
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import java.util.ArrayList;

import retrofit2.Call;

public class StoreActivity extends AppCompatActivity {
    private static final String TAG = "StoreActivity";
    private WGServerProxy proxy;
    private App instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        TextView points = findViewById(R.id.textViewPoints);
        points.setText(instance.getCurrentUser().getCurrentPoints().toString());

        setupLists();
        setupBackButton();
    }

    private void setupLists() {
        ArrayList<String> titleList = new ArrayList<>();

        titleList.add("KING");
        titleList.add("QUEEN");
        titleList.add("JOKER");

        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(this, R.layout.item, titleList);
        ListView titleListView = findViewById(R.id.listViewTitles);
        titleListView.setAdapter(titleAdapter);

        titleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView titleTextView = (TextView) view;

                if(instance.getCurrentUser().getCurrentPoints() < 50) {
                    Toast.makeText(StoreActivity.this, "Insuffucuent points", Toast.LENGTH_LONG).show();
                }
                else {
                    instance.getCurrentUser().setCurrentPoints(instance.getCurrentUser().getCurrentPoints() - 50);
                    if(position == 0) {
                        instance.getCurrentUser().setName("KING " + instance.getCurrentUser().getName());
                    }
                    else if(position == 1) {
                        instance.getCurrentUser().setName("QUEEN " + instance.getCurrentUser().getName());
                    }
                    else if(position == 2) {
                        instance.getCurrentUser().setName("JOKER" + instance.getCurrentUser().getName());
                    }
                }


                Call<User> caller = proxy.editUserById(instance.getCurrentUserId(), instance.getCurrentUser());
                ProxyBuilder.callProxy(StoreActivity.this, caller, returnedUser -> response(returnedUser));
            }
        });
    }

    public void response(User returnedUser) {
        instance.setCurrentUser(returnedUser);

        TextView points = findViewById(R.id.textViewPoints);
        points.setText(instance.getCurrentUser().getCurrentPoints().toString());

        Log.i("PURCHASE", "Server recorded purchase");
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

    public static Intent makeIntent(Context context) {
        return new Intent(context, StoreActivity.class);
    }
}
