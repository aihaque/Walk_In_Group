package com.example.homepc.walkinggroupapp.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.Group;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

/**
 * Displays the in-depth information of a user.
 */
public class UserInfoActivity extends AppCompatActivity {
    private static final String TAG = "UserInfoActivity";
    private WGServerProxy proxy;
    private App instance;

    private static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        setDisplays();
        setupBackButton();
    }

    private void setDisplays() {
        TextView name = findViewById(R.id.textViewNameDisplay);
        TextView email = findViewById(R.id.textViewEmailDisplay);
        TextView address = findViewById(R.id.textViewAddressDisplay);
        TextView homePhone = findViewById(R.id.textViewHomePhoneDisplay);
        TextView phone = findViewById(R.id.textViewPhoneDisplay);
        TextView birthMonth = findViewById(R.id.textViewBirthMonthDisplay);
        TextView birthYear = findViewById(R.id.textViewBirthYearDisplay);
        TextView teacher_name = findViewById(R.id.textViewTeacherNameDisplay);
        TextView grade = findViewById(R.id.textViewGradeDisplay);
        TextView emergencyInfo = findViewById(R.id.textViewEmergencyInfoDisplay);

        name.setText(user.getName());
        email.setText(user.getEmail());
        address.setText(user.getAddress());
        homePhone.setText(user.getHomePhone());
        phone.setText(user.getCellPhone());
        birthMonth.setText(user.getBirthMonth());
        birthYear.setText(user.getBirthYear());
        teacher_name.setText(user.getTeacherName());
        grade.setText(user.getGrade());
        emergencyInfo.setText(user.getEmergencyContactInfo());
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

    public static Intent makeIntent(Context context, User newUser){
        user = newUser;
        return new Intent(context, UserInfoActivity.class);
    }
}
