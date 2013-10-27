package com.banlinea.control;

import java.lang.reflect.Field;
import java.sql.SQLException;

import com.banlinea.control.bussiness.AuthenticationService;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import android.widget.Toast;

public class BalanceActivity extends Activity {

	TextView dailyBalance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_balance);
		getOverflowMenu();

		dailyBalance = (TextView) findViewById(R.id.dailySafeToSpend);
		dailyBalance.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BalanceActivity.this,
						RegisterTransactionActivity.class);
				startActivity(intent);
			}
		});
	}

	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.balance, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_category_manager:
			Toast.makeText(getApplicationContext(), "category manager",
					Toast.LENGTH_SHORT).show();
			Intent categoryManagerIntent = new Intent(getApplicationContext(), CategoryManagerActivity.class);
			startActivity(categoryManagerIntent);
			break;

		case R.id.menu_set_reminders:
			Toast.makeText(getApplicationContext(), "set reminders",
					Toast.LENGTH_SHORT).show();
			Intent setRemindersIntent = new Intent(getApplicationContext(), ReminderSetupActivity.class);
			startActivity(setRemindersIntent);
			break;

		case R.id.menu_action_initial_setup:
			Toast.makeText(getApplicationContext(), "action initial setup",
					Toast.LENGTH_SHORT).show();
			Intent initialSetupIntent = new Intent(getApplicationContext(), InitialSetupActivity.class);
			startActivity(initialSetupIntent);
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		AlertDialog.Builder builder = new AlertDialog.Builder(BalanceActivity.this);
		builder.setTitle(R.string.initial_setup_prompt_title).setMessage(R.string.initial_setup_prompt);
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent closeApp = new Intent("com.banlinea.control.closeapp");
				sendBroadcast(closeApp);
				finish();
			}
		});
		builder.setNeutralButton(R.string.logout, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
				try {
					new AuthenticationService(BalanceActivity.this).Logout();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				finish();
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	
	
}
