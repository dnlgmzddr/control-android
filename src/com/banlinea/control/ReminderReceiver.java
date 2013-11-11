package com.banlinea.control;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.widget.Toast;

public class ReminderReceiver extends BroadcastReceiver {

	public static final int REMINDER_NOTIFICATION = 1;

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent paramIntent) {

		SharedPreferences settings = context.getSharedPreferences(
				"ReminderSetup", Context.MODE_PRIVATE);
		if (settings.getBoolean("RemindersOn", false)) {

			// Request the notification manager
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			// Create a new intent which will be fired if you click on the
			// notification
			Intent intent = new Intent(context, BalanceActivity.class);

			// Attach the intent to a pending intent
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);

			// Create the notification
			Notification notification = new Notification(
					R.drawable.ic_action_about,
					context.getString(R.string.app_name),
					System.currentTimeMillis());

			long[] vibrate = { 0, 100, 200, 300 };
			notification.vibrate = vibrate;

			notification.ledARGB = Color.RED;
			notification.ledOffMS = 300;
			notification.ledOnMS = 300;

			notification.sound = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

			notification.defaults |= Notification.DEFAULT_LIGHTS;
			// notification.flags |= Notification.FLAG_SHOW_LIGHTS;

			notification
					.setLatestEventInfo(context,
							context.getString(R.string.app_name),
							context.getString(R.string.reminder_message),
							pendingIntent);

			// Fire the notification
			notificationManager.notify(REMINDER_NOTIFICATION, notification);

			// Set next reminder
			// Get new calendar object and set the date to now
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 20);
			calendar.set(Calendar.MINUTE, 00);
			calendar.set(Calendar.SECOND, 00);

			// Retrieve alarm manager from the system
			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			// Every scheduled intent needs a different ID, else it is just
			// executed
			// once
			int id = (int) System.currentTimeMillis();

			// Prepare the intent which should be launched at the date
			Intent intent1 = new Intent(context, ReminderReceiver.class);

			// Prepare the pending intent
			PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context,
					id, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

			// Register the alert in the system. You have the option to define
			// if
			// the device has to wake up on the alert or not
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pendingIntent1);

			Toast.makeText(context,
					"New Reminder set to" + calendar.toString(),
					Toast.LENGTH_LONG).show();
		}
	}
}
