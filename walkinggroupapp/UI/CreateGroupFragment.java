package com.example.homepc.walkinggroupapp.UI;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.Group;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

/**
 * Creates a fragment window to support creation of groups from MapsActivity.
 */
public class CreateGroupFragment extends AppCompatDialogFragment {
    private App instance;
    private WGServerProxy proxy;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        instance=App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());
        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.create_group_window,null);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        EditText findingGroupName = (EditText) v.findViewById(R.id.editgroupname);
                        String groupName = findingGroupName.getText().toString();
                        EditText findingNumberOfGroupMembers = (EditText) v.findViewById(R.id.edit_initial_number_of_group_members);
                        String NumberOfGroupMembers = findingNumberOfGroupMembers.getText().toString();
                        int InitialNumberOfGroupMembers = 0;
                        if(NumberOfGroupMembers.length()>0){
                            InitialNumberOfGroupMembers = Integer.parseInt(NumberOfGroupMembers);
                        }

                        instance.setInitialNumberOfGroupMembers(InitialNumberOfGroupMembers);
                        instance.setCreatingGroup(new Group());
                        instance.getCreatingGroup().setGroupDescription(groupName);
                        instance.getCreatingGroup().setLeader(instance.getCurrentUser());
                        instance.getCreatingGroup().addRouteLat(instance.getGroupLatitude());
                        instance.getCreatingGroup().addRouteLng(instance.getGroupLongitude());

                        Call<Group> caller = proxy.createNewGroup(instance.getCreatingGroup());
                        ProxyBuilder.callProxy(getContext(), caller, returnedGroup -> response(returnedGroup));

                        if(InitialNumberOfGroupMembers > 0){
                            int x = 0;

                            while(x < instance.getInitialNumberOfGroupMembers()){
                                FragmentManager manager2 = getFragmentManager();
                                CreateInitialGroupMemberFragment window2 = new CreateInitialGroupMemberFragment();
                                window2.show(manager2, "tag2");
                                x++;
                            }
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle("Creating Group on this destination")
                .setView(v)
                .setPositiveButton(android.R.string.ok,listener)
                .setNegativeButton(android.R.string.cancel,listener)
                .create();
    }

    private void response(Group returnedGroup) {
        instance.setCreatingGroup(returnedGroup);
    }

}
