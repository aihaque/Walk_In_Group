package com.example.homepc.walkinggroupapp.UI;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.Group;
import com.example.homepc.walkinggroupapp.Model.LastGpsLocation;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

/**
 * The activity that makes use of Google Maps. To create a group in this activity. Long press
 * a location, and a group can be created on the coordinates of that selected location (the pin).
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private WGServerProxy proxy;
    private App instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        setupBackButton();
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

    public void zoom(View view){
        if(view.getId() == R.id.zoom_in){
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }

        if(view.getId() == R.id.zoom_out){
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    public void searchPlaces(View view) {
        EditText findLocation = (EditText) findViewById(R.id.editnamelocation);
        String locationFound = findLocation.getText().toString();

        List<Address> addressList = null;

        if (locationFound != null || locationFound != "") {
            Geocoder geocoder = new Geocoder(this);

            try {
                addressList = geocoder.getFromLocationName(locationFound, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList.size()>0){
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in clicked "+locationFound));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
            else if(addressList.size()<=0){
                Toast.makeText(getApplicationContext(),"Address does not found please type a valid address",Toast.LENGTH_LONG).show();
            }
        }
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
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;

                instance.setGroupLatitude(latitude);
                instance.setGroupLongitude(longitude);

                FragmentManager manager = getSupportFragmentManager();
                CreateGroupFragment window = new CreateGroupFragment();
                window.show(manager, "tag");
                mMap.addMarker(new MarkerOptions().position(latLng).title("latitude =" + latitude + " longitude =" + longitude));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }
}
