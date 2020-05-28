package com.example.l6ps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
Button btnAdd, btnCancel;
EditText etName, etDesc, etTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);
        etTime = findViewById(R.id.etTime);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = etName.getText().toString().trim();
                String desc = etDesc.getText().toString().trim();
                String time = etTime.getText().toString();
                int timeInt = Integer.parseInt(time);
                DBHelper dbh = new DBHelper(AddActivity.this);
                long insert = dbh.insertTask(taskName, desc);
                if(insert != -1){
                    Toast.makeText(AddActivity.this, "Inserted", Toast.LENGTH_LONG).show();
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.SECOND, timeInt);

                    Intent i = new Intent(AddActivity.this, ScheduledNotificationReceiver.class);
                    int reqCode = 12345;
                    i.putExtra("name", taskName);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivity.this,reqCode,
                            i, PendingIntent.FLAG_CANCEL_CURRENT);

                    AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
