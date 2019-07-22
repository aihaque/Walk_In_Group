package com.example.homepc.walkinggroupapp.UI;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.Group;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class EditGroupActivity extends AppCompatActivity {
    private static Group group;
    private WGServerProxy proxy;
    private App instance;
    private static Long groupId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        instance=App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());
        setupOKbtn();
    }

    private void setupOKbtn() {
        Button btn =(Button) findViewById(R.id.OK_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setuplocation();
                Call<Group> caller = proxy.updateGroupDetails(groupId,group);
                ProxyBuilder.callProxy(getApplicationContext(), caller, returnedGroup -> response(returnedGroup));
            }
        });
    }

    private void response(Group returnedGroup) {
        return;
    }

    private void setuplocation() {
        EditText findingGroupLocation=(EditText) findViewById(R.id.edit_group_location);
        EditText findingGroupDestination=(EditText) findViewById(R.id.edit__group_destination);
        String groupLocation=findingGroupLocation.getText().toString();
        String groupDestination=findingGroupDestination.getText().toString();
        List<Address> addressList = null;
        List<Address> addressList2 = null;
        List<Double> routeLatArray=group.getRouteLatArray();
        List<Double> routeLngArray=group.getRouteLatArray();
        if (groupLocation.length()>0) {
            Geocoder geocoder = new Geocoder(this);

            try {
                addressList = geocoder.getFromLocationName(groupLocation, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
          if(addressList.size()>0){
              Address address = addressList.get(0);
              if(routeLatArray.size()>0&&routeLngArray.size()>0){
                  routeLatArray.set(0,address.getLatitude());
                  routeLngArray.set(0,address.getLongitude());
              }
              else if(routeLatArray.size()<=0&&routeLngArray.size()<=0){
                  group.addRouteLng(address.getLongitude());
                  group.addRouteLat(address.getLatitude());
              }
              Log.i("Group location","Lat= "+address.getLatitude());
          }
          else if(addressList.size()<=0){
              Toast.makeText(getApplicationContext(),"Address does not found please type a valid address",Toast.LENGTH_LONG).show();
          }
        }
        if (groupDestination.length()>0) {
            Geocoder geocoder = new Geocoder(this);

            try {
                addressList2 = geocoder.getFromLocationName(groupDestination, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList2.size()>0){
                Address address2 = addressList2.get(0);
                if(routeLatArray.size()>1&&routeLngArray.size()>1){
                    routeLatArray.set(1,address2.getLatitude());
                    routeLngArray.set(1,address2.getLongitude());
                }
                else if(routeLatArray.size()==1&&routeLngArray.size()==1){
                    group.addRouteLng(address2.getLongitude());
                    group.addRouteLat(address2.getLatitude());
                }
                Log.i("Group location","Lat= "+address2.getLatitude());
            }
            else if(addressList2.size()<=0){
                Toast.makeText(getApplicationContext(),"Address does not found please type a valid address",Toast.LENGTH_LONG).show();
            }
        }
    }

    public static Intent makeintent(Context Context,Group newGroup) {
        group=newGroup;
        groupId=group.getId();
        return new Intent(Context,EditGroupActivity.class);
    }
}
