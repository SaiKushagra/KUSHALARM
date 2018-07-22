package com.example.saiku.amitalarm;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saiku.amitalarm.R;
import com.example.saiku.amitalarm.VibrateService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    int done = 1;

    private static void myTask() {
        System.out.println("Running");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void alarm(View view) {
        long wait_time = seconds();
        long wait_time2 = seconds();
        long millis = System.currentTimeMillis();
        long millis2 = System.currentTimeMillis();
        long check_time = wait_time + millis;
        while (System.currentTimeMillis() <= check_time) {
            if (check_time == System.currentTimeMillis()) {
                Intent intent = new Intent(this, VibrateService.class);
                startService(intent);


                new Thread(){
                    public void run(){
                        try {
                            sleep(15000);
                            Intent o=new Intent (MainActivity.this,Main2Activity.class);
                            startActivity(o);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }
    }

    public void onCall() {
        long check_time1 = System.currentTimeMillis();
        while (System.currentTimeMillis() <= check_time1)
            if (check_time1 == System.currentTimeMillis()) {
                if (done == 2) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
                    callIntent.setData(Uri.parse("tel:9949197203"));    //this is the phone number calling
                    //check permission
                    //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
                    //the system asks the user to grant approval.
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        //request permission from user if the app hasn't got the required permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                                10);
                    } else {     //have got permission
                        try {
                            startActivity(callIntent); //call activity and make phone call
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    }

    public long seconds() {
        EditText time_wait = (EditText) findViewById(R.id.sec);
        long time = Long.parseLong(time_wait.getText().toString());
        time = time * 1000;
        return time;
    }

    public void stop(View view) {
        done = done + 1;
        onCall();
    }
}
