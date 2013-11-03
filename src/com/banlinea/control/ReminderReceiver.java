package com.banlinea.control;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;

public class ReminderReceiver extends BroadcastReceiver {

	public static final int REMINDER_NOTIFICATION = 1;
	
	@Override
	public void onReceive(Context context, Intent paramIntent) {
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
				R.drawable.ic_action_about, context.getString(R.string.app_name),
				System.currentTimeMillis());
		
		long[] vibrate = {0,100,200,300};
        notification.vibrate = vibrate;
         
        notification.ledARGB = Color.RED;
        notification.ledOffMS = 300;
        notification.ledOnMS = 300;
        
        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
         
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        //notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        
		
		notification.setLatestEventInfo(context, context.getString(R.string.app_name),
				context.getString(R.string.reminder_message), pendingIntent);

		// Fire the notification
		notificationManager.notify(REMINDER_NOTIFICATION, notification);
	}
}
