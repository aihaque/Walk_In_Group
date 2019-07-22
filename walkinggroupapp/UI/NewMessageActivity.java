package com.example.homepc.walkinggroupapp.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.Group;
import com.example.homepc.walkinggroupapp.Model.Message;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import java.util.List;

import retrofit2.Call;

/**
 * A text field that the user will enter his message.
 */
public class NewMessageActivity extends AppCompatActivity {
    private static final String TAG = "NewMessageActivity";
    private WGServerProxy proxy;
    private App instance;

    private static Group group;
    private static int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        switch(mode) {
            case 0:
                setGroupName();
                break;
            case 1:
                setParents();
            case 2:
                setEmergency();
            default:
                break;
        }

        setupSendButton();
    }

    private void setupSendButton() {
        Button btn = findViewById(R.id.btnSendMessage);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageBox = (EditText) findViewById(R.id.editTextMessageBox);
                String message = messageBox.getText().toString();
                sendMessage(message);
            }
        });
    }

    private void sendMessage(String text) {
        String message = text;
        Message newMessage = new Message();
        newMessage.setText(message);
        newMessage.setEmergency(false);

        switch(mode) {
            case 0:
                sendToGroup(newMessage);
                break;
            case 1:
                sendToParents(newMessage);
                break;
            case 2:
                newMessage.setEmergency(true);
                sendToParents(newMessage);
            default:
                break;
        }
    }

    private void sendToGroup(Message newMessage) {
        Call<Message> caller = proxy.sendMessageToGroup(group.getId(), newMessage);
        ProxyBuilder.callProxy(NewMessageActivity.this, caller, returnedMessage -> response(returnedMessage));
    }

    private void sendToParents(Message newMessage) {
        Call<Message> caller = proxy.sendMessageToParents(instance.getCurrentUserId(), newMessage);
        ProxyBuilder.callProxy(NewMessageActivity.this, caller, returnedMessage -> response(returnedMessage));
    }

    private void sendToGroupLeader(Message newMessage) {
        Call<Message> caller = proxy.sendMessageToGroup(group.getId(), newMessage);
        ProxyBuilder.callProxy(NewMessageActivity.this, caller, returnedMessage -> response(returnedMessage));
    }

    private void response(Message message) {
        Log.w(TAG, "Server replied with message: " + message.toString());
        finish();
    }

    public static Intent makeIntent(Context context, int messageMode, Object object) {
        mode = messageMode;

        switch(messageMode) {
            case 0: //broadcast mode
                group = (Group) object;
                break;
            default:
                break;
        }

        return new Intent(context, NewMessageActivity.class);
    }

    private void setGroupName() {
        TextView sendTo = (TextView) findViewById(R.id.txtViewTo);
        sendTo.setText("To: " + group.getGroupDescription());
    }

    private void setParents() {
        TextView sendTo = (TextView) findViewById(R.id.txtViewTo);
        sendTo.setText("To Parents: ");
    }

    private void setEmergency() {
        TextView sendTo = (TextView) findViewById(R.id.txtViewTo);
        sendTo.setText("EMERGENCY MESSAGE:");
    }
}
