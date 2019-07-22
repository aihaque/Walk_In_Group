package com.example.homepc.walkinggroupapp.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.Group;
import com.example.homepc.walkinggroupapp.Model.Message;
import com.example.homepc.walkinggroupapp.Model.LastGpsLocation;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;

import retrofit2.Call;


/**
 * Main activity of the app. The user will be able to navigate through the application wth the
 * buttons in this activity. This also serves as the "Parent's Dashboard", since there is no
 * distinction between a parent or a child user of the application.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private WGServerProxy proxy;
    private App instance;
    private GoogleApiClient mClient;
    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
        mClient.connect();

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());
        setupUser();
        setupChildListButton();
        setupParentListButton();
        setupMyGroupButton();
        setupGoogleMapsButton();
        setupSourcesButton();
        setupLogoutButton();
        setupEditInfoButton();
        setupMessagesButton();
        getMessagesBackground();
        setupParentDashBoardButton();
        setupStartUploadingBtn();
        setUpSeeGroupBtn();
        setupPermissionsBtn();
        setupSelectWalikngGroupBtn();
        setupStopUploadingbtn();
        setupStoreButton();
    }

    private void setupStoreButton() {
        Button btn = findViewById(R.id.btnStore);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = StoreActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupStopUploadingbtn() {
        Button btn = findViewById(R.id.stopUploading);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(uploadrunnable);
            }
        });
    }

    private void getUpdate() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(getApplicationContext(), " My Lat= "+location.getLatitude()+"Lng ="+location.getLongitude(), Toast.LENGTH_LONG).show();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'z'");
                Date curr = new Date();
                String timestamp = sdf.format(curr);

                LastGpsLocation lastGpsLocation = new LastGpsLocation();
                lastGpsLocation.setLng(location.getLongitude());
                lastGpsLocation.setLat(location.getLatitude());
                lastGpsLocation.setTimestamp(curr);

                instance.getCurrentUser().setLastGpsLocation(lastGpsLocation);

                Call<LastGpsLocation> caller = proxy.setLastGpsLocation(instance.getCurrentUserId(), lastGpsLocation);
                ProxyBuilder.callProxy(getApplicationContext(), caller, returnedLastGpsLocation -> response(returnedLastGpsLocation));
            }
        });
        Group WalkingGroup=instance.getStartWalkingWithTheGroup();
        if(WalkingGroup!=null){
            if(WalkingGroup.getRouteLngArray().size()>1&&WalkingGroup.getRouteLatArray().size()>1){
                Log.i("Seeeeee","GROUP Lat= "+WalkingGroup.getRouteLatArray().get(0)+"Lng ="+WalkingGroup.getRouteLngArray().get(0));
                Log.i("Seeeeee","GROUP des Lat= "+WalkingGroup.getRouteLatArray().get(1)+"Lng ="+WalkingGroup.getRouteLngArray().get(1));
                LatLng myLocation= new LatLng(instance.getCurrentUser().getLastGpsLocation().getLat(),instance.getCurrentUser().getLastGpsLocation().getLng());
                Toast.makeText(getApplicationContext(), " My Lat= "+myLocation.latitude+"Lng ="+myLocation.longitude, Toast.LENGTH_LONG).show();
                LatLng destination=new LatLng(WalkingGroup.getRouteLatArray().get(1),WalkingGroup.getRouteLngArray().get(1));
                Log.i("Seeeeee"," My Lat= "+myLocation.latitude+"Lng ="+myLocation.longitude);
                double desLat=WalkingGroup.getRouteLatArray().get(1);
                double desLng=WalkingGroup.getRouteLngArray().get(1);
                double myLat=instance.getCurrentUser().getLastGpsLocation().getLat();
                double myLng=instance.getCurrentUser().getLastGpsLocation().getLng();
                double latDiff=desLat-myLat;
                double lngDiff=desLng-myLng;
                if(latDiff < 0.003 && latDiff > -0.003 && lngDiff < 0.03 && lngDiff > -0.03){
                    new CountDownTimer(7000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            Toast.makeText(getApplicationContext(),"remaining = "+millisUntilFinished/1000, Toast.LENGTH_LONG).show();
                        }

                        public void onFinish() {
                            Toast.makeText(getApplicationContext(), "i am done", Toast.LENGTH_LONG).show();
                            User curUser=instance.getCurrentUser();
                            curUser.setCurrentPoints(curUser.getCurrentPoints()+10);
                            curUser.setTotalPointsEarned(curUser.getTotalPointsEarned()+10);
                            Call<User> caller=proxy.editUser(curUser.getId(),curUser);
                            Log.i("Seeeee","points= "+curUser.getCellPhone());
                            handler.removeCallbacks(uploadrunnable);
                        }
                    }.start();

                }
            }
        }
    }

    private void setupSelectWalikngGroupBtn() {
        Button btn = findViewById(R.id.startGroupWalking);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = GroupListActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setUpSeeGroupBtn() {
        Button btn = findViewById(R.id.see_group_location);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GroupsMapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupStartUploadingBtn() {
        Button btn = findViewById(R.id.startUploading);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // handler.postDelayed(runnable, 7000);
                if(instance.getCurrentUser() == null) {
                    handler.removeCallbacks(uploadrunnable);
                }
                uploadrunnable.run();
            }
        });
    }

    private void setupParentDashBoardButton() {
        Button btn = findViewById(R.id.parent_dashboard);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ParentMapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupUser() {
        Call<User> caller = proxy.getUserByEmail(instance.getCurrentUserEmail());
        ProxyBuilder.callProxy(MainActivity.this, caller, returnedUser -> response(returnedUser));
    }

    private void response(User user) {
        Log.w(TAG, "Server replied with user: " + user.toString());
        instance.setCurrentUser(user);

        TextView name = findViewById(R.id.txtViewName);
        name.setText(instance.getCurrentUserName());

        if(user.getCurrentPoints() == null) {
            user.setCurrentPoints(100);
            user.setTotalPointsEarned(100);
        }
        TextView points = findViewById(R.id.textViewPoints);
        points.setText(user.getCurrentPoints().toString());

        int numUnread = user.getUnreadMessages().size();
        Button btn = findViewById(R.id.btnMessages);
        btn.setText("Messages (" + numUnread + ")");

        Toast.makeText(getApplicationContext(), user.getCustomJson(), Toast.LENGTH_SHORT).show();
    }

    private void setupChildListButton() {
        Button btn = findViewById(R.id.btnMyChildren);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UserListActivity.makeIntent(MainActivity.this, 1);
                startActivity(intent);
            }
        });
    }

    private void setupParentListButton() {
        Button btn = findViewById(R.id.btnMyParents);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UserListActivity.makeIntent(MainActivity.this, 2);
                startActivity(intent);
            }
        });
    }

    private void setupMyGroupButton() {
        Button btn = findViewById(R.id.btnMyGroups);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MyGroupsActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupGoogleMapsButton() {
        Button btn = findViewById(R.id.btnGoogleMaps);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupSourcesButton() {
        Button btn = findViewById(R.id.btnSources);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SourcesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupLogoutButton() {
        Button btn = findViewById(R.id.btnLogOut);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.setisUserLoggedIn(false);
                instance.setCurrentUser(null);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupEditInfoButton() {
        Button btn = findViewById(R.id.btnEditInfo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditInfoActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setupMessagesButton() {
        Button btn = findViewById(R.id.btnMessages);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MessagesActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupPermissionsBtn() {
        Button btn = (Button) findViewById(R.id.btnPermissions);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PermissionsActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void getMessagesBackground() {
        Handler handler2 = new Handler();
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                getUnreadMessages();
                handler2.postDelayed(this,60000);
            }
        };

        if(instance.getCurrentUser() == null) {
            handler2.removeCallbacks(runnable2);
        }
        handler2.postDelayed(runnable2, 60000);
    }
    private Runnable uploadrunnable=new Runnable() {
        @Override
        public void run() {
            getUpdate();
            handler.postDelayed(this, 7000);
        }
    };
    private void response(LastGpsLocation lastGpsLocation) {
        Log.w(TAG, "Server Replied with: " + lastGpsLocation.toString());
    }

    private void getUnreadMessages() {
        if(instance.getCurrentUser() != null) {
            Long id = instance.getCurrentUserId();
            Call<User> caller = proxy.getUserById(id);
            ProxyBuilder.callProxy(MainActivity.this, caller, returnedList -> response(returnedList));
        }
    }

    private void response(List<Message> returnedList) {
        Log.w(TAG, "Server replied with group list: " + returnedList.toString());
        instance.setMessages(returnedList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUser();
    }
}
