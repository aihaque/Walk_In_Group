package com.example.homepc.walkinggroupapp.UI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;

/**
 * Makes use of Google Maps to view the last GPS location of the children that the user is
 * monitoring.
 */
public class ParentMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private WGServerProxy proxy;
    private App instance = App.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mMap.setMyLocationEnabled(true);
        Call<List<User>> caller = proxy.getMonitorUser(instance.getCurrentUserId());
        ProxyBuilder.callProxy(ParentMapActivity.this, caller, returnedUserList -> response(returnedUserList));
    }

    private void response(List<User> userList) {
        for(User user : userList){
            LatLng latLng = new LatLng(user.getLastGpsLocation().getLat(), user.getLastGpsLocation().getLng());

            if(user.getLastGpsLocation().getTimestamp() != null){
                mMap.addMarker(new MarkerOptions().position(latLng).title(user.getName() + ": " +user.getLastGpsLocation().getTimestamp().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                Log.i("TAG",user.getName() + " lat = " + user.getLastGpsLocation().getLat() + "DATE = "+user.getLastGpsLocation().getTimestamp().toString());
            }
        }
    }
}
