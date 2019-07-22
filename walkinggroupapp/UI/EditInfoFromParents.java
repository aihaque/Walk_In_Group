package com.example.homepc.walkinggroupapp.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.homepc.walkinggroupapp.Model.App;
import com.example.homepc.walkinggroupapp.Model.Group;
import com.example.homepc.walkinggroupapp.Model.ProxyBuilder;
import com.example.homepc.walkinggroupapp.Model.User;
import com.example.homepc.walkinggroupapp.Model.WGServerProxy;
import com.example.homepc.walkinggroupapp.R;

import java.util.List;

import retrofit2.Call;

/**
 * Edit the in-depth information of a "child" from the child list.
 */
public class EditInfoFromParents extends AppCompatActivity {
    private static final String TAG = "Edit Info From Parents";
    private WGServerProxy proxy;
    private App instance;

    private static Long currentChildId;
    private User currentChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info_from_parents);

        instance = App.getInstance();
        proxy = ProxyBuilder.getProxy(instance.getApiKey(), instance.getToken());

        getCurrentChild();
        setupExitEditInfoButton();
        setupSaveInfoButton();
    }

    private void getCurrentChild() {
        Call<User> caller = proxy.getUserById(currentChildId);
        ProxyBuilder.callProxy(EditInfoFromParents.this, caller, returnedUser -> response(returnedUser));
    }

    private void response(User returnedUser) {
        currentChild = returnedUser;
        updateTextFields();
    }

    private void updateTextFields() {
        EditText currentName=(EditText)findViewById(R.id.textEditName2);
        currentName.setText(currentChild.getName());

        EditText currentAddress=(EditText)findViewById(R.id.textEditAddress2);
        currentAddress.setText(currentChild.getAddress());

        EditText currentEmail=(EditText)findViewById(R.id.textEditEmail2);
        currentEmail.setText(currentChild.getEmail());

        EditText currentPhoneNumber=(EditText)findViewById(R.id.textEditPhoneNumber2);
        currentPhoneNumber.setText(currentChild.getCellPhone());

        EditText currentEmergencyInfo=(EditText)findViewById(R.id.textEditEmergencyInfo2);
        currentEmergencyInfo.setText(currentChild.getEmergencyContactInfo());

        EditText currentHomePhone=(EditText)findViewById(R.id.textEditHomeNumber2);
        currentHomePhone.setText(currentChild.getHomePhone());

        EditText currentBirthYear=(EditText)findViewById(R.id.textEditBirthYear2);
        currentBirthYear.setText(currentChild.getBirthYear());

        EditText currentBirthMonth=(EditText)findViewById(R.id.textEditBirthMonth2);
        currentBirthMonth.setText(currentChild.getBirthMonth());

        EditText currentGrade=(EditText)findViewById(R.id.textEditGrade2);
        currentGrade.setText(currentChild.getGrade());

        EditText currentTeacherName=(EditText)findViewById(R.id.textEditTeacherName2);
        currentTeacherName.setText(currentChild.getTeacherName());
    }

    private void setupExitEditInfoButton() {
        Button btn = (Button) findViewById(R.id.btnLeaveWithoutSaveInfo2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {finish();}
        });
    }

    private void setupSaveInfoButton() {
        Button btn = (Button) findViewById(R.id.btnSaveInfo2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewInformation();
                Call<User> caller = proxy.editUserById(currentChildId, currentChild);
                ProxyBuilder.callProxy(EditInfoFromParents.this, caller, returnedUser -> response(returnedUser));
                finish();


            }
        });
    }

    private void getNewInformation() {
        EditText n = (EditText) findViewById(R.id.textEditName2);
        EditText em = (EditText) findViewById(R.id.textEditEmail2);
        EditText ad = (EditText) findViewById(R.id.textEditAddress2);
        EditText pn= (EditText) findViewById(R.id.textEditPhoneNumber2);
        EditText emInfo = (EditText) findViewById(R.id.textEditEmergencyInfo2);
        EditText y= (EditText) findViewById(R.id.textEditBirthYear2);
        EditText m = (EditText) findViewById(R.id.textEditBirthMonth2);
        EditText g = (EditText) findViewById(R.id.textEditGrade2);
        EditText tn= (EditText) findViewById(R.id.textEditTeacherName2);

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
        if(name.length() == 0) {name = currentChild.getName();}
        if (email.length() == 0) {email = currentChild.getEmail();}
        if (address.length() == 0) {address = currentChild.getAddress();}
        if(phoneNumber.length() == 0) {phoneNumber = currentChild.getCellPhone();}
        if(emergencyInfo.length() == 0) {emergencyInfo = currentChild.getEmergencyContactInfo();}
        if(year.length() == 0) {year = currentChild.getBirthYear();}
        if (month.length() == 0) {month = currentChild.getBirthMonth();}
        if(grade.length() == 0) {grade = currentChild.getGrade();}
        if(teacherName.length() == 0) {teacherName = currentChild.getTeacherName();}

        //reset users' info
        currentChild.setName(name);
        currentChild.setEmail(email);
        currentChild.setAddress(address);
        currentChild.setCellPhone(phoneNumber);
        currentChild.setEmergencyContactInfo(emergencyInfo);
        currentChild.setBirthYear(year);
        currentChild.setBirthMonth(month);
        currentChild.setGrade(grade);
        currentChild.setTeacherName(teacherName);
    }

    public static Intent makeIntent(Context context, Long childId){
        currentChildId = childId;
        return new Intent(context, EditInfoFromParents.class);
    }
}
