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
import android.widget.Toast;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.Message;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Activity that display messages for the current user. Has buttons to change the view
 * to ALL, UNREAD, and READ messages. Also has buttons to create a new message for the
 * corresponding parents, group broadcast, and the "PANIC!" button.
 */
public class MessagesActivity extends AppCompatActivity {
    private static final String TAG = "MessagesActivity";

    private static WGServerProxy proxy;
    private static App instance;

    private List<Message> messageList;
    private List<String> stringList;
    private static int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        setupBroadcastButton();
        setupParentsButton();
        setupPanicButton();
        getUserMessages();
        setupAllMessageButton();
        setupUnreadMessagesButton();
        setupReadMessagesButton();
        registerClickCallback();
    }

    private void registerClickCallback() {
        ListView listView = (ListView) findViewById(R.id.listViewMessages);
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
        menu.add("Mark as Read");
        menu.add("Mark as Unread");
        menu.add("Delete (All users)");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        Long messageId = messageList.get(position).getId();

        if (item.getTitle().equals("Mark as Read")) {
            setReadMessage(messageId);
        } else if (item.getTitle().equals("Mark as Unread")) {
            setUnreadMessage(messageId);
        } else if (item.getTitle().equals("Delete (All users)")) {
            deleteMessage(messageId);
        }

        return true;
    }

    private void setupParentsButton() {
        Button btn = (Button) findViewById(R.id.btnMessageParents);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(instance.hasNoParents()) {
                    showErrorMessage(0);
                } else {
                    Intent intent = NewMessageActivity.makeIntent(MessagesActivity.this, 1, null);
                    startActivity(intent);
                }
            }
        });
    }

    private void setupBroadcastButton() {
        Button btn = (Button) findViewById(R.id.btnBroadcast);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(instance.hasNoGroups()) {
                    showErrorMessage(1);
                } else {
                    Intent intent = SendToActivity.makeIntent(MessagesActivity.this, 0);
                    startActivity(intent);
                }
            }
        });
    }

    private void setupPanicButton() {
        Button btn = (Button) findViewById(R.id.btnPanic);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NewMessageActivity.makeIntent(MessagesActivity.this, 2, null);
                startActivity(intent);
            }
        });
    }

    private void showErrorMessage(int mode) {
        switch(mode) {
            case 0:
                Toast.makeText(this, "Error: User has no parents listed.", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "Error: User is not a member of any group.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void setupReadMessagesButton() {
        Button btn = (Button) findViewById(R.id.btnReadMessages);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReadMessages();
            }
        });
    }

    private void setupUnreadMessagesButton() {
        Button btn = (Button) findViewById(R.id.btnUnreadMessages);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUnreadMessages();
            }
        });
    }

    private void setupAllMessageButton() {
        Button btn = (Button) findViewById(R.id.btnAllMessages);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserMessages();
            }
        });
    }

    private void displayItems() {
        messageList = instance.getMessages();
        stringList = new ArrayList<>();

        for(Message message : messageList) {
            getUserName( message.getFromUser().getId(), message);
        }
    }

    private void getUserMessages() {
        mode = 0;
        String id = instance.getCurrentUserId().toString();
        Call<List<Message>> caller = proxy.getMessagesStatus(id, "");
        ProxyBuilder.callProxy(MessagesActivity.this, caller, returnedList -> response(returnedList));
    }

    private void getUnreadMessages() {
        mode = 1;
        String id = instance.getCurrentUserId().toString();
        Call<List<Message>> caller = proxy.getMessagesStatus(id, "unread");
        ProxyBuilder.callProxy(MessagesActivity.this, caller, returnedList -> response(returnedList));
    }

    private void getReadMessages() {
        mode = 2;
        String id = instance.getCurrentUserId().toString();
        Call<List<Message>> caller = proxy.getMessagesStatus(id, "read");
        ProxyBuilder.callProxy(MessagesActivity.this, caller, returnedList -> response(returnedList));
    }

    private void getUserName(Long id, Message message) {
        Call<User> caller = proxy.getUserById(id);
        ProxyBuilder.callProxy(MessagesActivity.this, caller, returnedList -> displayResponse(returnedList, message));
    }

    private void setReadMessage(Long messageId) {
        Long userId = instance.getCurrentUserId();
        Call<User> caller = proxy.setMessageAsRead(messageId, userId, true);
        ProxyBuilder.callProxy(MessagesActivity.this, caller, returnedList -> response(returnedList));
    }

    private void setUnreadMessage(Long messageId) {
        Long userId = instance.getCurrentUserId();
        Call<User> caller = proxy.setMessageAsRead(messageId, userId, false);
        ProxyBuilder.callProxy(MessagesActivity.this, caller, returnedList -> response(returnedList));
    }

    private void deleteMessage(Long messageId) {
        Long userId = instance.getCurrentUserId();
        Call<Void> caller = proxy.deleteMessage(messageId);
        ProxyBuilder.callProxy(MessagesActivity.this, caller, returnedList -> response(returnedList));
    }

    private void response(User returnedUser) {
        instance.setCurrentUser(returnedUser);
        switch(mode) {
            case 0:
                getUserMessages();
                break;
            case 1:
                getUnreadMessages();
                break;
            case 2:
                getReadMessages();
                break;
            default:
                break;
        }
    }

    private void displayResponse(User returnedUser, Message message) {
        if(message.isEmergency()) {
            stringList.add("From: " + returnedUser.getName() +"\n" +
                    "**EMERGERCY**\n" + message.getText());
        } else {
            stringList.add("From: " + returnedUser.getName() +"\n" + message.getText());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, stringList);
        ListView listView = (ListView) findViewById(R.id.listViewMessages);
        listView.setAdapter(adapter);
    }

    private void response(Void returnedNothing) {
        updateMessageList();
    }

    private void response(List<Message> returnedList) {
        Log.w(TAG, "Server replied with message list: " + returnedList.toString());
        instance.setMessages(returnedList);
        if(returnedList.size() == 0) {

            switch(mode) {
                case 0:
                    Toast.makeText(this, "No messages at the moment", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(this, "No unread messages at the moment", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(this, "No read messages at the moment", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
        displayItems();
    }

    public static Intent makeIntent(Context context) {
        mode = 0;
        return new Intent(context, MessagesActivity.class);
    }

    private void updateMessageList() {
        switch(mode) {
            case 0:
                getUserMessages();
                break;
            case 1:
                getUnreadMessages();
                break;
            case 2:
                getReadMessages();
                break;
            default:
                break;
        }
    }

}
