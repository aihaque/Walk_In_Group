package com.example.homepc.walkinggroupapp.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import retrofit2.Call;

/**
 * Activity to edit the current user's in-depth information.
 */
public class EditInfoActivity extends AppCompatActivity {
    private static final String TAG = "Edit Info";
    private WGServerProxy proxy;
    private App instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        //Initial info from user
        EditText currentName=(EditText)findViewById(R.id.textEditName);
        currentName.setText(instance.getCurrentUserName());

        EditText currentAddress=(EditText)findViewById(R.id.textEditAddress);
        currentAddress.setText(instance.getCurrentUserAddress());

        EditText currentEmail=(EditText)findViewById(R.id.textEditEmail);
        currentEmail.setText(instance.getCurrentUserEmail());

        EditText currentPhoneNumber=(EditText)findViewById(R.id.textEditPhoneNumber);
        currentPhoneNumber.setText(instance.getCurrentUserCellPhone());

        EditText currentEmergencyInfo=(EditText)findViewById(R.id.textEditEmergencyInfo);
        currentEmergencyInfo.setText(instance.getCurrentUserEmergencyContactInfo());

        EditText currentHomePhone=(EditText)findViewById(R.id.textEditHomeNumber);
        currentHomePhone.setText(instance.getCurrentUserHomePhone());

        EditText currentBirthYear=(EditText)findViewById(R.id.textEditBirthYear);
        currentBirthYear.setText(instance.getCurrentUserBirthYear());

        EditText currentBirthMonth=(EditText)findViewById(R.id.textEditBirthMonth);
        currentBirthMonth.setText(instance.getCurrentUserBirthMonth());

        EditText currentGrade=(EditText)findViewById(R.id.textEditGrade);
        currentGrade.setText(instance.getCurrentUserGrade());

        EditText currentTeacherName=(EditText)findViewById(R.id.textEditTeacherName);
        currentTeacherName.setText(instance.getCurrentUserTeacherName());

        setupExitEditInfoButton();
        setupSaveInfoButton();
    }

    private void setupExitEditInfoButton() {
        Button btn = (Button) findViewById(R.id.btnLeaveWithoutSaveInfo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {finish();}
        });
    }

    private void setupSaveInfoButton() {
        Button btn = (Button) findViewById(R.id.btnSaveInfo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if the user is a teacher or parents
                if (searchId(instance.getCurrentUserId()) == true){

                    EditText n = (EditText) findViewById(R.id.textEditName);
                    EditText em = (EditText) findViewById(R.id.textEditEmail);
                    EditText ad = (EditText) findViewById(R.id.textEditAddress);
                    EditText pn= (EditText) findViewById(R.id.textEditPhoneNumber);
                    EditText emInfo = (EditText) findViewById(R.id.textEditEmergencyInfo);
                    EditText y= (EditText) findViewById(R.id.textEditBirthYear);
                    EditText m = (EditText) findViewById(R.id.textEditBirthMonth);

                    String name = n.getText().toString();
                    String email = em.getText().toString();
                    String address = ad.getText().toString();
                    String phoneNumber= pn.getText().toString();
                    String emergencyInfo= emInfo.getText().toString();
                    String year = y.getText().toString();
                    String month = m.getText().toString();

                    //When the user did not enter any info, set the parameter back to current user info
                    if(name.length() == 0) {name = instance.getCurrentUserName();}
                    if(email.length() == 0) {email = instance.getCurrentUserEmail();}
                    if(address.length() == 0) {address = instance.getCurrentUserAddress();}
                    if(phoneNumber.length() == 0) {phoneNumber = instance.getCurrentUserCellPhone();}
                    if(emergencyInfo.length() == 0) {emergencyInfo = instance.getCurrentUserEmergencyContactInfo();}
                    if(year.length() == 0) {year = instance.getCurrentUserBirthYear();}
                    if(month.length() == 0) {month = instance.getCurrentUserBirthMonth();}

                    //reset users' info
                    instance.getCurrentUser().setName(name);
                    instance.getCurrentUser().setEmail(email);
                    instance.getCurrentUser().setAddress(address);
                    instance.getCurrentUser().setCellPhone(phoneNumber);
                    instance.getCurrentUser().setEmergencyContactInfo(emergencyInfo);
                    instance.getCurrentUser().setBirthYear(year);
                    instance.getCurrentUser().setBirthMonth(month);
                    instance.getCurrentUser().setCurrentPoints(100);
                }

                else {  //user is a student
                    EditText n = (EditText) findViewById(R.id.textEditName);
                    EditText em = (EditText) findViewById(R.id.textEditEmail);
                    EditText ad = (EditText) findViewById(R.id.textEditAddress);
                    EditText pn= (EditText) findViewById(R.id.textEditPhoneNumber);
                    EditText emInfo = (EditText) findViewById(R.id.textEditEmergencyInfo);
                    EditText y= (EditText) findViewById(R.id.textEditBirthYear);
                    EditText m = (EditText) findViewById(R.id.textEditBirthMonth);
                    EditText g = (EditText) findViewById(R.id.textEditGrade);
                    EditText tn= (EditText) findViewById(R.id.textEditTeacherName);

                    String grade = g.getText().toString();
                    String teacherName = tn.getText().toString();
                    String name = n.getText().toString();
                    String email = em.getText().toString();
                    String address = ad.getText().toString();
                    String  phoneNumber= pn.getText().toString();
                    String  emergencyInfo= emInfo.getText().toString();
                    String year = y.getText().toString();
                    String month = m.getText().toString();

                    //When the user did not enter any info, set the parameter back to current user info
                    if(name.length() == 0) {name = instance.getCurrentUserName();}
                    if(email.length() == 0) {email = instance.getCurrentUserEmail();}
                    if(address.length() == 0) {address = instance.getCurrentUserAddress();}
                    if(phoneNumber.length() == 0) {phoneNumber = instance.getCurrentUserCellPhone();}
                    if(emergencyInfo.length() == 0) {emergencyInfo = instance.getCurrentUserEmergencyContactInfo();}
                    if(year.length() == 0) {year = instance.getCurrentUserBirthYear();}
                    if(month.length() == 0) {month = instance.getCurrentUserBirthMonth();}
                    if(grade.length() == 0) {grade = instance.getCurrentUserGrade();}
                    if(teacherName.length() == 0) {teacherName = instance.getCurrentUserTeacherName();}

                    //reset users' info
                    instance.getCurrentUser().setName(name);
                    instance.getCurrentUser().setEmail(email);
                    instance.getCurrentUser().setAddress(address);
                    instance.getCurrentUser().setCellPhone(phoneNumber);
                    instance.getCurrentUser().setEmergencyContactInfo(emergencyInfo);
                    instance.getCurrentUser().setBirthYear(year);
                    instance.getCurrentUser().setBirthMonth(month);
                    instance.getCurrentUser().setGrade(grade);
                    instance.getCurrentUser().setTeacherName(teacherName);
                    instance.getCurrentUser().setCurrentPoints(100);
                }

                Call<User> caller = proxy.editUserById(instance.getCurrentUserId(), instance.getCurrentUser());
                ProxyBuilder.callProxy(EditInfoActivity.this, caller, returnedUser -> response(returnedUser));

                finish();
            }
        });

    }

    private void response(User user) {
        Log.w(TAG, "Server replied with user: " + user.toString());
        instance.setCurrentUser(user);

    }

    public boolean searchId(long id) {
        boolean found = false;
        int index = 0;

        while(found == false && index < instance.getCurrentUserMonitor().size()) {
            if(instance.getCurrentUserMonitor().get(index).getId() == id) {
                found = true;
            }
            index++;
        }

        return found;
    }
}
