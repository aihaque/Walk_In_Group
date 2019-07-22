package com.example.homepc.walkinggroupapp.UI;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import java.util.List;

import retrofit2.Call;

/**
 * Users can be added right away with this fragment when the group is initially created.
 */
public class CreateInitialGroupMemberFragment extends AppCompatDialogFragment {
    private App instance = App.getInstance();
    private WGServerProxy proxy;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());
        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.create_initial_group_member_window,null);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        EditText find = (EditText) v.findViewById(R.id.edit_group_memeber_email);
                        String email = find.getText().toString();

                        while(email == ""){
                            Toast.makeText(getContext(),"Email must be a valid email address.",Toast.LENGTH_LONG).show();
                            find = (EditText) v.findViewById(R.id.edit_group_memeber_email);
                            email = find.getText().toString();
                        }

                        Call<User> caller = proxy.getUserByEmail(email);
                        ProxyBuilder.callProxy(getActivity(), caller, returnedUser -> response(returnedUser));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle("Add member to the group.")
                .setView(v)
                .setPositiveButton(android.R.string.ok,listener)
                .setNegativeButton(android.R.string.cancel,listener)
                .create();
    }

    private void response(User returnedUser) {
        Call<List<User>> caller = proxy.addMemberToGroup(instance.getCreatingGroup().getId(), returnedUser);
        ProxyBuilder.callProxy(getActivity(), caller, returnedUserList -> response(returnedUserList));
    }

    private void response(List<User> userList) {
        Log.w("example", "Server replied with user list: " + userList.toString());
    }
}
