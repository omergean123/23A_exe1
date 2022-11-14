package com.omer.a23a_exe1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final int STORAGE_PERMISSION_CODE = 900;
//    private final int REQUEST_CODE_PERMISSION_BLUETOOTH = 901;
//    private final int REQUEST_CODE_PERMISSION_VOLUME = 902;
//    private final int REQUEST_CODE_PERMISSION_MICROPHONE = 903;
    private Button BTN_Login;
    private EditText EDT_password;
//    private Button BTN_Request;
    NetworkInfo isActive;
    ConnectivityManager myCm;
    AudioManager myAudM,myAudVolume;
    private  int batLevel;


    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = MainActivity.this;
        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Log.d("pttt",String.valueOf(batLevel));
        initViews();
        initButtons();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void initViews() {
        BTN_Login = findViewById(R.id.BTN_Login);
        EDT_password = findViewById(R.id.EDT_password);
//        BTN_Request =  findViewById(R.id.BTN_Request);

        myCm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        myAudM = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        myAudVolume = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
    }
    private void initButtons() {
//        BTN_Request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(ContextCompat.checkSelfPermission(MainActivity.this,
//                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(MainActivity.this, "You have already granted this permission", Toast.LENGTH_SHORT).show();
//                }else{
//                    requestStoragePermission();
//                }
//            }
//        });

        BTN_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(VerticalCheck() && isWifiOn() && isVibrateMode() && isSoundMax() && isBatteryCorrect()){
                    Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isBatteryCorrect() {
        if(Integer.parseInt(EDT_password.getText().toString()) == batLevel){
            return true;
        }
        else return false;
    }

    private boolean isSoundMax() {
        int origionalVolume = myAudVolume.getStreamVolume(AudioManager.STREAM_MUSIC);
        if(origionalVolume == 15){
            return true;
        }
        return false;
    }

    private boolean isWifiOn() {
        isActive = myCm.getActiveNetworkInfo();
        if(isActive != null){
            if(isActive.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }else{
                return false;
            }
        }
        return false;
    }
    private boolean isVibrateMode() {
        switch (myAudM.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
            case AudioManager.RINGER_MODE_NORMAL:
                return false;
            case AudioManager.RINGER_MODE_VIBRATE:
                return true;
        }
        return false;
    }

    private void requestStoragePermission() { //if user deny permission first time
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is a MUST!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "This permission grantad succesfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "This permission DENIED!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean VerticalCheck() {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            return true;
        }
        else return false;
    }
    public static int getBatteryPercentage(Context context) {

        if (Build.VERSION.SDK_INT >= 21) {

            BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
            return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        } else {

            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, iFilter);

            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

            double batteryPct = level / (double) scale;

            return (int) (batteryPct * 100);
        }
    }
}