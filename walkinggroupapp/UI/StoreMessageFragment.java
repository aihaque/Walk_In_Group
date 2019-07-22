package com.example.homepc.walkinggroupapp.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.homepc.walkinggroupapp.R;

public class StoreMessageFragment extends AppCompatDialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Create the view to show
        View v = LayoutInflater.from(getActivity())
                 .inflate(R.layout.store_message, null);

        //Create the listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("PURCHASE", "User wants to purchase something from store");
            }
        };

        //Build the alert dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("Purchase Request")
                .setView(v)
                .setPositiveButton(android.R.string.ok, listener)
                .create();
    }
}
