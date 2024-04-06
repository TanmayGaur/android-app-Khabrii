package com.example.khabrii;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText getFwdPhone;
    private EditText getFromPhone;
    private EditText getMsgContains;
    public static String fwdPhone = "0000";
    public static String msgContains = "empty";
    public static String fromPhone = "1234";
    public static String savedFwdPhone;
    public static String savedMsgContains ;
    public static String savedFromPhone  ;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch serviceSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_SMS},
                PackageManager.PERMISSION_GRANTED);

        textView= findViewById(R.id.textview);
        getFwdPhone = findViewById(R.id.editTextPhone);
        getMsgContains=findViewById(R.id.editTextText);
        getFromPhone= findViewById(R.id.editTextPhone2);
        Button submitBtn = findViewById(R.id.submitBtn);

        getSavedInputs();

        serviceSwitch = findViewById(R.id.serviceSwitch);
        serviceSwitch.setChecked(isServiceRunning());
        serviceSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                startForegroundService();
                Log.d("TAG", "onCreate: fg is running");
            } else {
                stopForegroundService();
                Log.d("TAG", "onCreate: fs is stopped");
            }
        });
        submitBtn.setOnClickListener(view -> {
            fwdPhone = String.valueOf(getFwdPhone.getText());
            msgContains= String.valueOf(getMsgContains.getText());
            fromPhone= String.valueOf(getFromPhone.getText());
            saveInputs();
            getSavedInputs();
        });
    }

    private void saveInputs() {
        SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
        editor.putString("fwd_phone", fwdPhone);
        editor.putString("msg_contains", msgContains);
        editor.putString("from_phone", fromPhone);
        editor.apply();
            }

    private void getSavedInputs() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        savedFwdPhone= sharedPreferences.getString("fwd_phone", fwdPhone);
        savedMsgContains= sharedPreferences.getString("msg_contains", msgContains);
        savedFromPhone= sharedPreferences.getString("from_phone", fromPhone);
        String text = "Forward to: "+ savedFwdPhone +"\nContains: "+savedMsgContains+"\nFrom: "+savedFromPhone;
        textView.setText(text);
    }

    @SuppressWarnings("deprecation")
    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MyForegroundService.class.getName().equals(service.service.getClassName())) {
                Log.d("TAG", "isServiceRunning: true");
                return true; // Service is running
            }
        }
        Log.d("TAG", "isServiceRunning: false");
        return false; // Service is not running
    }

    private void startForegroundService() {
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        startForegroundService(serviceIntent);
    }

    private void stopForegroundService() {
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        stopService(serviceIntent);
    }
}