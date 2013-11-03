package com.banlinea.control;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ReminderSetupActivity extends Activity {
	
	private ToggleButton setRemindersToggle;
	private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_setup);
        
        Log.d("Calendar Time", "" + Calendar.getInstance().toString());
        
        setRemindersToggle = (ToggleButton) findViewById(R.id.set_reminders_toggle);
        
        continueButton = (Button) findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (setRemindersToggle.isChecked()) {
					createScheduledNotification();
					Toast.makeText(getApplicationContext(), "Recordatorio ajustado", Toast.LENGTH_SHORT).show();
				}
				
				
				if (ReminderSetupActivity.this.getIntent().getBooleanExtra("suggestSetup", false)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(ReminderSetupActivity.this);
					builder.setTitle(R.string.initial_setup_prompt_title).setMessage(R.string.initial_setup_prompt);
					builder.setPositiveButton(R.string.continue_text, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(ReminderSetupActivity.this, InitialSetupActivity.class);
							startActivity(intent);
							ReminderSetupActivity.this.finish();
						}
					});
					builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ReminderSetupActivity.this.finish();
						}
					});
					builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface dialog) {
							ReminderSetupActivity.this.finish();
						}
					});
					
					AlertDialog dialog = builder.create();
					dialog.show();
				}
				else {
					ReminderSetupActivity.this.finish();
				}
			}
		});
        
        
    }
    
    private void createScheduledNotification()
    {
		// Get new calendar object and set the date to now
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 20);
		calendar.set(Calendar.MINUTE, 54);
		calendar.set(Calendar.SECOND, 00);
		
		// Retrieve alarm manager from the system
		AlarmManager alarmManager = (AlarmManager) ReminderSetupActivity.this
				.getSystemService(getBaseContext().ALARM_SERVICE);
		// Every scheduled intent needs a different ID, else it is just executed
		// once
		int id = (int) System.currentTimeMillis();

		// Prepare the intent which should be launched at the date
		Intent intent = new Intent(ReminderSetupActivity.this, ReminderReceiver.class);

		// Prepare the pending intent
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Register the alert in the system. You have the option to define if
		// the device has to wake up on the alert or not
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reminder_setup, menu);
        return true;
    }
    
}
